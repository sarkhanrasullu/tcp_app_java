/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;

public class TCPServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(6789));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server is listening on port 6789...");

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            SelectionKey[] selectionKeysArray = selectionKeys.toArray(new SelectionKey[selectionKeys.size()]);

            for(int i=0;i<selectionKeysArray.length;i++) {
                SelectionKey key = selectionKeysArray[i];
                selectionKeys.remove(key);

                if (key.isAcceptable()) {
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("New client connected: " + clientChannel.getRemoteAddress());
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();

                    // Read the image size (4 bytes)
                    ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                    if (clientChannel.read(sizeBuffer) == -1) {
                        clientChannel.close();
                        System.out.println("Client disconnected.");
                        continue;
                    }
                    sizeBuffer.flip();
                    int imageSize = sizeBuffer.getInt();

                    // Read the image data
                    ByteBuffer imageBuffer = ByteBuffer.allocate(imageSize);
                    while (imageBuffer.hasRemaining()) {
                        if (clientChannel.read(imageBuffer) == -1) {
                            clientChannel.close();
                            System.out.println("Client disconnected.");
                            break;
                        }
                    }

                    // Save the image to a file
                    imageBuffer.flip();
                    Files.write(Paths.get("received.png"), imageBuffer.array(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    System.out.println("Image received and saved as 'received.png'.");

                    // Send a response to the client
                    ByteBuffer responseBuffer = ByteBuffer.wrap("Image received".getBytes());
                    clientChannel.write(responseBuffer);
                }
            }
        }
    }
}
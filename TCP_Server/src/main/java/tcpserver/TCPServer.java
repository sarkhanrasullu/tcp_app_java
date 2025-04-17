/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class TCPServer {

    public static void main(String[] args) throws IOException {
        // Create a selector to handle multiple channels
        Selector selector = Selector.open();

        // Open a non-blocking server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // Bind the server socket to a port
        serverSocketChannel.socket().bind(new InetSocketAddress(6789));

        // Register the server socket channel with the selector for accepting connections
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server is listening on port 6789...");

        while (true) {
            // Wait for events
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            SelectionKey[] selectionKeysArr = selectionKeys.toArray(new SelectionKey[selectionKeys.size()]);

            // Iterate over the selected keys
            for (int i = 0; i < selectionKeysArr.length; i++) {
                SelectionKey key = selectionKeysArr[i];
                selectionKeys.remove(key);
                if (key.isAcceptable()) {
                    // Accept a new client connection
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);

                    // Register the client channel with the selector for reading data
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("New client connected: " + clientChannel.getRemoteAddress());
                } else if (key.isReadable()) {
                    // Read data from a client
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        // Client has closed the connection
                        clientChannel.close();
                        System.out.println("Client disconnected.");
                    } else {
                        // Process the received data
                        buffer.flip();
                        String message = new String(buffer.array(), 0, buffer.limit());
                        System.out.println("Received: " + message);

                        // Optionally, send a response back to the client
                        buffer.clear();
                        buffer.put(new Scanner(System.in).nextLine().getBytes());
                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                }
            }
        }
    }

}

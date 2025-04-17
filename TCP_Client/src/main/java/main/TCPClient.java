package main;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // Connect to the server
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 6789));

            while(true) {
                // Send a message to the server
                String messageToSend = new Scanner(System.in).nextLine();
                ByteBuffer writeBuffer = ByteBuffer.wrap(messageToSend.getBytes());
                socketChannel.write(writeBuffer);

                // Prepare to read the server's response
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int bytesRead = socketChannel.read(readBuffer);

                if (bytesRead > 0) {
                    readBuffer.flip(); // Switch to read mode
                    byte[] responseBytes = new byte[readBuffer.remaining()];
                    readBuffer.get(responseBytes);
                    System.out.println("Server response: " + new String(responseBytes));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
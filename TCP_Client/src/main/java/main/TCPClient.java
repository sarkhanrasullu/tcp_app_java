package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TCPClient {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 6789;
        String imagePath = "test.png";

        try (SocketChannel socketChannel = SocketChannel.open()) {
            // Connect to the server
            socketChannel.connect(new InetSocketAddress(serverAddress, serverPort));
            System.out.println("Connected to the server.");

            // Read the image file into a byte array
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            int imageSize = imageBytes.length;

            // Send the image size (4 bytes)
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            sizeBuffer.putInt(imageSize);
            sizeBuffer.flip();
            socketChannel.write(sizeBuffer);

            // Send the image data
            ByteBuffer imageBuffer = ByteBuffer.wrap(imageBytes);//13913123
            while (imageBuffer.hasRemaining()) {
                socketChannel.write(imageBuffer);
            }
            System.out.println("Image sent to the server.");

            // Receive the server's response
            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(responseBuffer);
            responseBuffer.flip();
            String response = new String(responseBuffer.array(), 0, responseBuffer.limit());
            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author sarkhanrasullu
 */
public class TCPServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        readAsByte();
    }

    public static void readAsByte() throws Exception{
        ServerSocket ourFirstServerSocket = new ServerSocket(6789);//localhost, 127.0.0.1, ipconfig

        while (true) {
            System.out.println("Yeni socket uchun veya bashqa sozle desek yeni mushteri uchun gozleyirem...");
            Socket connection = ourFirstServerSocket.accept();//gozleyecek
            System.out.println("Urra yeni mushteri geldi...");
            
            DataInputStream dataStream = new DataInputStream(connection.getInputStream());
              
            byte[] arr = readMessage(dataStream);
            System.out.println("message length2="+arr.length);//500

            Files.write(Paths.get("sarkhan.jpg"), arr);
        }
    }

    public static byte[] readMessage(DataInputStream din) throws Exception{
        int msgLen = din.readInt();//1
        System.out.println("message length1="+msgLen);//500
        byte[] msg = new byte[msgLen];

        din.readFully(msg);
        return msg;
    }

    public static void readAsString() throws Exception{
        ServerSocket ourFirstServerSocket = new ServerSocket(6789);//localhost, 127.0.0.1, ipconfig

        while (true) {
            System.out.println("Yeni socket uchun veya bashqa sozle desek yeni mushteri uchun gozleyirem...");
            Socket connection = ourFirstServerSocket.accept();//gozleyecek
            System.out.println("Urra yeni mushteri geldi...");
            InputStream is = connection.getInputStream();

            InputStreamReader reader = new InputStreamReader(is);

            BufferedReader bReader = new BufferedReader(reader);//

            String messageFromClient = bReader.readLine();
            System.out.println("Mushteri deyir ki:" + messageFromClient);
        }
    }

}

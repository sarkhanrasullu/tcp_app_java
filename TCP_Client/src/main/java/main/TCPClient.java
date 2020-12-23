/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author sarkhanrasullu
 */
public class TCPClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("192.168.1.103", 6789);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        
        byte[] bytes = Files.readAllBytes(Paths.get("/Users/sarkhanrasullu/Desktop/Java.png"));
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        socket.close();
        
    }
}

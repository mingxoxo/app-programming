package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
//출처 https://copycoding.tistory.com/240
public class MainServer implements Runnable {
 
    public static final int ServerPort = 9999;
    public static final String ServerIP = "172.16.54.1";
 
    @Override
    public void run() {
 
        try {
            System.out.println("S: Connecting...");
            ServerSocket serverSocket = new ServerSocket(ServerPort);
            
            while (true) {
            	//client 접속 대기
            	
                Socket client = serverSocket.accept();
                System.out.println("S: Receiving...");
                
                try {
                	//client data 수신
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String str = in.readLine();
                    System.out.println("S: Received: '" + str + "'");
                     
                    //client에 다시 전송
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                    out.println("Server Received " + str);
                    
                } catch (Exception e) {
                    System.out.println("S: Error");
                    e.printStackTrace();
                } finally{
                    client.close();
                    System.out.println("S: Done.");
                }
            }
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
 
        Thread desktopServerThread = new Thread(new MainServer());
        desktopServerThread.start();
 
    }
}

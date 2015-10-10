/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static maintance.MainClass.port;

/**
 *
 * @author Felipe
 */
public class WebServer {
    private ServerSocket socket;
    private String host;
    private int port;
    private ArrayList<WebClient> clients = new ArrayList<WebClient>();
    
    private Thread serverThread;
    private Thread communicationThread;
    
    public WebServer(String host, int port) {
        this.host = host;
        this.port = port;
        
        System.out.println("Server started at "+host+" listening on Port "+port);
        
        try
        {
            socket = new ServerSocket(port);
            serverThread = new Thread(new Runnable() {
                   
                /*
                    Thread necessário para que múltiplos clientes possam se conectar.
                */
                
                @Override
                public void run() {                                       
                    while(true)
                    {
                        try
                        {
                            Socket client = socket.accept(); // Espera até que um Cliente se conecte.                            
                            
                            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            PrintWriter output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
                            output.println("Returned GET");
                            output.flush();
                            System.out.println("Requisição HTTP: "+br.readLine());                                                                             
                            System.out.println("Client Connected");                         
                            
                        }
                        catch(IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            communicationThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while(true)
                    {
                        for(int a  = 0; a < clients.size(); a++)
                        {
                            /*try
                            {
                                new DataInputStream(clients.get(a).getSocket().getInputStream()).readUTF();
                            }
                            catch(IOException ex) {
                                ex.printStackTrace();
                            }*/
                        }
                    }
                }
            });
            
            serverThread.start();
            communicationThread.start();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }   
    
    public void addWebClient(WebClient webClient) {        
        clients.add(webClient);
    }
    
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }    
    
    public ServerSocket getSocket() {
        return socket;
    } 
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appguiswing;

/**
 *
 * @author Student233534
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class Server extends Thread{

	private BufferedReader reader;
	private Socket clientSocket;
	ArrayList<SOAPMessage> inbox;
        private NodeApp node;
	
	public Server(Socket clientSocket, ArrayList<SOAPMessage> inbox, NodeApp nodeApp)
	{
                this.node=nodeApp;
		this.clientSocket = clientSocket;
		this.inbox = inbox;
		try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	public void run() {
		String inputLine="";
		while(true) {
			try {
				while((inputLine = reader.readLine()) != "\n"){
				InputStream inputStream = new ByteArrayInputStream(inputLine.getBytes());
				SOAPMessage soap = MessageFactory.newInstance().createMessage(null, inputStream);
				node.addToInbox(soap);
				}
			} catch (IOException | SOAPException e) {
				e.printStackTrace();
			} 
			
		}
		
	}
        
}

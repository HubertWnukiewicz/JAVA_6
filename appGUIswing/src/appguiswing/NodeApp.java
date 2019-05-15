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

import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.soap.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class NodeApp extends JFrame{

	public MessageFactory msgFactory;
	public SOAPMessage soapMsg;
	public SOAPPart soapPart;
	public SOAPEnvelope soapEnvelope;
	public SOAPHeader soapHeader;
	public SOAPBody soapBody;
	public static ServerSocket serverSocket;
	public static Socket clientSocket;
	public static Socket nextNodeSocket;
	public static Thread serverThread;
        public static NodeApp nodeApp;
	public int portA;
        public int portB;
        public static String name;
	public static ArrayList<SOAPMessage> inbox;
        
        public ClientAppForm clientAppForm;
	
	
	
	public NodeApp(String name,int PortA, int PortB){
		
		this.portA=PortA;
                this.portB=PortB;
                //nodeApp=this;
                this.name=name;
		inbox = new ArrayList<>();
		
		try {
			msgFactory = MessageFactory.newInstance();
			soapMsg = msgFactory.createMessage();
			soapPart = soapMsg.getSOAPPart();
			soapEnvelope = soapPart.getEnvelope();
			soapHeader = soapEnvelope.getHeader();
			soapBody = soapEnvelope.getBody();
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
               clientAppForm=new ClientAppForm(this);
        }
		
        public int addToInbox(SOAPMessage soapMess)
        {
            	try {
			msgFactory = MessageFactory.newInstance();
			soapMsg = msgFactory.createMessage();
			soapPart = soapMsg.getSOAPPart();
			soapEnvelope = soapPart.getEnvelope();
			soapHeader = soapEnvelope.getHeader();
			soapBody = soapEnvelope.getBody();
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
            
            String nMess=null;
            try {
                nMess = soapMess.getSOAPBody().getTextContent();
                System.out.print("nMess= "+nMess);
            } catch (SOAPException ex) {
                Logger.getLogger(NodeApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            String messTo = null;
            try { 
                messTo = soapMess.getSOAPHeader().getTextContent();
                System.out.print("messTo= "+messTo);
            } catch (SOAPException ex) {
                Logger.getLogger(NodeApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                 
            if(!(messTo.equals(this.name)))
            {
                if(this.name.equals(String.valueOf(nMess.charAt(13)))){
                    if(!messTo.equals("toAll"))
                        clientAppForm.logsArea.append("RECEIVER NOT FOUND\n");
                    return 0;
                }
            }
            
            
            if(messTo.equals(this.name) || messTo.equals("toAll")){
                    try {
                        clientAppForm.inboxField.append(soapMess.getSOAPBody().getTextContent()+"\n");
                } catch (SOAPException ex) {
                    Logger.getLogger(NodeApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                try {
                     
                    if(!messTo.equals("toAll"))
                        clientAppForm.logsArea.setText("Unicast message from " + nMess.charAt(13) + " to " + messTo + " forwarded\n");
                    else
                        clientAppForm.logsArea.setText("Broadcast messages from " + nMess.charAt(13) + " forwarded\n");
                    
                        System.out.println("PRZESYLANIE WIADOMOSCI DALEJ ");
                        
                        nMess = soapMess.getSOAPBody().getTextContent();
                    
                        soapHeader.addTextNode(messTo);
                        
                        SOAPBodyElement element = soapBody.addBodyElement(soapEnvelope.createName("JAVA", "LAB", "6"));
                        element.addChildElement("test").addTextNode(nMess.substring(0,15)+" ");

                        nMess = soapMess.getSOAPBody().getTextContent();
                        element.addTextNode(nMess.substring(16,nMess.length()));
                        PrintStream out = new PrintStream(nextNodeSocket.getOutputStream(), true);
                        
                        nMess = soapMess.getSOAPBody().getTextContent();
                        System.out.println(nMess);
                        soapMsg.writeTo(out);
                        out.flush();
                        out.print("\n");
                        out.flush();
                } catch (SOAPException | NumberFormatException | IOException e1) {
                        e1.printStackTrace();
                }
            }
            return 0;
        }
        
	
	
}

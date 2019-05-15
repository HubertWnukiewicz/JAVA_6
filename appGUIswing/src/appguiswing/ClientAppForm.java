/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appguiswing;

import static appguiswing.NodeApp.nextNodeSocket;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;

/**
 *
 * @author Admin
 */
public class ClientAppForm extends JFrame {
	private JPanel recievedMSG;
	private JPanel sendMSG;
        private JPanel logsMSG;
	private JTextField nameField, myPort, tarPort;
	private JTextField toField, messageField;

	public static JTextArea writeMSG, logsArea, inboxField;
	private JButton connectButton, sendButton;

	private JLabel nameLabel, myPortLabel, tarPortLabel;
	private JLabel toLabel, messageLabel;
	private JScrollPane scroll;
        private JComboBox chooseClient;
        
        public ClientAppForm(NodeApp nodeApp)
        {
                super(nodeApp.name);
		setSize(450, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
        
        
                recievedMSG = new JPanel();
		recievedMSG.setBounds(12, 400, 259, 348);
		recievedMSG.setBorder(BorderFactory.createTitledBorder("Inbox"));
		getContentPane().add(recievedMSG);
		recievedMSG.setLayout(null);
		
		//messageLabel = new JLabel("MSG:");
		//messageLabel.setBounds(12, 23, 56, 16);
		//recievedMSG.add(messageLabel);
				
		inboxField = new JTextArea();
		inboxField.setBounds(12, 52, 224, 242);
		inboxField.setLineWrap(true);
                inboxField.setEditable(false);
		recievedMSG.add(inboxField);
			
		sendMSG = new JPanel();
		sendMSG.setBounds(12, 57, 260, 190);
		sendMSG.setBorder(BorderFactory.createTitledBorder("New message:"));
		sendMSG.setLayout(null);
		add(sendMSG);  
                
               
                chooseClient=new JComboBox(new String[]{"unicast","broadcast"});
                chooseClient.setBounds(12, 45, 120, 20);
                
                chooseClient.addActionListener(e -> {
			if(chooseClient.getSelectedIndex()==0)
                            toField.setEditable(true);
                        else{
                            toField.setEditable(false);
                            toField.setText("");
                        }
		});
                add(chooseClient);
                //sendMSGadd(chooseClient);
                
                logsMSG = new JPanel();
		logsMSG.setBounds(12, 250, 260, 150);
		logsMSG.setBorder(BorderFactory.createTitledBorder("Logs:"));
		logsMSG.setLayout(null);
		add(logsMSG);  
                
                logsArea = new JTextArea();
		logsArea.setBounds(12, 20, 224, 120);
                logsArea.setEditable(false);
		logsMSG.add(logsArea);
		
		connectButton = new JButton("Activate");
		connectButton.setBounds(12, 155, 97, 25);
		connectButton.addActionListener(e -> {
                    try{
                        nextNodeSocket = new Socket("localhost", nodeApp.portB);
                        connectButton.setVisible(false);
                        
                    }catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }
		});
		sendMSG.add(connectButton);
		
		writeMSG = new JTextArea();
		scroll = new JScrollPane(writeMSG);
		scroll.setBounds(12, 80, 224, 70);
		sendMSG.add(scroll);
		
		toLabel = new JLabel("To:");
		toLabel.setBounds(12, 24, 42, 16);
		sendMSG.add(toLabel);
		
		toField = new JTextField();
		toField.setBounds(52, 21, 184, 22);
		sendMSG.add(toField);
		
		
		sendButton = new JButton("Send");
		sendButton.setBounds(124, 155, 112, 25);
		sendButton.addActionListener(e -> {
			try {
                            
                                try {
                                    nodeApp.msgFactory = MessageFactory.newInstance();
                                    nodeApp.soapMsg = nodeApp.msgFactory.createMessage();
                                    nodeApp.soapPart = nodeApp.soapMsg.getSOAPPart();
                                    nodeApp.soapEnvelope = nodeApp.soapPart.getEnvelope();
                                    nodeApp.soapHeader = nodeApp.soapEnvelope.getHeader();
                                    nodeApp.soapBody = nodeApp.soapEnvelope.getBody();
                                 } catch (SOAPException e1) {
                                         e1.printStackTrace();
                                 }
                            
                                if(chooseClient.getSelectedIndex()==0)
                                    nodeApp.soapHeader.addTextNode(toField.getText());
                                else
                                    nodeApp.soapHeader.addTextNode("toAll");

				SOAPBodyElement element = nodeApp.soapBody.addBodyElement(nodeApp.soapEnvelope.createName("JAVA", "LAB", "6"));
				element.addChildElement("test").addTextNode("Message From " + nodeApp.name + ": ");
				
				element.addTextNode(writeMSG.getText());
				PrintStream out = new PrintStream(nextNodeSocket.getOutputStream(), true);
                                
				nodeApp.soapMsg.writeTo(out);
				out.flush();
				out.print("\n");
				out.flush();
			} catch (SOAPException | NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
		});
		sendMSG.add(sendButton);
		
		nameLabel = new JLabel("Name:");
		nameLabel.setBounds(12, 13, 45, 16);
		add(nameLabel);
		
		myPortLabel = new JLabel("My Port:");
		myPortLabel.setBounds(134, 13, 56, 16);
		add(myPortLabel);
		
		tarPortLabel = new JLabel("TargetPort:");
		tarPortLabel.setBounds(278, 12, 82, 19);
		add(tarPortLabel);
		
		nameField = new JTextField(nodeApp.name);
		nameField.setEditable(false);
		nameField.setBounds(69, 10, 53, 22);
		add(nameField);
		
		myPort = new JTextField(Integer.toString(nodeApp.portA));
		myPort.setEditable(false);
		myPort.setBounds(202, 10, 53, 22);
		add(myPort);
		
		tarPort = new JTextField(Integer.toString(nodeApp.portB));
		tarPort.setEditable(false);
		tarPort.setBounds(366, 10, 53, 22);
		getContentPane().add(tarPort);
		
		setVisible(true);
	}
    
        }


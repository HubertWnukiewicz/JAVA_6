/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appguiswing;

import java.awt.EventQueue;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.BindException;
import java.net.ServerSocket;

/**
 *
 * @author Admin
 */
public class Main {
    static NodeApp nodeApp;
    private static int portB;
    private static int portA;
    private static String name;
    public static void main(String[] args) {
            
            name = args[0];
            portA=Integer.parseInt(args[1]);
            portB=Integer.parseInt(args[2]);
            
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					nodeApp = new NodeApp(name,portA,portB);
					nodeApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			nodeApp.serverSocket = new ServerSocket(portA);
			nodeApp.clientSocket = nodeApp.serverSocket.accept();
			nodeApp.serverThread = new Server(nodeApp.clientSocket, nodeApp.inbox, nodeApp);
			nodeApp.serverThread.start();
		} catch (NumberFormatException | IOException el) {
		 System.out.println("Scoket is already in use.");
                 exit(0);
		}
	}
}

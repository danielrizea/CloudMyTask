package com.cloudmytask.connectors.tcp;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.cloudmytask.connectors.CallbackInterface;

// Obiect ce implementeaza interfata CallbackInterface, asociat connector-ului de socketi.
// Practic, trimite rezultatele inapoi la client prin intermediul unui socket.
public class SocketConnectorTCPCallbackObject implements CallbackInterface {
	private ServerSocket socket;
	private InetAddress addr;
	private int port;
	private Socket clientSocket;
	
	public SocketConnectorTCPCallbackObject(ServerSocket socket, InetAddress addr, int port,Socket clientSocket) {
		this.socket = socket;
		this.addr = addr;
		this.port = port;
		this.clientSocket = clientSocket;
	}
	
	public void sendResult(Object obj) {
		
		try {
			//Socket clientSocket = null;
			
			//clientSocket = this.socket.accept();
			//System.out.println("result");
			InetAddress clientAddr = this.socket.getInetAddress();
			int clientPort = this.socket.getLocalPort();
			
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(obj);
            //out.close();

		} catch (Exception e) {
			System.out.println("Eroare "+e.getMessage());
		}
	}
	
	public String toString() {
		return "" + this.addr + ":" + this.port;
	}
}

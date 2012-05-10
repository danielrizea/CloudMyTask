package com.cloudmytask.connectors.udp;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import com.cloudmytask.connectors.CallbackInterface;

// Obiect ce implementeaza interfata CallbackInterface, asociat connector-ului de socketi.
// Practic, trimite rezultatele inapoi la client prin intermediul unui socket.
public class SocketConnectorUDPCallbackObject implements CallbackInterface {
	private DatagramSocket socket;
	private InetAddress addr;
	private int port;
	
	public SocketConnectorUDPCallbackObject(DatagramSocket socket, InetAddress addr, int port) {
		this.socket = socket;
		this.addr = addr;
		this.port = port;
	}
	
	public void sendResult(Object obj) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] outputBuf = baos.toByteArray();
			DatagramPacket ansPacket = new DatagramPacket(outputBuf, 0, outputBuf.length, this.addr, this.port);
			this.socket.send(ansPacket);
			oos.close();
			baos.close();
		} catch (Exception e) {
		}
	}
	
	public String toString() {
		return "" + this.addr + ":" + this.port;
	}
}

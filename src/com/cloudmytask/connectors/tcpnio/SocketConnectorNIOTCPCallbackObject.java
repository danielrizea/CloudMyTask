package com.cloudmytask.connectors.tcpnio;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SelectionKey;

import com.cloudmytask.connectors.CallbackInterface;

public class SocketConnectorNIOTCPCallbackObject implements CallbackInterface {
	private CMTServiceSocketConnectorNIOTCP connector;
	private SelectionKey key;
	
	public SocketConnectorNIOTCPCallbackObject(CMTServiceSocketConnectorNIOTCP connector, SelectionKey key) {
		this.connector = connector;
		this.key = key;
	}
	
	public void sendResult(Object result) {
		try {
			//Serializam rezultatul.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(result);
			byte[] outputBuf = baos.toByteArray();
			
			// Apoi il dam spre a fi trimis catre client.
			
			this.connector.sendData(this.key, outputBuf);
		} catch (Exception e) {
			System.err.println("sendResult exception: " + e);
			e.printStackTrace();
		}
	}
}

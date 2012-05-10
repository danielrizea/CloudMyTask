package com.cloudmytask.connectors.tcpnio;

import java.nio.channels.*;

public class ReadJob implements Runnable {
	private CMTServiceSocketConnectorNIOTCP server;
	private SelectionKey key;
	
	public ReadJob(CMTServiceSocketConnectorNIOTCP server, SelectionKey key) {
		this.server = server;
		this.key = key;
	}
	
	public void run() {
		try {
			this.server.doRead(this.key);
		} catch (Exception e) {
			System.err.println("ReadJob exception: " + e);
			e.printStackTrace();
		}
	}
}

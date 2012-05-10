package com.cloudmytask.connectors.tcpnio;

import java.nio.channels.*;

public class WriteJob implements Runnable {
	private CMTServiceSocketConnectorNIOTCP server;
	private SelectionKey key;
	
	public WriteJob(CMTServiceSocketConnectorNIOTCP server, SelectionKey key) {
		this.server = server;
		this.key = key;
	}
	
	public void run() {
		try {
			this.server.doWrite(this.key);
		} catch (Exception e) {
			System.err.println("WriteJob exception: " + e);
			e.printStackTrace();
		}
	}
}

package com.cloudmytask.connectors.tcpnio;

import java.nio.channels.*;

public class AcceptJob implements Runnable {
	private CMTServiceSocketConnectorNIOTCP server;
	private SelectionKey key;
	
	public AcceptJob(CMTServiceSocketConnectorNIOTCP server, SelectionKey key) {
		this.server = server;
		this.key = key;
	}
	
	public void run() {
		try {
			this.server.doAccept(this.key);
		} catch (Exception e) {
			System.err.println("AcceptJob exception: " + e);
			e.printStackTrace();
		}
	}
}

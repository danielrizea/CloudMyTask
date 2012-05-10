package com.cloudmytask.connectors.tcpnio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIOTCPChangeRequest {
	public SelectionKey key;
	public SocketChannel socketChannel;
	public int newOps;
	
	public NIOTCPChangeRequest(SelectionKey key, int newOps) {
		this.key = key;
		this.newOps = newOps;
		this.socketChannel = null;
	}
	
	public NIOTCPChangeRequest(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
}

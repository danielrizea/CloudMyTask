package com.cloudmytask.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastGroup {
	private InetAddress groupAddress;
	private int port;

	public InetAddress getGroupAddress() {
		return groupAddress;
	}

	public int getPort() {
		return port;
	}

	public MulticastGroup(String groupAddress, int port) throws UnknownHostException {
		super();
		this.groupAddress = InetAddress.getByName(groupAddress);
		this.port = port;
	}
}


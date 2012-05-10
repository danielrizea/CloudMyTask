package com.cloudmytask.connectors.tcpnio;

import java.nio.channels.*;

public interface NIOTCPSocketConnector {
	public void sendData(SelectionKey key, byte[] data);
}

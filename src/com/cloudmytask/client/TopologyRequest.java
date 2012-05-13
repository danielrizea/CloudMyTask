package com.cloudmytask.client;

import java.io.Serializable;
import java.util.HashMap;

public class TopologyRequest implements Serializable {
	public static final long serialVersionUID = 89299;

	public static final int HELLO_MESSAGE = 1;
	public static final int UPDATE_TOPOLOGY = 2;

	
	public int type;
	
	public String message;
	
	public int connections[][];
	
	public TopologyRequest(String message, int type) {
		this.type = type;
		this.message = message;
	}
	
	public String toString() {
		return "" + this.hashCode() + ":" + this.type;
	}
}

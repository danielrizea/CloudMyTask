package com.cloudmytask.client;

import java.io.*;

public class Request implements Serializable {
	public static final long serialVersionUID = 89283;

	public int type;
	public int scriptID;
	
	public int requestID;
	
	
	public String scriptFileData;
	public String scriptFileName;
	public String clientID;
	
	public String message ;
	
	public Request(String message, int type) {
		this.type = type;
		this.message = message;
	}
	
	public String toString() {
		return "" + this.hashCode() + ":" + this.type;
	}
}

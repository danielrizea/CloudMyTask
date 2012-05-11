package com.cloudmytask.client;

import java.io.*;

public class Request implements Serializable {
	public static final long serialVersionUID = 89283;

	public static final int REQUEST_PROCESS_SCRIPT = 1;
	public static final int REQUEST_GET_LOAD = 2;
	
	public int type;
	public int scriptID;
	
	public String requestID;
	public String answer;
	
	public String scriptFileData;
	public String scriptFileName;
	public String clientID;
	
	public String message ;
	public float loadFactor;
	
	
	public Request(String message, int type) {
		this.type = type;
		this.message = message;
	}
	
	public String toString() {
		return "" + this.hashCode() + ":" + this.type;
	}
}

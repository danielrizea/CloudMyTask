package com.cloudmytask.client;

import java.io.Serializable;
import java.util.HashMap;

public class Request implements Serializable {
	public static final long serialVersionUID = 89283;

	public static final int REQUEST_PROCESS_SCRIPT = 1;
	public static final int REQUEST_GET_LOAD = 2;
	//from one instance to another
	public static final int REQUEST_PASS_SCRIPT = 3;
	
	public int type;
	public int scriptID;
	
	public String requestID;
	public String answer;
	
	public String scriptFileData;
	public String scriptFileName;
	public String clientID;
	
	public String message ;
	public float loadFactor;
	
	
		// req pentru a verifica daca un client e banned
		public static final int R_IS_BANNED = 6;
		// req pentru a adauga un client la lista de banned
		public static final int R_ADD_BANNED = 7;
		// req pentru a face update a starii unei masini (s-a eliberat, este busy etc)
		public static final int R_UPDATE_STATUS = 8;
		// req pentru a afla care din vecini e available pentru a-i pasa job
		public static final int R_GET_AVAILABLE = 9;

		// va fi setat pentru a informa masina centrala de gradul de load al instanteti
		public Integer state;
		// va fi setat cand se va trimite o cerere catre masina centrala pentru a afla care este vecinul liber
		public HashMap<String,int[]> neighbours;
		
		// in cazul in care s-a facut o cerere sa se verifice daca un client e banned se seteaza cu true/false
		public Boolean bannedInfo;	
		// pt cerere de tip available neighbors -> se primeste un string cu clientID-ul pentru respectivul vecin
		public String neighborID;
	
	
	public Request(String message, int type) {
		this.type = type;
		this.message = message;
	}
	
	public String toString() {
		return "" + this.hashCode() + ":" + this.type;
	}
}

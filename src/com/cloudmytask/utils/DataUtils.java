package com.cloudmytask.utils;

import java.io.*;

public class DataUtils {
	public static byte[] encrypt(byte [] b) {
		byte[] bnew = new byte[b.length];
		
		for (int i = 0; i < 4; i++) {
			bnew[i] = b[i];
		}

		for (int i = 4; i < b.length; i++) {
			bnew[i] = (byte) ((((int) b[i] + 150) % 256) - 128);
		}
		
		//System.out.println("[DataUtils.encrypt] Encoded len=" + bnew[0] + ":" + bnew[1] + ":" + bnew[2] + ":" + bnew[3]);

		return bnew;
	}
	
	public static byte[] decrypt(byte [] b) {
		byte[] bnew = new byte[b.length];

		for (int i = 0; i < 4; i++) {
			bnew[i] = b[i];
		}
		
		for (int i = 4; i < b.length; i++) {
			bnew[i] = (byte) ((((int) b[i] + 128 + 256 - 22) % 256) - 128);
		}

		return bnew;
	}

	public static byte[] encode(Object obj) {
		// Serializam obiectul.
		byte[] serializedObject = null;
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			serializedObject = baos.toByteArray();
		} catch (Exception e) {
			System.err.println("[DataUtils.encode] Eroare la encode: " + e);
			e.printStackTrace();
			return null;
		}
		
		// Apoi adaugam pe primii 4 bytes dimensiunea sa.
		byte[] encodedObject = new byte[serializedObject.length + 4];
		//serializare
		int len = serializedObject.length;
		encodedObject[0] = (byte) (((len >> 24) % 256) - 128);
		encodedObject[1] = (byte) (((len >> 16) % 256) - 128);
		encodedObject[2] = (byte) (((len >> 8) % 256) - 128);
		encodedObject[3] = (byte) ((len % 256) - 128);
		
		int ln = ((128 + (int) encodedObject[0]) << 24) + ((128 + (int) encodedObject[1]) << 16) + ((128 + (int) encodedObject[2]) << 8) + (128 + (int)encodedObject[3] );

		for (int i = 0; i < serializedObject.length; i++) {
			encodedObject[i + 4] = serializedObject[i];
		}
		
		System.out.println("Encoded Object :");
		for(int i=0;i<encodedObject.length;i++)
			System.out.print(encodedObject[i] +" ");
		//System.out.println();
		return encodedObject;
	}
	//decodeaza mesajul
	public static Object decode(byte[] buffer) {
		if (buffer.length < 4)
			return null;

		if (buffer.length < 4) {
			return null;
		}
		//determina lungimea
		int len = ((128 + (int) buffer[0]) << 24) + ((128 + (int) buffer[1]) << 16) + ((128 + (int) buffer[2]) << 8) + (128 + (int) buffer[3]);
		if (buffer.length != 4 + len) {
			return null;
		}

		byte[] serializedObject = new byte[buffer.length - 4];
		for (int i = 0; i < len; i++) {
			serializedObject[i] = buffer[i + 4];
		}
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedObject));
			Object obj = ois.readObject();
			return obj;
		} catch (Exception e) {
			System.err.println("[DataUtils.decode] Eroare la decode: " + e);
			e.printStackTrace();
			return null;			
		}	
	}
	// functie folosita cand nu se primeste tot mesajul
	public static byte[] getCompleteRequest(byte[] buffer) {
		if (buffer.length < 4 ) {
			return null;
		}
		
		int len = ((128 + (int) buffer[0]) << 24) + ((128 + (int) buffer[1]) << 16) + ((128 + (int) buffer[2]) << 8) + (128 + (int) buffer[3]);

		if (buffer.length < 4 + len) {
			return null;
		}
	
		byte[] request = new byte[len + 4];
		for (int i = 0; i < len + 4; i++) {
			request[i] = buffer[i];
		}

		return request;		
	}
}

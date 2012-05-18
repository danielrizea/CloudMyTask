package com.cloudmytask;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.ini4j.*;

public class ReadIni4jConfig {
	public static final String testConfigFileName = "configs/config.ini";
	
	private int connections[][] ;
	private int communicationType;
	
	public ReadIni4jConfig ()
	{
	
		this.connections = new int[100][100];
		Ini ini;
		try {
			ini = new Ini(new File(testConfigFileName));
		} catch (Exception e) {
			System.err.println("Exception opening the testConfigFile:" + e);
			e.printStackTrace();
			return;
		}

		/* prelucrare matrice de adiacenta */
		Ini.Section iniConnection = ini.get("connections");
		
		String connectionMatrix=  iniConnection.get("matrix");
		int connectionMatrixLength = connectionMatrix.length();
		
		boolean isEnd = false;
		int i = 0 ; 
		int nrCol = 0;
		int nrLine = 0; 
		while (i < connectionMatrixLength)
		{
			//e inceput de linie dintr-o matrice
			if ( connectionMatrix.charAt(i) == '{'){
				i++;
			}
			//e sfarsitul unei linii
			else if ( connectionMatrix.charAt(i) == '}'){
				nrCol ++;
				nrLine = 0;
				i++;
			}
			else if ( ( connectionMatrix.charAt(i) == ',') || ( connectionMatrix.charAt(i) == ' ') )
			{
				i++;
			}
			else {

				String aux = String.valueOf(connectionMatrix.charAt(i));
				this.connections[nrCol][nrLine] = Integer.parseInt(aux);
				i++;
				nrLine++;
			}
			
		}
		/*
		 //afisare matrice de configurare
		 int j;

		System.out.println("col:" + nrCol);
		nrCol = nrCol -1;
		for (i = 0 ; i < nrCol ; i++){
			for (j = 0; j < nrCol; j++)
			{
				System.out.print(conn[i][j] );
				System.out.print(' ');
			}
			System.out.println();
		}
		System.out.println("linie" + nrLine);
		*/
		//prelucrare tip protocol
		Ini.Section iniProtocol = ini.get("protocol");
		
		this.communicationType = Integer.parseInt(iniProtocol.get("type"));
		
		/* modificare variabile din GlobalConfig */
		GlobalConfig.connections = this.connections;
		GlobalConfig.CommunicationType = this.communicationType;

	}
}



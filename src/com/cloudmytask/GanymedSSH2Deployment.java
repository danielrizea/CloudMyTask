package com.cloudmytask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ini4j.Ini;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class GanymedSSH2Deployment {

	public static final String IniConfigFileName = "configs/ssh_config.ini";
	
	/* Parametri: conn: conexiunea deschisa; 
				  localDirectory: directorul din care se vor copia fisierele; 
				  remoteTargetDirectory: directorul in care se vor copia fisierle din directorul primit ca parametru
	 * functia copiaza recursiv toate folderele si fisierele din localDirectory
	 */
				
	private static void putDir(Connection conn, String localDirectory, String remoteTargetDirectory) throws IOException {	
		File dir = new File(localDirectory);
	    final String[] fileList = dir.list();
	    for (String file : fileList) {
	        final String fullFileName = localDirectory + "/" + file;
	        if (new File(fullFileName).isDirectory()) {
	        	//nu va copia fisierele din folderele .svn
	        	if (file.compareTo(".svn") != 0) {
		            final String subDir = remoteTargetDirectory + "/" + file;
		            Session sess = conn.openSession();
		            sess.execCommand("mkdir " + subDir);
		            sess.waitForCondition(ChannelCondition.EOF, 0);
		            putDir(conn, fullFileName, subDir);
	        	}
	        }
	        else {
	        	//copiez fisierul in remoteTargetDirectory
	        	SCPClient scpc = conn.createSCPClient();
	        	scpc.put(fullFileName, remoteTargetDirectory);
	        	System.out.println(fullFileName);
	        }
	    }
	}
	
	public static void main(String args[]) throws IOException {
	
		Ini ini;

		try {
			ini = new Ini(new File(IniConfigFileName));
		} catch (Exception e) {
			System.err.println("Exception opening the IniConfigFile:" + e);
			e.printStackTrace();
			return;
		}

		Ini.Section ssh_config = ini.get("ssh_config");
		Connection ssh_conn = new Connection(ssh_config.get("ssh_address"));
		System.out.println("Connecting to "+ssh_config.get("ssh_address"));
		ConnectionInfo ssh_conn_info = null;
		boolean conn_result = false;

		try {
			ssh_conn_info = ssh_conn.connect();
			conn_result = ssh_conn.authenticateWithPassword(ssh_config.get("ssh_user"), ssh_config.get("ssh_password"));
		} catch (Exception e) {
			System.err.println("Error connecting to " + ssh_config.get("ssh_address") + " using SSH: " + e);
			e.printStackTrace();
		}
		
		
		 
		if (conn_result) {
			try {		
				 Session sess = ssh_conn.openSession();
				// Creeaza directorul testCloudMyTasks (mai intai il sterge, daca exista).
				Session ssh_sess = ssh_conn.openSession();
				String cmdString = "rm -r CloudMyTasks; mkdir CloudMyTasks";
				System.out.println("Executing command via SSH: " + cmdString);
				ssh_sess.execCommand(cmdString);
				Integer exit_status;
	
				do {
					Thread.sleep(1000);
					exit_status = ssh_sess.getExitStatus();
				} while (exit_status == null);
				
				System.out.println("exit_status=" + exit_status.intValue());
				
				//copiez toate fisierele din bin in folderul CloudMyTasks de pe server
				putDir(ssh_conn, "bin", "CloudMyTasks");
				System.out.println("List Files in CloudMyTasks");
				
				//afiseaza directoarele din folderul CloudMyTasks de pe server
				sess.execCommand("tree CloudMyTasks");

	            InputStream stdout = new StreamGobbler(sess.getStdout());
	            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
	            while (true)
	            {
	                String line = br.readLine();
	                if (line == null)
	                    break;
	                System.out.println(line);
	            }
	            /* Show exit status, if available (otherwise "null") */
	            System.out.println("ExitCode: " + sess.getExitStatus());
	            /* Close this session */
	            sess.close();
			
			} catch (Exception e) {
				System.err.println("SSH error: " + e);
				e.printStackTrace();
			}			
		}
		
	}

}

package com.cloudmytask;

import java.io.*; 

import org.ini4j.*;

import ch.ethz.ssh2.*;

public class GanymedSSH2Deployment {

	public static final String IniConfigFileName = "configs/ssh_config.ini";
	/* ssh_keyfile !!! */
//	public static final String ssh_keyfile = "configs/id_rsa";
	
	public static void main(String args[]) {
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
				// Creeaza directorul testCloudMyTasks (mai intai il sterge, daca exista).
				Session ssh_sess = ssh_conn.openSession();
				String cmdString = "rm -f -r testCloudMyTasks ; mkdir testCloudMyTasks";
				System.out.println("Executing command via SSH: " + cmdString);
				ssh_sess.execCommand(cmdString);
				Integer exit_status;
	
				do {
					Thread.sleep(100);
					exit_status = ssh_sess.getExitStatus();
				} while (exit_status == null);
				
				System.out.println("exit_status=" + exit_status.intValue());
				ssh_sess.close();
	
				// Copiaza 2 fisiere in directorul nou creat.
				SCPClient scp_client = ssh_conn.createSCPClient();
/* TODO de modificat */
				String local_files[] = new String[2];		
				local_files[0] = "configs/destinations.conf";
				local_files[1] = "configs/sample.ini";
					
				scp_client.put(local_files, "testCloudMyTasks");

				// Verifica ca fisierele sunt acolo.
				ssh_sess = ssh_conn.openSession();
				cmdString = "ls testCloudMyTasks/";
				System.out.println("Executing command via SSH: " + cmdString);
				ssh_sess.execCommand(cmdString);
	
				do {
					Thread.sleep(100);
					exit_status = ssh_sess.getExitStatus();
				} while (exit_status == null);
				
				System.out.println("exit_status=" + exit_status.intValue());

				// Obtine stdout-ul comenzii SSH si afisaza-l.
				BufferedReader reader = new BufferedReader(new InputStreamReader(ssh_sess.getStdout()));
				//afiseaza ce genereaza comanda ls
				String line;
				while ((line = reader.readLine()) != null) {	
				    System.out.println("### ls output line: " + line);
				}
				/* execut programul */
				ssh_sess.close();				
			} catch (Exception e) {
				System.err.println("SSH error: " + e);
				e.printStackTrace();
			}			
		}
	}

}

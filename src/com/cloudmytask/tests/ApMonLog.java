
package com.cloudmytask.tests;

import apmon.ApMon;

/**
 * The Class ApMonLog.
 */
public class ApMonLog {
	
	private static ApMonLog singletonInstance = null;
	
	public static final String ConfigFileName = "configs/destinations.conf";
	public static final String LogMachineName = "CMTTest";
	
	//numele testului
	public static final String TestName = "Testing_full_capacity";
	public ApMon apm = null;
	
	protected ApMonLog() {		
		apm = null;
		try {
			apm = new ApMon(ConfigFileName);
		} catch (Exception e) {
			System.err.println("Eroare creare instanta ApMon: " + e);
			e.printStackTrace();
		}
	}
	//returneaza instanta
	public static ApMonLog getInstance(){
		if(singletonInstance == null){
			singletonInstance = new ApMonLog();
		}
		return singletonInstance;
	}
	
	//Log message
	public void logMessage(String parameterName, Integer parameterType, Integer parameterValue){
		
		if(apm != null){
			try{
				apm.sendParameter(LogMachineName, TestName, parameterName, parameterValue);
			}
			catch(Exception e){
				System.err.println("Exception in sending apm log message " + e.getMessage());
			}
		}
		else{
			System.err.println("The apm was not instantiated");
		}
	}

}

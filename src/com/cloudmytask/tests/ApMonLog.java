
package com.cloudmytask.tests;

import apmon.ApMon;

/**
 * The Class ApMonLog.
 */
public class ApMonLog {
	
	private static ApMonLog singletonInstance = null;
	
	/** The Constant ConfigFileName. */
	public static final String ConfigFileName = "/home/daniel/Desktop/PDSD/lab/lab7/conf/destinations.conf";
	
	/** The Constant LogMachineName. */
	public static final String LogMachineName = "CMTTest";
	
	/** The name of test. */
	public static final String TestName = "Testing_full_capacity";
	
	/** The apm. */
	public ApMon apm = null;
	
	/**
	 * Instantiates a new ap mon log.
	 *
	 * @param nameOfTest the name of test
	 */
	protected ApMonLog() {	
	
		apm = null;
		
		try {
			apm = new ApMon(ConfigFileName);
		} catch (Exception e) {
			System.err.println("Eroare creare instanta ApMon: " + e);
			e.printStackTrace();
		}
	}

	public static ApMonLog getInstance(){
		
		if(singletonInstance == null){
			singletonInstance = new ApMonLog();
		}
		
		return singletonInstance;
	}
	
	/**
	 * Log message.
	 *
	 * @param parameterName the parameter name
	 * @param parameterType the parameter type
	 * @param parameterValue the parameter value
	 */
	public void logMessage(String parameterName, Integer parameterType, Object parameterValue){
		
		if(apm != null){
			try{
				apm.sendParameter(LogMachineName, TestName, parameterName, parameterValue);
			}
			catch(Exception e){
				System.err.println("Exception in sending apm log message " + e.getMessage());
			}
		}
		else
		{
			System.err.println("The apm was not instantiated");
		}
	}

}

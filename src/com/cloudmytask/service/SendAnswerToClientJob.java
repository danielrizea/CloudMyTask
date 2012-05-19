package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class SendAnswerToClientJob implements Runnable{

	private CMTPrivateServiceInterface service;
	private Request answer;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Request> answerQueue;
	private MachineInfo machineInfo;
	
	public SendAnswerToClientJob(CMTPrivateServiceInterface service,Request answer, CallbackInterface ci, ConcurrentHashMap<String, Request> answerQueue, MachineInfo machineInfo){
		this.service = service;
		this.answer = answer;
		this.ci = ci;
		this.answerQueue = answerQueue;
		this.machineInfo = machineInfo;
	}
	
	public void run() {
		
		//System.out.println("[CMTServiceObject] send answer to client " + answer.requestID );
		
		answerQueue.put(answer.requestID, answer);
		machineInfo.writeToLogFile("ServiceObject " + machineInfo.id, " Send result to client of request " + answer.requestID);
		ci.sendResult(answer);
		
		
	}
}

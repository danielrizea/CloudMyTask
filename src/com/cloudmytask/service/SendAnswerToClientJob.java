package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class SendAnswerToClientJob implements Runnable{

	private CMTPrivateServiceInterface service;
	private Request answer;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Request> answerQueue;

	public SendAnswerToClientJob(CMTPrivateServiceInterface service,Request answer, CallbackInterface ci, ConcurrentHashMap<String, Request> answerQueue){
		this.service = service;
		this.answer = answer;
		this.ci = ci;
		this.answerQueue = answerQueue;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("[CMTServiceObject] send answer to client " + answer.requestID );
		answerQueue.put(answer.requestID, answer);
		ci.sendResult(answer);
		
		
	}
}

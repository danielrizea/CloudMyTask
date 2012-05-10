package com.cloudmytask.service;

public class MachineDescription {

	
	public int id;
	public int[] neighbours;
	
	public MachineDescription(int id, int[] neighbours){
		
		this.id = id;
		this.neighbours = neighbours;
	}
	
}

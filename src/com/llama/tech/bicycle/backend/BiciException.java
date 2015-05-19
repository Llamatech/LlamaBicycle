package com.llama.tech.bicycle.backend;

import java.io.Serializable;

public class BiciException extends Exception implements Serializable{
	
	public BiciException(String message){
		super(message);
	}

}

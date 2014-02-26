package com.aetherworks.example.jersey2.exception;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message){
		super(message);
	}
}

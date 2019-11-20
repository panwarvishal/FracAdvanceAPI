package com.frac.FracAdvanced.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable{
	
	private final String jwt;

	public String getJwt() {
		return jwt;
	}

	public AuthenticationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}
	
	

}

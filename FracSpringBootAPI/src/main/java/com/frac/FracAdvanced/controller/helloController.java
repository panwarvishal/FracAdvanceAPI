package com.frac.FracAdvanced.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frac.FracAdvanced.domain.security.JwtUtil;
import com.frac.FracAdvanced.domain.security.MyUserDetailService;
import com.frac.FracAdvanced.model.AuthenticationRequest;
import com.frac.FracAdvanced.model.AuthenticationResponse;
@CrossOrigin(origins = "http://localhost:4200")
@RestController 
public class helloController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailService userDetailService;
	
	@Autowired
	private JwtUtil jwtTokenUnit;

	@RequestMapping("/hello")
	public List<String> login12() {
		
		List<String> a1=new ArrayList<>();
		a1.add("This is spring boot part......vishal");
		return a1;
	} 

	@RequestMapping(value="/authenticate", method= RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
	{
		try {
			
			System.out.println("th is  authenticate....");
			authenticationManager.authenticate(
new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
		
			}
		catch(BadCredentialsException e) { throw new Exception("incorrect password or username",e);}
		final UserDetails	 userDetails= userDetailService.loadUserByUsername(authenticationRequest.getUsername());
	final String jwt= jwtTokenUnit.generateToken(userDetails);
	return ResponseEntity.ok(new AuthenticationResponse(jwt));
			
	}
}

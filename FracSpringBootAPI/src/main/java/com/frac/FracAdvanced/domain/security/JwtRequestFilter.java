package com.frac.FracAdvanced.domain.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain;


@Component
public class JwtRequestFilter extends OncePerRequestFilter{	
	@Autowired
	private MyUserDetailService userDetailService;	
	@Autowired
	private JwtUtil jwtTokenUnit;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {	
		System.out.println("this is filter");
		final String authorizationHeader=request.getHeader("Authorization");
		String username=null;		
		String jwt=null;
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
			{jwt=authorizationHeader.substring(7);
		username=jwtTokenUnit.extractUserName(jwt);
			}
if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null)
		
		{ UserDetails userDetails=this.userDetailService.loadUserByUsername(username);

	if(jwtTokenUnit.validateToken(jwt, userDetails))
	{
UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
	
usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
}}

filterChain.doFilter(request,response);	}}



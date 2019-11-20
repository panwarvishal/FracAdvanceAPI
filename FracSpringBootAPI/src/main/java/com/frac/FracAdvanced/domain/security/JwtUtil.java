package com.frac.FracAdvanced.domain.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.jpa.spi.CriteriaQueryTupleTransformer;
import org.hibernate.tool.schema.extract.spi.ExtractionContext;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.graphbuilder.math.func.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class JwtUtil {
	private String SECRET_KEY="secret";
	
	private ConfigurationService configurationService;
	public String extractUserName(String token)
	{
		
		return extractClaim(token,Claims::getSubject);
		
	}
	
	public Date extractExpiration(String token)
	{
		
		return extractClaim(token,Claims::getExpiration);
		
	}
	
	
	
	
	public <T> T extractClaim(String token, java.util.function.Function<Claims,T> claimsResolver)
	{
		
		final Claims claims= extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}
	
	private Claims extractAllClaims(String token)
	{
		
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token)
	{
		
		return extractExpiration(token).before(new Date());
	}
	
	
	public String generateToken(UserDetails userDetails)
	{		
		Map<String, Object> claims=new HashMap<>();
		
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String subject)
	{
		
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()*1000*60*60))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	
	public Boolean validateToken(String token, UserDetails userDetails)
	{
		final String username=extractUserName(token);
		return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	
	 /*public void addTokenToResponse(UserDetails account, HttpServletResponse res) {

	        LocalDateTime expiry = LocalDateTime.now().plusSeconds(ConfigurationService.getJwtExpirationInSec());

	        String token = Jwts.builder()
	                .setSubject(account.getUsername())
	                .setIssuedAt(new Date())
	                .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
	                .signWith(SignatureAlgorithm.HS512, configurationService.getJwtSecret())
	                .compact();
	        res.addHeader(configurationService.getHeaderField(), configurationService.getTokenPrefix() + token);
	    }*/

}

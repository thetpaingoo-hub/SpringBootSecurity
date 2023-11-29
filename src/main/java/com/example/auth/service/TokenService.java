package com.example.auth.service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.auth.dto.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	private String jwtkey="b5e50b39e2c74b8fb3dc47a089c19ea8b5e50b39e2c74b8fb3dc47a089c19ea8";
	public String getJwtToken() {
	
		String token="Bearer ";//JWTKeys.PREFIX.get();
		Key key=Keys.hmacShaKeyFor(jwtkey.getBytes());
		
		Map<String, Object> claims=new HashMap<String, Object>();
		claims.put("uuid",  UUID.randomUUID().toString().replace("-", ""));
		claims.put("id", "00001");
		claims.put("name", "Replace with user name");
		
		Date iat=Date.from(Instant.now());
		Date exp=Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		token+=Jwts.builder()
		.addClaims(claims)
		.setIssuedAt(iat)
		.setExpiration(exp)
		.signWith(key,SignatureAlgorithm.HS512).compact();
		return token;
	}
	
	public UserPrincipal parseJwtToken(String token) {
		Jws<Claims> jwtClaims=Jwts.parserBuilder()
				.setSigningKey(jwtkey.getBytes())
				.build()
				.parseClaimsJws(token);
		Map<String, Object> claims=jwtClaims.getBody();
		UserPrincipal userPrincipal=new UserPrincipal();
		userPrincipal.setSyskey(claims.get("id").toString());
		userPrincipal.setName(claims.get("name").toString());
		userPrincipal.setRoles(Arrays.asList(new String[] {"master"}));
		return userPrincipal;
	}
}

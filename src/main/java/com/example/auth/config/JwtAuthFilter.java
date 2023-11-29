package com.example.auth.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.auth.dto.UserPrincipal;
import com.example.auth.service.TokenService;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter{
	private final TokenService tokenService;

	public JwtAuthFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeaderIsInvalid(authorizationHeader)) {
				filterChain.doFilter(request, response);
				return;
			}
			UsernamePasswordAuthenticationToken token = createToken(authorizationHeader);

			SecurityContextHolder.getContext().setAuthentication(token);
			filterChain.doFilter(request, response);

		} catch (UnsupportedJwtException | MalformedJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		} catch (SignatureException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
			return;
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expired");
			return;
		}

	}
	
	private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
		return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");
	}

	private UsernamePasswordAuthenticationToken createToken(String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
		UserPrincipal userPrincipal = this.tokenService.parseJwtToken(token);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String role:userPrincipal.getRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
		}
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
	}
}

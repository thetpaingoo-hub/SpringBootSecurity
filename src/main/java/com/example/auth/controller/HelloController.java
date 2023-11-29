package com.example.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.service.TokenService;

@RestController
@RequestMapping("")
public class HelloController {
	@Autowired
	private TokenService tokenService;
	
	@GetMapping(path = "hello")
	public ResponseEntity<?> getHelloWorld() {
		return ResponseEntity.ok().body("hello");
	}
	
	@GetMapping(path = "token")
	public ResponseEntity<?> getToken() {
		
		return ResponseEntity.ok().body(this.tokenService.getJwtToken());
	}

}

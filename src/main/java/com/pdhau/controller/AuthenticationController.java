package com.pdhau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pdhau.config.JwtTokenUtil;
import com.pdhau.model.AuthToken;
import com.pdhau.model.LoginUser;
import com.pdhau.model.User;
import com.pdhau.service.UserService;

@RestController
@RequestMapping("/token")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ResponseEntity<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final User user = (User) userService.loadUserByUsername(loginUser.getUsername());
		if(user.isValid(loginUser.getPassword())){
			final String token = jwtTokenUtil.generateToken(user);
			return new ResponseEntity<AuthToken>(new AuthToken(token), HttpStatus.OK);
		}
		return new ResponseEntity<AuthToken>(HttpStatus.UNAUTHORIZED);
		
	}

}

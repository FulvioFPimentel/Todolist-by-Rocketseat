package com.fpimentel.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fpimentel.todolist.user.IUserRepository;
import com.fpimentel.todolist.user.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter implements Filter {

	@Autowired
	private IUserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String requestURL = request.getRequestURI();
		
		if (requestURL.contains("/tasks")) {
			var authorization = request.getHeader("Authorization");
			var authEncoded = authorization.substring("Basic".length()).trim();
			
			byte[] authDecode = Base64.getDecoder().decode(authEncoded);
			var authString = new String(authDecode);
			
			String[] credentials = authString.split(":");			
			
			UserModel user = this.userRepository.findByUsername(credentials[0].toString());
			if(user != null) {
				if(BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword()).verified) {
					request.setAttribute("idUser", user.getId());
					filterChain.doFilter(request, response);
				} else {
					response.sendError(401);
				}
				
			} else {
				response.sendError(401);
			}
	
		} else {
			filterChain.doFilter(request, response);
		}
			
	}

}

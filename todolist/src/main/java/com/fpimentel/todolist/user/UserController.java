package com.fpimentel.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController("/users")
public class UserController {
	
	@Autowired
	private IUserRepository userRepository;
	
	@PostMapping("/")
	public ResponseEntity<Object> creater(@RequestBody UserModel userModel) {
		var user = this.userRepository.findByUsername(userModel.getUsername());
		
		if(user != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
		}
		
		 var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
		 userModel.setPassword(passwordHashred);
		 
		 var userCreated = this.userRepository.save(userModel);
		 return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserModel> findByName(@PathVariable String username) {
		UserModel user = this.userRepository.findByUsername(username);
		return ResponseEntity.status(200).body(user);
	}
	
}

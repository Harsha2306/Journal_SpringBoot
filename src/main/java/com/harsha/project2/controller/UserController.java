package com.harsha.project2.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsha.project2.ErrorResponse;
import com.harsha.project2.entity.User;
import com.harsha.project2.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
	}

//	@GetMapping("/{userId}")
//	public ResponseEntity<?> getUserById(@PathVariable ObjectId userId) {
//		try {
//			Optional<User> user = userService.getUserById(userId);
//			return user.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(user.get())
//					: ResponseEntity.status(HttpStatus.NOT_FOUND)
//							.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
//		}
//	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User savedUser = userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
		try {
			User user = userService.getUserByName(updatedUser.getUserName());
			if (user != null)
				System.out.println(user.toString());
			if (user != null) {
				if (updatedUser.getPassword() != null && updatedUser.getPassword().trim().length() != 0)
					user.setPassword(updatedUser.getPassword());
				return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(user));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

}

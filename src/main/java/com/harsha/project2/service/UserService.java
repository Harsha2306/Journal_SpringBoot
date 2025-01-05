package com.harsha.project2.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.harsha.project2.entity.Journal;
import com.harsha.project2.entity.User;
import com.harsha.project2.repository.UserRepository;

@Service
public class UserService {

	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(ObjectId userId) {
		return userRepository.findById(userId);
	}

	public User getUserByName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public void deleteUserById(ObjectId userId) {
		userRepository.deleteById(userId);
	}
	
	public User saveUser(User user){
		return userRepository.save(user);
	}

}

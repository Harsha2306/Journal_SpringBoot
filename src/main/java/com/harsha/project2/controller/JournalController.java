package com.harsha.project2.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harsha.project2.ErrorResponse;
import com.harsha.project2.entity.Journal;
import com.harsha.project2.entity.User;
import com.harsha.project2.service.UserService;

@RestController
@RequestMapping("/journal")
public class JournalController {

	private UserService userService;

	public JournalController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("{userId}")
	public ResponseEntity<?> getAllJounalsByUserId(@PathVariable ObjectId userId) {
		try {
			Optional<User> optionalUser = userService.getUserById(userId);
			if (optionalUser.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get().getJournals());
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@PostMapping("{userId}")
	public ResponseEntity<?> createJournal(@RequestBody Journal journal, @PathVariable ObjectId userId) {
		try {
			Optional<User> optionlUser = userService.getUserById(userId);
			if (optionlUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
			}

			journal.setDate(LocalDateTime.now());
			journal.setId(new ObjectId());

			User user = optionlUser.get();
			user.getJournals().add(journal);
			userService.saveUser(user);

			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@GetMapping("/{userId}/{journalId}")
	public ResponseEntity<?> getJournalById(@PathVariable ObjectId userId, @PathVariable ObjectId journalId) {
		try {
			Optional<User> optionalUser = userService.getUserById(userId);
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
			}

			User user = optionalUser.get();
			List<Journal> journals = user.getJournals();
			Optional<Journal> requiredJournal = journals.stream().filter(journal -> journal.getId().equals(journalId))
					.findFirst();

			return requiredJournal.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(requiredJournal.get())
					: ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "journal not found"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@DeleteMapping("/{userId}/{journalId}")
	public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId userId, @PathVariable ObjectId journalId) {
		try {
			Optional<User> optionalUser = userService.getUserById(userId);
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
			}

			User user = optionalUser.get();
			boolean isRemoved = user.getJournals().removeIf(journal -> journal.getId().equals(journalId));
			if (!isRemoved)
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "journal not found"));

			userService.saveUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@PutMapping("/{userId}/{journalId}")
	public ResponseEntity<?> updateJournalById(@PathVariable ObjectId userId, @PathVariable ObjectId journalId,
			@RequestBody Journal updatdJournal) {
		try {
			Optional<User> optionalUser = userService.getUserById(userId);
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "user not found"));
			}

			User user = optionalUser.get();
			List<Journal> journals = user.getJournals();
			Optional<Journal> optionalJournal = journals.stream().filter(journal -> journal.getId().equals(journalId))
					.findFirst();
			if (optionalJournal.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "journal not found"));

			Journal journal = optionalJournal.get();
			if (updatdJournal.getTitle() != null && updatdJournal.getTitle().trim().length() != 0)
				journal.setTitle(updatdJournal.getTitle());
			if (updatdJournal.getContent() != null && updatdJournal.getContent().trim().length() != 0)
				journal.setContent(updatdJournal.getContent());
			userService.saveUser(user);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

}

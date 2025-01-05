package com.harsha.project2.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.harsha.project2.service.JournalService;

@RestController
@RequestMapping("/journal")
public class JournalController {

	private JournalService journalService;

	public JournalController(JournalService journalService) {
		this.journalService = journalService;
	}

	@GetMapping
	public ResponseEntity<List<Journal>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(journalService.getAll());
	}

	@PostMapping
	public ResponseEntity<?> createJournal(@RequestBody Journal journal) {
		try {
			journal.setDate(LocalDateTime.now());
			Journal savedJournal = journalService.createJournal(journal);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedJournal);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@GetMapping("/{journalId}")
	public ResponseEntity<?> getJournalById(@PathVariable ObjectId journalId) {
		try {
			Optional<Journal> journal = journalService.getJournalById(journalId);
			return journal.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(journal.get())
					: ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "not found"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@DeleteMapping("/{journalId}")
	public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId journalId) {
		try {
			journalService.deleteJournalById(journalId);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
		}
	}

	@PutMapping("/{journalId}")
	public ResponseEntity<?> updateJournalById(@PathVariable ObjectId journalId, @RequestBody Journal updatdJournal) {
		Optional<Journal> journal = journalService.getJournalById(journalId);
		if (journal.isPresent()) {
			if (updatdJournal.getTitle() != null && updatdJournal.getTitle().trim().length() != 0)
				journal.get().setTitle(updatdJournal.getTitle());
			if (updatdJournal.getContent() != null && updatdJournal.getContent().trim().length() != 0)
				journal.get().setContent(updatdJournal.getContent());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(journalService.createJournal(journal.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "not found"));
	}

}

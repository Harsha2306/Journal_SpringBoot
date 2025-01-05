package com.harsha.project2.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.harsha.project2.entity.Journal;
import com.harsha.project2.repository.JournalRepository;

@Service
public class JournalService {
	JournalRepository journalRepository;

	public JournalService(JournalRepository journalRepository) {
		this.journalRepository = journalRepository;
	}

	public Journal createJournal(Journal journal) {
		return journalRepository.save(journal);
	}

	public List<Journal> getAll() {
		return journalRepository.findAll();
	}

	public Optional<Journal> getJournalById(ObjectId journalId) {
		return journalRepository.findById(journalId);
	}

	public void deleteJournalById(ObjectId journalId) {
		journalRepository.deleteById(journalId);
	}

}

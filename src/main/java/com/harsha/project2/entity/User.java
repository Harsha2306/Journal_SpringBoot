package com.harsha.project2.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Document
@Data
public class User {

	@Id
	private ObjectId id;
	@NonNull
	private String password;
	@Indexed(unique = true)
	@NonNull
	private String userName;
	private List<Journal> journals = new ArrayList<Journal>();
}

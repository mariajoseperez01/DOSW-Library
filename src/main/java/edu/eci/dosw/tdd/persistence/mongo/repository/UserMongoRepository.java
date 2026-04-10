package edu.eci.dosw.tdd.persistence.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

	Optional<UserDocument> findByName(String name);
}
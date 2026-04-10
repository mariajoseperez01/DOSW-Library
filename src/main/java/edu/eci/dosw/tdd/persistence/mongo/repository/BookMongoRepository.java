package edu.eci.dosw.tdd.persistence.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;

public interface BookMongoRepository extends MongoRepository<BookDocument, String> {
}
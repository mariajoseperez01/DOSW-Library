package edu.eci.dosw.tdd.persistence.nonrelational;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.tdd.persistence.mongo.repository.UserMongoRepository;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Repository
@Profile("mongo")
public class MongoUserRepositoryAdapter implements UserRepositoryPort {

	private final UserMongoRepository userMongoRepository;

	public MongoUserRepositoryAdapter(UserMongoRepository userMongoRepository) {
		this.userMongoRepository = userMongoRepository;
	}

	@Override
	public User save(User entity) {
		return toModel(userMongoRepository.save(toDocument(entity)));
	}

	@Override
	public Optional<User> findById(String id) {
		return userMongoRepository.findById(id).map(this::toModel);
	}

	@Override
	public List<User> findAll() {
		return userMongoRepository.findAll().stream().map(this::toModel).toList();
	}

	@Override
	public void deleteAll() {
		userMongoRepository.deleteAll();
	}

	@Override
	public Optional<User> findByName(String name) {
		return userMongoRepository.findByName(name).map(this::toModel);
	}

	private UserDocument toDocument(User user) {
		UserDocument document = new UserDocument();
		document.setId(user.getId());
		document.setName(user.getName());
		document.setPassword(user.getPassword());
		document.setRole(user.getRole());
		return document;
	}

	private User toModel(UserDocument document) {
		User user = new User();
		user.setId(document.getId());
		user.setName(document.getName());
		user.setPassword(document.getPassword());
		user.setRole(document.getRole());
		return user;
	}
}
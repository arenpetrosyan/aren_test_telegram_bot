package com.aren.ArenDemoBot.repositories;

import com.aren.ArenDemoBot.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// Repository for User entity
@Repository
public interface UserRepository extends MongoRepository<User, Long> {
}

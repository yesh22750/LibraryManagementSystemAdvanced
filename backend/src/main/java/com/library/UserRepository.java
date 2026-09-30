package com.library;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByFullNameContainingIgnoreCase(String q);

    List<User> findByEmail(String email);
}
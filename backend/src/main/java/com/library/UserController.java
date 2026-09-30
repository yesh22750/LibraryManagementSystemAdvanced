package com.library;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;

    @NotBlank
    @Column(name = "full_name")
    String fullName;

    @NotBlank
    @Email
    String email;

    String phone;

    @Column(name = "membership_date")
    LocalDate membershipDate;

    @Enumerated(EnumType.STRING)
    Status status;

    public Integer getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public Status getStatus() {
        return status;
    }

    enum Status {
        ACTIVE,
        INACTIVE
    }
}

@RestController
@RequestMapping("/api/users")
@CrossOrigin
class UserController {

    private final UserRepository repo;

    UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    List<User> all(
            @RequestParam(required = false) String q) {

        return q == null
                ? repo.findAll()
                : repo.findByFullNameContainingIgnoreCase(q);
    }

    @GetMapping("/{id}")
    User one(@PathVariable Integer id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new java.util.NoSuchElementException(
                                "User not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User create(@Valid @RequestBody User user) {

        return repo.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Integer id) {

        repo.deleteById(id);
    }
}
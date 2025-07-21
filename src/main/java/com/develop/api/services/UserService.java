package com.develop.api.services;

import com.develop.api.model.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> findByUsername(String username);
}
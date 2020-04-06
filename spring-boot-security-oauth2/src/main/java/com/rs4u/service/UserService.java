package com.rs4u.service;

import java.util.List;

import com.rs4u.model.User;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
}

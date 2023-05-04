package com.aist.cinema.service;

import com.aist.cinema.dto.user.UserResponse;
import com.aist.cinema.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}

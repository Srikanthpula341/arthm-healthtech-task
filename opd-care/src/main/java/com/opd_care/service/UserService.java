package com.opd_care.service;

import com.opd_care.dto.UserDTO;
import com.opd_care.model.UserRole;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByRole(UserRole role);
    UserDTO getUserById(String id);
    UserDTO login(String username, String password);
}

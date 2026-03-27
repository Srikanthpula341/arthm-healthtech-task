package com.opd_care.service.impl;

import com.opd_care.constant.AppConstants;
import com.opd_care.constant.ErrorConstants;
import com.opd_care.dto.UserDTO;
import com.opd_care.exception.ResourceNotFoundException;
import com.opd_care.exception.UnauthorizedException;
import com.opd_care.exception.ValidationException;
import com.opd_care.model.User;
import com.opd_care.model.UserRole;
import com.opd_care.repository.UserRepository;
import com.opd_care.service.UserService;
import com.opd_care.util.MappingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException("Username already exists");
        }

        User user = MappingUtil.toUserEntity(dto);
        user.setUserId(AppConstants.USER_ID_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        User saved = userRepository.save(user);
        return MappingUtil.toUserDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(MappingUtil::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(MappingUtil::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return MappingUtil.toUserDTO(user);
    }

    @Override
    public UserDTO login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(MappingUtil::toUserDTO)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
    }
}

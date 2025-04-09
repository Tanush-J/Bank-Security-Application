package com.security.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.bank.dto.UserDto;
import com.security.bank.entity.Role;
import com.security.bank.entity.User;
import com.security.bank.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public void createUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        user.setAddress(userDto.getAddress());
        user.setName(userDto.getName());
        user.setNumber(userDto.getNumber());
        user.setUsername(userDto.getUsername());
        user.setPassword(encodedPassword);
        user.setIdentityProof(userDto.getIdentityProof());
        Role role = new Role();
        role.setRoleName("ROLE_CUSTOMER");
        user.setRoles(role);
        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) {
            throw new RuntimeException("user not found with given id!");
        }
        return user.get(); 
    }
}

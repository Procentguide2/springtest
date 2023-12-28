package com.example.springtest.service;

import com.example.springtest.exception.UserExistException;
import com.example.springtest.form.RegisterForm;
import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository sysUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    public void saveUserFromForm(RegisterForm form) throws UserExistException {
        Optional<User> foundUser = Optional.ofNullable(sysUserRepository.findByUsername(form.getUsername()));
        if (foundUser.isPresent()){
            throw new UserExistException("There is an account with that email username: " + form.getUsername());
        }

        User user = new User(form.getUsername(),passwordEncoder.encode(form.getPassword()));
        sysUserRepository.save(user);

    }
}

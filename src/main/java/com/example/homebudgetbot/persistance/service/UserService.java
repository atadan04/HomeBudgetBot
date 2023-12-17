package com.example.homebudgetbot.persistance.service;

import com.example.homebudgetbot.persistance.entity.User;
import com.example.homebudgetbot.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public User save(User user){
        return userRepository.save(user);
    }
    public void deleteById(UUID id){
        userRepository.deleteById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public Optional<User> findById(UUID id){
        return userRepository.findById(id);
    }
}

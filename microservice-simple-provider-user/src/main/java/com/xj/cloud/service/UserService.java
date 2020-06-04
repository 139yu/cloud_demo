package com.xj.cloud.service;

import com.xj.cloud.dao.UserRepository;
import com.xj.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getItem(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }

}

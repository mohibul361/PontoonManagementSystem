package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Users;
import com.biwta.pontoon.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WrongPasswordService {
	
    public final UserRepository userRepository = null;

    public void wrongPasswordAttentAction(String email) {
        Optional<Users> optionalUsers = userRepository.findOneByEmailIgnoreCase(email);
        
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            Integer count = users.getWrongAttemptCount() == null ? 0 : users.getWrongAttemptCount();
            users.setWrongAttemptCount(count + 1);
            userRepository.save(users);
        }
    }
    
}

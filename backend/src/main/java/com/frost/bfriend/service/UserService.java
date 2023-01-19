package com.frost.bfriend.service;

import com.frost.bfriend.dto.UserDto;
import com.frost.bfriend.dto.UserDto.CreateRequest;
import com.frost.bfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(CreateRequest createRequest) {
        if(!userRepository.existsByEmail(createRequest.getEmail())) {
            throw new
        }
    }
}

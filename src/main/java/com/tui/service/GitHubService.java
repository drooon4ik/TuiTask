package com.tui.service;

import com.tui.entity.user.UserRepository;
import reactor.core.publisher.Flux;

public interface GitHubService {

    Flux<UserRepository> getUserNotForkRepositories(String userLogin);

    class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}

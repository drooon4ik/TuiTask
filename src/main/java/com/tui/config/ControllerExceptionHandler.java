package com.tui.config;

import com.tui.controller.user.repositories.entity.ApiError;
import com.tui.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(GitHubService.UserNotFoundException.class)
    public ResponseEntity<Object> handleContentNotAllowedException(GitHubService.UserNotFoundException ex) {

        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
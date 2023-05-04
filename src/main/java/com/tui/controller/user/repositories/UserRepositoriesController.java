package com.tui.controller.user.repositories;

import com.tui.controller.user.repositories.entity.UserRepositoryView;
import com.tui.service.GitHubService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users/{username}/repos/")
public class UserRepositoriesController {

    private final GitHubService gitHubService;

    public UserRepositoriesController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UserRepositoryView> getUserRepositories(@PathVariable String username) {
        return gitHubService.getUserNotForkRepositories(username)
                .map(UserRepositoryView::from);
    }
}

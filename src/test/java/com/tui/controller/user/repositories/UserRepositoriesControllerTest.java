package com.tui.controller.user.repositories;

import com.tui.controller.user.repositories.entity.UserRepositoryView;
import com.tui.entity.user.UserRepository;
import com.tui.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserRepositoriesControllerTest {

    @InjectMocks
    private UserRepositoriesController target;

    @Mock
    private GitHubService gitHubService;

    @Test
    void shouldGetUserRepositories() {
        // given
        String username = "user";
        UserRepository repo1 = new UserRepository("repo1", "owner1", List.of(new UserRepository.Branch("branch1", "branch1sha")));
        UserRepository repo2 = new UserRepository("repo2", "owner2", List.of(new UserRepository.Branch("branch2", "branch2sha")));
        Flux<UserRepository> gitHubServiceResult = Flux.just(repo1, repo2);
        Mockito.when(gitHubService.getUserNotForkRepositories(username)).thenReturn(gitHubServiceResult);

        // when
        Flux<UserRepositoryView> resultPublisher = target.getUserRepositories(username);

        // then
        StepVerifier.create(resultPublisher)
                .expectNext(UserRepositoryView.from(repo1))
                .expectNext(UserRepositoryView.from(repo2))
                .verifyComplete();
    }

    @Test
    void shouldGetEmptyUserRepositories() {
        // given
        String username = "user";
        Flux<UserRepository> gitHubServiceResult = Flux.empty();
        Mockito.when(gitHubService.getUserNotForkRepositories(username)).thenReturn(gitHubServiceResult);

        // when
        Flux<UserRepositoryView> resultPublisher = target.getUserRepositories(username);

        // then
        StepVerifier.create(resultPublisher).verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenGitHubServiceReturnsError() {
        // given
        String username = "user";
        Mockito.when(gitHubService.getUserNotForkRepositories(username)).thenReturn(Flux.error(new RuntimeException()));

        // when
        Flux<UserRepositoryView> resultPublisher = target.getUserRepositories(username);

        // then
        StepVerifier.create(resultPublisher).verifyError(RuntimeException.class);
    }
}
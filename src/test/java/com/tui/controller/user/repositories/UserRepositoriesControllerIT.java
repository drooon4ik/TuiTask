package com.tui.controller.user.repositories;

import com.tui.BaseIntegrationTest;
import com.tui.controller.user.repositories.entity.ApiError;
import com.tui.controller.user.repositories.entity.UserRepositoryView;
import com.tui.entity.user.UserRepository;
import com.tui.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.util.List;

// todo split IT and unit tests
class UserRepositoriesControllerIT extends BaseIntegrationTest {

    @MockBean
    private GitHubService gitHubService;

    @Test
    void shouldReturnUserRepos() {
        // given
        String username = "user";
        UserRepository repo1 = new UserRepository("repo1", "owner1", List.of(new UserRepository.Branch("branch1", "branch1sha")));
        UserRepository repo2 = new UserRepository("repo2", "owner2", List.of(new UserRepository.Branch("branch2", "branch2sha")));
        Flux<UserRepository> gitHubServiceResult = Flux.just(repo1, repo2);
        Mockito.when(gitHubService.getUserNotForkRepositories(username)).thenReturn(gitHubServiceResult);

        // when then
        client.get().uri("/users/{user}/repos/", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserRepositoryView.class).hasSize(2).contains(
                        UserRepositoryView.from(repo1), UserRepositoryView.from(repo2)
                );
    }

    @Test
    void shouldReturn404WhenThereAreNoUserRepos() {
        // given
        String username = "user";
        Mockito.when(gitHubService.getUserNotForkRepositories(username)).thenReturn(Flux.error(new GitHubService.UserNotFoundException(String.format("User %s does not exist", username))));

        // when then
        client.get().uri("/users/{user}/repos/", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApiError.class).isEqualTo(
                        new ApiError(HttpStatus.NOT_FOUND.value(), String.format("User %s does not exist", username))
                );
    }
}
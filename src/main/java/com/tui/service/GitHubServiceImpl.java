package com.tui.service;

import com.tui.entity.user.UserRepository;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {

    private final GitHub github;

    public GitHubServiceImpl(GitHub github) {
        this.github = github;
    }

    @Override
    public Flux<UserRepository> getUserNotForkRepositories(String userLogin) {
        return getRepositories(userLogin)
                .filter(it -> !it.isFork())
                .map(repo -> new UserRepository(repo.getName(), repo.getOwnerName(), getBranches(repo)));
    }

    private Flux<GHRepository> getRepositories(String userLogin) {
        return Mono.fromCallable(() -> github.getUser(userLogin))
                .flatMapIterable(GitHubServiceImpl::getUserRepos)
                .onErrorMap(error -> {
                    if (error instanceof FileNotFoundException) //todo add proper handling
                        return new UserNotFoundException(String.format("User %s does not exist", userLogin));
                    return error;
                });
    }

    private static Collection<GHRepository> getUserRepos(GHUser it) {
        try {
            return it.getRepositories().values();
        } catch (IOException e) {
            // log
            throw new RuntimeException(e);
        }
    }

    private List<UserRepository.Branch> getBranches(GHRepository repo) {
        try {
            return repo.getBranches().values().stream()
                    .map((branch) -> new UserRepository.Branch(branch.getName(), branch.getSHA1()))
                    .toList();
        } catch (IOException e) {
            //log
            throw new RuntimeException(e);
        }
    }
}

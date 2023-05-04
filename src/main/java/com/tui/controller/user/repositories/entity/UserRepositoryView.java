package com.tui.controller.user.repositories.entity;

import com.tui.entity.user.UserRepository;

import java.util.List;

public record UserRepositoryView(String name, String owner, List<Branch> branches) {
    public record Branch(String name, String sha) {
        public static Branch from(UserRepository.Branch branch) {
            return new Branch(branch.name(), branch.sha());
        }
    }

    public static UserRepositoryView from(UserRepository repo) {
        return new UserRepositoryView(repo.name(), repo.owner(), repo.branches().stream().map(Branch::from).toList());
    }
}

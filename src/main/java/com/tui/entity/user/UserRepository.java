package com.tui.entity.user;

import java.util.List;

public record UserRepository(String name, String owner, List<Branch> branches) {
    public record Branch(String name, String sha) {
    }
}

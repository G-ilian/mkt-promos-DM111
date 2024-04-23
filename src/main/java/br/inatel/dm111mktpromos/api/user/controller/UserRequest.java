package br.inatel.dm111mktpromos.api.user.controller;

public record UserRequest(String name,
                          String email,
                          String password,
                          String role) {
}

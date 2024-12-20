package ru.eltech.server.service;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AuthManager {

    private static final String LOGIN = "1";
    private static final String PASSWORD = "1";

    private boolean loggedIn = false;

    public boolean login(String login, String password) {
        loggedIn = LOGIN.equals(login) && PASSWORD.equals(password);
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

}

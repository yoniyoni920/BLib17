package controllers;

import entities.User;

/**
 * This class is mostly a singleton holder for the currently connected user
 */
public class Auth {
    private User user;
    private static Auth instance;

    private Auth() {

    }

    /**
     * @return A singleton instance of the class
     */
    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

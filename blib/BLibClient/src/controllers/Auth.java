package controllers;

import entities.Subscriber;
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

    /**
     * @return The currently authenticated subscriber, if is one.
     */
    public Subscriber getSubscriber() {
        if (user instanceof Subscriber) {
            return (Subscriber)user;
        }
        return null;
    }

    /**
     * @return The currently authenticated user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The currently authenticated user
     */
    public void setUser(User user) {
        this.user = user;
    }
}

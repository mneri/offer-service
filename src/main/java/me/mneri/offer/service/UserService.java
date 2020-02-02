package me.mneri.offer.service;

import me.mneri.offer.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Find all enabled {@link User}s.
     *
     * @return The list of all enabled users.
     */
    List<User> findAllEnabled();

    /**
     * Find the user with the specified id in the repository.
     *
     * @param id The id.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    Optional<User> findEnabledById(String id);

    /**
     * Find the user with the specified username in the repository.
     *
     * @param username The username.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    Optional<User> findEnabledByUsername(String username);

    /**
     * Persist a user into the database.
     *
     * @param user The user.
     */
    void save(User user);
}

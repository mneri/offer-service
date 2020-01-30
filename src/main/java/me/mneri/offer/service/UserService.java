package me.mneri.offer.service;

import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.specification.UserSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the user repository.
 *
 * @author mneri
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean existsEnabledById(String id) {
        return userRepository.count(where(isEnabled()).and(idIsEqualTo(id))) == 1;
    }

    /**
     * Find all enabled {@link User}s.
     *
     * @return The list of all enabled users.
     */
    public List<User> findEnabled() {
        return userRepository.findAll(where(isEnabled()));
    }

    /**
     * Find the user with the specified id in the repository.
     *
     * @param id The id.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    public Optional<User> findEnabledById(String id) {
        return userRepository.findOne(where(isEnabled()).and(idIsEqualTo(id)));
    }

    /**
     * Find the user with the specified username in the repository.
     *
     * @param username The username.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    public Optional<User> findEnabledByUsername(String username) {
        return userRepository.findOne(where(isEnabled()).and(usernameIsEqualTo(username)));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}

package me.mneri.offer.service;

import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static me.mneri.offer.specification.UserSpecification.isEnabled;
import static me.mneri.offer.specification.UserSpecification.usernameIsEqualTo;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Find the user with the specified username in the repository.
     *
     * @param username The username.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findOne(where(isEnabled()).and(usernameIsEqualTo(username)));
    }
}

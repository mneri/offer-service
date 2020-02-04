package me.mneri.offer.service.impl;

import lombok.extern.log4j.Log4j2;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.UserService;
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
@Log4j2
@Service
public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    public List<User> findAllEnabled() {
        return userRepository.findAll(where(userIsEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> findEnabledById(String id) {
        return userRepository.findOne(where(userIsEnabled()).and(userIdIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> findEnabledByUsername(String username) {
        return userRepository.findOne(where(userIsEnabled()).and(userUsernameIsEqualTo(username)));
    }

    /**
     * {@inheritDoc}
     */
    public void save(User user) {
        userRepository.save(user);
        log.debug("User created; userId: {}", user.getId());
    }
}

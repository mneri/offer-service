package me.mneri.offer.repository;

import me.mneri.offer.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for {@link User} beans.
 *
 * @author mneri
 */
@Repository
public interface UserRepository extends CrudRepository<User, String>, JpaSpecificationExecutor<User> {
}

package me.mneri.offer.data.entity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

/**
 * Default implementation of {@link EntityFactory}.
 *
 * @author Massimo Neri
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class EntityFactoryImpl implements EntityFactory {
    private final Clock clock;

    private final UUIDProvider uuidProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Offer createOffer() {
        Offer offer = new Offer();
        offer.setId(uuidProvider.createRandomUuid());
        offer.setCreateTime(new Date(clock.millis()));
        return offer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser() {
        User user = new User();
        user.setId(uuidProvider.createRandomUuid());
        user.setEnabled(true);
        return user;
    }
}

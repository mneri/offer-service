/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.offer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.mneri.offer.data.entity.EntityFactory;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.repository.OfferRepository;
import me.mneri.offer.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Date;

/**
 * {@link CommandLineRunner} that initializes the demo data into the database.
 *
 * @author Massimo Neri
 */
//@Component
//@Profile("!test") // Most of the tests rely on an empty initial database, we exclude this class from the test profile.
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfferCommandLineRunner implements CommandLineRunner {
    private final Clock clock;

    private final EntityFactory entityFactory;

    private final UserRepository userRepository;

    private final OfferRepository offerRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Return a 30 days forward date.
     *
     * @return A new {@link Date} object, 30 days into the future.
     */
    private long nextMonth() {
        return 30 * 24 * 60 * 60 * 1000L;
    }

    @Override
    @SneakyThrows
    public void run(String... args) {
        User mneri = entityFactory.createUser();
        mneri.setUsername("mneri");
        mneri.setEncodedPassword("secret", passwordEncoder);

        User jkacz = entityFactory.createUser();
        jkacz.setUsername("jkacz");
        jkacz.setEncodedPassword("secret", passwordEncoder);

        userRepository.save(mneri);
        userRepository.save(jkacz);

        Offer freeCoffee = entityFactory.createOffer();
        freeCoffee.setTitle("Buy one coffee, get one free");
        freeCoffee.setDescription("Come to our amazing shop and get one coffee for free!");
        freeCoffee.setPrice(new BigDecimal("2.00"));
        freeCoffee.setCurrency("GBP");
        freeCoffee.setTtl(nextMonth(), clock);
        freeCoffee.setPublisher(mneri);

        Offer freeChocolate = entityFactory.createOffer();
        freeChocolate.setTitle("Buy one chocolate, get one free");
        freeChocolate.setDescription("Come to our amazing shop and get one chocolate for free!");
        freeChocolate.setPrice(new BigDecimal("2.50"));
        freeChocolate.setCurrency("GBP");
        freeChocolate.setTtl(nextMonth(), clock);
        freeChocolate.setPublisher(jkacz);

        offerRepository.save(freeCoffee);
        offerRepository.save(freeChocolate);
    }
}

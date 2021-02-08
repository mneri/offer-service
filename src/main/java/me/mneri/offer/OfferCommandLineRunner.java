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

import me.mneri.offer.bean.OfferCreate;
import me.mneri.offer.entity.User;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * {@link CommandLineRunner} that initializes the demo data into the database.
 *
 * @author mneri
 */
@Component
@Profile("!test") // Most of the tests rely on an empty initial database, we exclude this class from the test profile.
public class OfferCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Return a 30 days forward date.
     *
     * @return A new {@link Date} object, 30 days into the future.
     */
    private long nextMonth() {
        return 30 * 24 * 60 * 60 * 1000L;
    }

    @Override
    public void run(String... args) {
        User mneri = new User("mneri", "secret", passwordEncoder);
        User jkaczmarczyk = new User("jkaczmarczyk", "secret", passwordEncoder);

        userService.save(mneri);
        userService.save(jkaczmarczyk);

        OfferCreate freeCoffee = new OfferCreate();
        freeCoffee.setTitle("Buy one coffee, get one free");
        freeCoffee.setDescription("Come to our amazing shop and get one coffee for free!");
        freeCoffee.setPrice(new BigDecimal("2.00"));
        freeCoffee.setCurrency("GBP");
        freeCoffee.setTtl(nextMonth());

        OfferCreate freeChocolate = new OfferCreate();
        freeChocolate.setTitle("Buy one chocolate, get one free");
        freeChocolate.setDescription("Come to our amazing shop and get one chocolate for free!");
        freeChocolate.setPrice(new BigDecimal("2.50"));
        freeChocolate.setCurrency("GBP");
        freeChocolate.setTtl(nextMonth());

        offerService.save(freeCoffee, mneri);
        offerService.save(freeChocolate, jkaczmarczyk);
    }
}

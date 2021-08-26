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

import lombok.SneakyThrows;
import me.mneri.offer.business.pojo.OfferCreate;
import me.mneri.offer.business.pojo.UserCreate;
import me.mneri.offer.business.service.OfferService;
import me.mneri.offer.business.service.UserService;
import me.mneri.offer.data.entity.User;
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
 * @author Massimo Neri
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
    @SneakyThrows
    public void run(String... args) {
        UserCreate mneriCreate = new UserCreate();
        mneriCreate.setUsername("mneri");
        mneriCreate.setPassword("secret");

        UserCreate jkaczmarczykCreate = new UserCreate();
        jkaczmarczykCreate.setUsername("jkaczmarczyk");
        jkaczmarczykCreate.setPassword("secret");

        User mneri = userService.save(mneriCreate);
        User jkaczmarczyk = userService.save(jkaczmarczykCreate);

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

        offerService.save(freeCoffee, mneri.getId());
        offerService.save(freeChocolate, jkaczmarczyk.getId());
    }
}

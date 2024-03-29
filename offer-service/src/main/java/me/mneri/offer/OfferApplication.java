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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.time.Clock;

/**
 * Main class and starting point of the application.
 *
 * @author Massimo Neri
 */
@EnableEurekaClient
@SpringBootApplication
public class OfferApplication {
    /**
     * Application {@link AnonymousAuthenticationFilter}.
     *
     * @return The anonymous authentication filter.
     */
    @Bean
    protected AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new AnonymousAuthenticationFilter(AnonymousAuthenticationFilter.class.getCanonicalName());
    }

    /**
     * Application {@link Clock}.
     *
     * @return The clock.
     */
    @Bean
    protected Clock clock() {
        return Clock.systemDefaultZone();
    }

    /**
     * Application {@link PasswordEncoder}.
     *
     * @return The password encoder.
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Application entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String... args) {
        SpringApplication.run(OfferApplication.class, args);
    }
}

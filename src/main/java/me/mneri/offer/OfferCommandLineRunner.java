package me.mneri.offer;

import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Component
public class OfferCommandLineRunner implements CommandLineRunner {
    @Autowired
    private OfferService offerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        User mneri = new User("mneri", "secret", passwordEncoder);
        Offer offer = Offer.builder()
                .title("Bazinga")
                .description("Awesome")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .end(new Date(currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L))
                .publisher(mneri)
                .build();

        userService.save(mneri);
        offerService.save(offer);
    }
}

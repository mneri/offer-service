package me.mneri.offer;

import me.mneri.offer.entity.Offer;
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

        Offer freeCoffee = Offer.builder()
                .title("Buy one coffee, get one free")
                .description("Come to our amazing shop and get one coffee for free!")
                .price(new BigDecimal("2.00"))
                .currency("GBP")
                .publisher(mneri)
                .ttl(nextMonth())
                .build();
        Offer freeChocolate = Offer.builder()
                .title("Buy one chocolate, get one free")
                .description("Come to our amazing shop and get one chocolate for free!")
                .price(new BigDecimal("2.50"))
                .currency("GBP")
                .publisher(jkaczmarczyk)
                .ttl(nextMonth())
                .build();

        offerService.save(freeCoffee);
        offerService.save(freeChocolate);
    }
}

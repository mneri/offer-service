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

@Component
@Profile("!test")
public class OfferCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Date nextMonth() {
        return new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);
    }

    @Override
    public void run(String... args) throws Exception {
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
                .end(nextMonth())
                .build();
        Offer freeChocolate = Offer.builder()
                .title("Buy one chocolate, get one free")
                .description("Come to our amazing shop and get one chocolate for free!")
                .price(new BigDecimal("2.50"))
                .currency("GBP")
                .publisher(jkaczmarczyk)
                .end(nextMonth())
                .build();

        offerService.save(freeCoffee);
        offerService.save(freeChocolate);
    }
}

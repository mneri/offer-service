package me.mneri.offer.controller;

import lombok.NoArgsConstructor;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;

import java.math.BigDecimal;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class ControllerTestUtil {
    static Offer createTestOffer(User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Amazing")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .publisher(publisher)
                .end(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L))
                .build();
    }
}

package me.mneri.offer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.validator.Constants;
import me.mneri.offer.validator.Description;
import me.mneri.offer.validator.Title;

import java.math.BigDecimal;

/**
 * DTO for a user's request to create a new {@link Offer}.
 *
 * @author mneri
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class OfferRequest {
    @NonNull
    @Schema(description = "Offer's title.",
            example = "Buy 1 get 1 for free.",
            maxLength = Constants.TITLE_MAX_LENGTH,
            minLength = Constants.TITLE_MIN_LENGTH,
            required = true)
    @Title
    private String title;

    @NonNull
    @Schema(description = "Offer's description.",
            example = "Order 1 bag of coffee and get one free.",
            maxLength = Constants.DESCRIPTION_MAX_LENGTH,
            minLength = Constants.DESCRIPTION_MIN_LENGTH,
            required = true)
    @Description
    private String description;

    @NonNull
    @Schema(description = "Offer's price.",
            example = "100.00",
            required = true)
    private BigDecimal price;

    @NonNull
    @Schema(description = "The currency of the offer's price.",
            example = "GBP",
            required = true)
    private String currency;

    @Schema(description = "The offer's time to live in milliseconds.",
            example = "60000",
            required = true)
    private long ttl;
}

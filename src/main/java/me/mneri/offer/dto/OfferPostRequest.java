package me.mneri.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.validator.Description;
import me.mneri.offer.validator.Title;

import java.math.BigDecimal;
import java.util.Date;

import static me.mneri.offer.validator.Constants.*;
import static me.mneri.offer.validator.Constants.DESCRIPTION_MIN_LENGTH;

/**
 * DTO for a user's request to create a new {@link Offer}.
 *
 * @author mneri
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class OfferPostRequest {
    @NonNull
    @Schema(description = "Offer's title.",
            example = "Buy 1 get 1 for free.",
            maxLength = TITLE_MAX_LENGTH,
            minLength = TITLE_MIN_LENGTH,
            required = true)
    @Title
    private String title;

    @NonNull
    @Schema(description = "Offer's description.",
            example = "Order 1 bag of coffee and get one free.",
            maxLength = DESCRIPTION_MAX_LENGTH,
            minLength = DESCRIPTION_MIN_LENGTH,
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    @Schema(description = "The offer's end date.",
            example = "2020-12-32 00:00:00.000",
            required = true)
    private Date end;
}

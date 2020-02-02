package me.mneri.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.validator.Description;
import me.mneri.offer.validator.Title;

import java.math.BigDecimal;
import java.util.Date;

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
    @Title
    private String title;

    @NonNull
    @Description
    private String description;

    @NonNull
    private BigDecimal price;

    @NonNull
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Date end;
}

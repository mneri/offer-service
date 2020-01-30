package me.mneri.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import me.mneri.offer.entity.Offer;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for {@link Offer} objects.
 *
 * @author mneri
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class OfferDto {
    @NonNull
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private BigDecimal price;

    @NonNull
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Date end;
}

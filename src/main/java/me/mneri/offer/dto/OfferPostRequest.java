package me.mneri.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import me.mneri.offer.validator.OfferDescription;
import me.mneri.offer.validator.OfferTitle;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class OfferPostRequest {
    @NonNull
    @OfferTitle
    private String title;

    @NonNull
    @OfferDescription
    private String description;

    @NonNull
    private BigDecimal price;

    @NonNull
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Date end;
}

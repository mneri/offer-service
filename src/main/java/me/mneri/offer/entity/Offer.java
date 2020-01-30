package me.mneri.offer.entity;

import lombok.*;
import me.mneri.offer.validator.OfferDescription;
import me.mneri.offer.validator.OfferTitle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@ToString
public class Offer {
    @Builder
    private Offer(String title, String description, BigDecimal price, String currency, Date end, User publisher) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.end = end;
        this.publisher = publisher;
    }

    @Id
    @NonNull
    @NotBlank
    @Setter(PROTECTED)
    private String id;

    @Column
    @NonNull
    @OfferTitle
    private String title;

    @Column
    @NonNull
    @OfferDescription
    private String description;

    @Column(precision = 16, scale = 2)
    @NonNull
    private BigDecimal price;

    @Column
    @NonNull
    private String currency;

    @Column(name = "end_time")
    @NonNull
    private Date end;

    @Column
    private boolean canceled;

    @ManyToOne(fetch = LAZY)
    @NonNull
    private User publisher;
}

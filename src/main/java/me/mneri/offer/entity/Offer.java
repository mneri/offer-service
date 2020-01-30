package me.mneri.offer.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    private Offer(@NonNull String title, @NonNull String description, @NonNull BigDecimal price,
                  @NonNull String currency, @NonNull Date end, @NonNull User publisher) {
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
    @Setter(PROTECTED)
    private String id;

    @Column
    @NonNull
    private String title;

    @Column
    @NonNull
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

package me.mneri.offer.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PRIVATE)
@Builder
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString
public class Offer {
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

    @Column
    @NonNull
    private Date startTime;

    @Column
    @NonNull
    private Date endTime;

    @Column
    private boolean canceled;

    @ManyToOne(fetch = LAZY)
    @NonNull
    private User owner;
}

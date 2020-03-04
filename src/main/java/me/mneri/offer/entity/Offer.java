package me.mneri.offer.entity;

/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import lombok.*;
import me.mneri.offer.validator.Description;
import me.mneri.offer.validator.Title;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * ORM for {@code offer} table.
 * <p>
 * The id is immutable and is assigned upon creation. ORM objects are compared by their id and not their state.
 *
 * @author mneri
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Offer {
    @Builder
    private Offer(String title, String description, BigDecimal price, String currency, long ttl, User publisher) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.createTime = new Date();
        this.endTime = new Date(createTime.getTime() + ttl);
        this.publisher = publisher;
    }

    /*
     * We want to give to the user an interface that talks about 'ttl' (time to live, duration of an offer) but querying
     * the ttl is hard. It would be far more convenient to store an 'end_time' field and filter out the expired offers
     * using it.
     *
     * Here, we create two transient getter and setter method publicly visible that mask the 'end_time' field.
     */

    @Transient
    public long getTtl() {
        return endTime.getTime() - createTime.getTime();
    }

    @Transient
    public void setTtl(long ttl) {
        endTime = new Date(createTime.getTime() + ttl);
    }

    @Id
    @NonNull
    @NotBlank
    @Setter(AccessLevel.PROTECTED)
    private String id;

    @Column
    @NonNull
    @Title
    private String title;

    @Column
    @NonNull
    @Description
    private String description;

    @Column(precision = 16, scale = 2)
    @NonNull
    private BigDecimal price;

    @Column
    @NonNull
    private String currency;

    @Column(name = "create_time")
    @NonNull
    @Setter(AccessLevel.PROTECTED)
    private Date createTime;

    @Column(name = "end_time")
    @NonNull
    @Setter(AccessLevel.PROTECTED)
    private Date endTime;

    @Column
    private boolean canceled;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private User publisher;
}

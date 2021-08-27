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

package me.mneri.offer.data.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.mneri.offer.data.validator.Description;
import me.mneri.offer.data.validator.Title;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.Date;
import java.util.UUID;

/**
 * ORM for the {@code offer} table.
 * <p>
 * The id is immutable and is assigned upon creation. ORM objects are compared by their id and not their state.
 *
 * @author Massimo Neri
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer {
    /**
     * The maximum length for a description field.
     */
    public static final int DESCRIPTION_MAX_LENGTH = 8192;

    /**
     * The minimum length for a description field.
     */
    public static final int DESCRIPTION_MIN_LENGTH = 1;

    /**
     * The maximum length for a title field.
     */
    public static final int TITLE_MAX_LENGTH = 256;

    /**
     * The minimum length for a description field.
     */
    public static final int TITLE_MIN_LENGTH = 1;

    /**
     * The minimum TTL value.
     */
    public static final int TTL_MIN_VALUE = 1;

    @Id
    @NotNull
    @Setter(AccessLevel.PROTECTED)
    private UUID id;

    @Column
    @Title
    private String title;

    @Column
    @Description
    private String description;

    @Column(precision = 16, scale = 2)
    @NotNull
    private BigDecimal price;

    @Column
    @NotEmpty
    private String currency;

    @Column(name = "create_time")
    @NotNull
    @Setter(AccessLevel.PROTECTED)
    private Date createTime;

    @Column(name = "end_time")
    @NotNull
    @Setter(AccessLevel.PROTECTED)
    private Date endTime;

    @Column
    private boolean cancelled;

    @JoinColumn(name = "publisher")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @ToString.Exclude
    private User publisher;

    /**
     * Return this offer's TTL.
     *
     * @param clock The system clock.
     * @return The offer's TTL.
     */
    @Transient
    public long getTtl(Clock clock) {
        return Math.max(0, endTime.getTime() - clock.millis());
    }

    /**
     * Return {@code true} if the offer is expired, {@code false} otherwise.
     * <p>
     * An offer is expired if its end time is previous than the current time. In other words, an offer is expired if the
     * TTL has reached zero.
     *
     * @param clock The system clock.
     * @return {@code true} if the offer is expired, {@code false} otherwise.
     */
    @Transient
    public boolean isExpired(Clock clock) {
        return endTime.getTime() < clock.millis();
    }

    /**
     * Set this offer's TTL.
     *
     * @param ttl   The new TTL.
     * @param clock The system clock.
     * @throws IllegalArgumentException If the TTL is equal or less than zero.
     */
    @Transient
    public void setTtl(long ttl, Clock clock) {
        if (ttl <= 0) {
            throw new IllegalArgumentException("TTL is less or equal to 0.");
        }

        setEndTime(new Date(clock.millis() + ttl));
    }
}

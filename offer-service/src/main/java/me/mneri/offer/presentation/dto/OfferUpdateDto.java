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

package me.mneri.offer.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.validator.Description;
import me.mneri.offer.data.validator.Title;
import me.mneri.offer.presentation.validator.Ttl;

import java.math.BigDecimal;

/**
 * DTO for a user's request to update an {@link Offer}.
 *
 * @author Massimo Neri
 */
@Data
@Schema(name = "OfferUpdate")
public class OfferUpdateDto {
    @Schema(description = "Offer's title.",
            example = "Buy 1 get 1 for free.",
            maxLength = Offer.TITLE_MAX_LENGTH,
            minLength = Offer.TITLE_MIN_LENGTH,
            required = true)
    @Title
    private String title;

    @Description
    @Schema(description = "Offer's description.",
            example = "Order 1 bag of coffee and get one free.",
            maxLength = Offer.DESCRIPTION_MAX_LENGTH,
            minLength = Offer.DESCRIPTION_MIN_LENGTH,
            required = true)
    private String description;

    @Schema(description = "Offer's price.",
            example = "100.00",
            required = true)
    private BigDecimal price;

    @Schema(description = "The currency of the offer's price.",
            example = "GBP",
            required = true)
    private String currency;

    @Schema(description = "The offer's time to live in milliseconds.",
            example = "60000",
            required = true)
    @Ttl
    private Long ttl;
}

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

package me.mneri.offer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.mneri.offer.entity.User;
import me.mneri.offer.validator.Constants;
import me.mneri.offer.validator.Username;

/**
 * DTO for {@link User} objects.
 *
 * @author mneri
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Schema(name = "User")
@ToString
public class UserDto {
    @Schema(description = "User's unique identifier.",
            example = "123e4567-e89b-12d3-a456-556642440000",
            required = true)
    @NonNull
    private String id;

    @Schema(description = "User's username.",
            example = "john_doe",
            maxLength = Constants.USERNAME_MAX_LENGTH,
            minLength = Constants.USERNAME_MIN_LENGTH,
            pattern = Constants.USERNAME_REGEXP,
            required = true)
    @NonNull
    @Username
    private String username;
}

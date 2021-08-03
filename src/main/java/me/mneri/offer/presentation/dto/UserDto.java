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
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.validator.Username;

import java.util.UUID;

/**
 * DTO for {@link User} objects.
 *
 * @author Massimo Neri
 */
@Data
@NoArgsConstructor
@Schema(name = "User")
public class UserDto {
    @NonNull
    @Schema(description = "User's unique identifier.",
            example = "123e4567-e89b-12d3-a456-556642440000",
            required = true)
    private UUID id;

    @NonNull
    @Schema(description = "User's username.",
            example = "john_doe",
            maxLength = User.USERNAME_MAX_LENGTH,
            minLength = User.USERNAME_MIN_LENGTH,
            pattern = User.USERNAME_REGEXP,
            required = true)
    @Username
    private String username;
}

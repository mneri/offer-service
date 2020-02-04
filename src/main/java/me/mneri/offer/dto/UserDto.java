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

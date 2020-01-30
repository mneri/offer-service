package me.mneri.offer.dto;

import lombok.*;
import me.mneri.offer.entity.User;
import me.mneri.offer.validator.Username;

/**
 * DTO for {@link User} objects.
 *
 * @author mneri
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class UserDto {
    @NonNull
    private String id;

    @NonNull
    @Username
    private String username;
}

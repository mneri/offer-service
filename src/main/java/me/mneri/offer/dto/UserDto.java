package me.mneri.offer.dto;

import lombok.*;
import me.mneri.offer.validator.Username;

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

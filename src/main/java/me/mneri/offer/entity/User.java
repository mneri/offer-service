package me.mneri.offer.entity;

import lombok.*;
import me.mneri.offer.validator.Username;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

/**
 * ORM for {@code user} table.
 * <p>
 * The id is immutable and is assigned upon creation. ORM objects are compared by their id and not their state.
 *
 * @author mneri
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@ToString
public class User {
    /**
     * Create a new {@code User}.
     *
     * @param username        The username.
     * @param rawPassword     The raw password.
     * @param passwordEncoder The password encoder.
     */
    public User(@NonNull String username, @NonNull String rawPassword, @NonNull PasswordEncoder passwordEncoder) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.encodedPassword = passwordEncoder.encode(rawPassword);
        this.enabled = true;
    }

    /**
     * Set the encoded password.
     *
     * @param rawPassword     The raw password.
     * @param passwordEncoder The encoder.
     */
    public void setEncodedPassword(@NonNull String rawPassword, @NonNull PasswordEncoder passwordEncoder) {
        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }

    @Column
    @Id
    @NonNull
    @Setter(PROTECTED)
    private String id;

    @Column(unique = true)
    @NonNull
    @Username
    private String username;

    /*
     * The field name is 'encodedPassword' to give the programmer better understanding of what this field *really*
     * contains. The name 'password' would have been ambiguous.
     */
    @Column(name = "password")
    @NonNull
    @Setter(PROTECTED)
    private String encodedPassword;

    @Column
    private boolean enabled;
}

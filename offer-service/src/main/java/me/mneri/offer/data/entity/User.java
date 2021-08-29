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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import me.mneri.offer.data.validator.Username;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * ORM for the {@code user} table.
 * <p>
 * The id is immutable and is assigned upon creation. ORM objects are compared by their id and not their state.
 *
 * @author Massimo Neri
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user", indexes = @Index(name = "idx_user_username_unq", columnList = "username", unique = true))
public class User {
    /**
     * The maximum length for a username field.
     */
    public static final int USERNAME_MAX_LENGTH = 24;

    /**
     * The minimum length for a username field.
     */
    public static final int USERNAME_MIN_LENGTH = 3;

    /**
     * The regular expression defining the format of a username field.
     */
    public static final String USERNAME_REGEXP = "[a-zA-Z0-9_]+";

    @Id
    @NotNull
    @Setter(AccessLevel.PROTECTED)
    private UUID id;

    @Column(unique = true)
    @Username
    private String username;

    @Column(name = "password")
    @NotBlank
    @Setter(AccessLevel.PROTECTED)
    @ToString.Exclude
    private String encodedPassword;

    @Column
    private boolean enabled;

    @JoinTable(
            name = "user_authority",
            inverseJoinColumns = @JoinColumn(name = "authority"),
            joinColumns = @JoinColumn(name = "user"))
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Authority> authorities;

    @Column
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    @ToString.Exclude
    @Version
    private long version;

    @Transient
    public void setEncodedPassword(@NonNull String rawPassword, @NonNull PasswordEncoder passwordEncoder) {
        setEncodedPassword(passwordEncoder.encode(rawPassword));
    }
}

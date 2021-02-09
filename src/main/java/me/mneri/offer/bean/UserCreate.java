package me.mneri.offer.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class UserCreate {
    private String username;

    private String password;
}

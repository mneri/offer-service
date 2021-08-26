package me.mneri.offer.business.bean;

import lombok.val;
import me.mneri.offer.business.pojo.UserCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserCreateTest {
    @Test
    public void givenPassword_whenToStringIsCalled_thenPasswordIsNotShown() {
        // Given
        val create = newUserCreateInstance();

        // When
        val actual = create.toString();

        // Then
        Assertions.assertThat(actual.contains(create.getPassword())).isFalse();
    }

    private UserCreate newUserCreateInstance() {
        val create = new UserCreate();
        create.setPassword("secret");
        return create;
    }
}

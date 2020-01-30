package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserToUserDtoMappingTest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void givenUser_whenUserIsMappedToUserDto_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        // When
        val dto = modelMapper.map(user, UserDto.class);

        // Then
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
    }
}

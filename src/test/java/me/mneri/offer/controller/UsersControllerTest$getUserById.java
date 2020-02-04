package me.mneri.offer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.User;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /users/{userId}} endpoint.
 * <p>
 * We test 2 main cases:
 * <ul>
 *     <li>An empty repository;</li>
 *     <li>A repository containing the specified user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class UsersControllerTest$getUserById {
    private static final String PATH = "/users/%s";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @BeforeEach
    private void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test the endpoint against an empty repository.
     */
    @SneakyThrows
    @Test
    void givenEmptyRepository_whenGetUserByIdIsCalled_thenNoUserIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();
        Optional<User> optional = Optional.empty();

        given(userService.findEnabledById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing a user with the specified user id.
     */
    @SneakyThrows
    @Test
    void givenEnabledUser_whenGetUsersIsCalled_thenUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val id = user.getId();
        Optional<User> optional = Optional.of(user);

        given(userService.findEnabledById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        UserDto result = objectMapper.readValue(response.getContentAsString(), UserDto.class);
        UserDto expected = modelMapper.map(user, UserDto.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}

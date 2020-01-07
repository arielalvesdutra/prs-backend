package dev.arielalvesdutra.prs.it.controllers;

import dev.arielalvesdutra.prs.builders.UserBuilder;
import dev.arielalvesdutra.prs.controllers.dto.LoginFormDTO;
import dev.arielalvesdutra.prs.controllers.dto.TokenDTO;
import dev.arielalvesdutra.prs.repositories.UserRepository;
import dev.arielalvesdutra.prs.services.UserService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureTestDatabase
@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthControllerIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void authentication_withValidUser_shouldWork() {
        String password = "password";
        String email = "michal@dundermifflin.com";
        userService.create(new UserBuilder()
                .withEmail(email)
                .withPassword(password)
                .build());
        LoginFormDTO loginFormDTO = new LoginFormDTO(email, password);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LoginFormDTO> httpEntity = new HttpEntity<>(loginFormDTO, headers);

        ResponseEntity<TokenDTO> response = restTemplate.exchange(
                "/auth",
                HttpMethod.POST,
                httpEntity,
                TokenDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getToken()).isNotNull();
    }
}

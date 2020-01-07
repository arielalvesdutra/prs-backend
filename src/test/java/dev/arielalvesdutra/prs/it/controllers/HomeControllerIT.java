package dev.arielalvesdutra.prs.it.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HomeControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void rootRoute_shouldReturnWelcomeMessageWith200Status() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/",
                HttpMethod.GET,
                null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Seja bem vindo!");
    }
}

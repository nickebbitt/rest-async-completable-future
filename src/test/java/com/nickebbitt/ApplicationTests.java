package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.nickebbitt.Application.RESULT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void syncEndpointShouldReturnString() {

		log.info("Before request");

		final String result = restTemplate.getForObject("/sync", String.class);

		log.info("After request");

		assertThat(result).isEqualTo(RESULT);

	}

    @Test
    public void asyncEndpointShouldReturnStringWithDeferredResult() {

		log.info("Before request");

        final String result = restTemplate.getForObject("/asyncDeferred", String.class);

		log.info("After request");

        assertThat(result).isEqualTo(RESULT);

    }

	@Test
	public void asyncEndpointShouldReturnStringWithCompletableFuture() {

		log.info("Before request");

		final String result = restTemplate.getForObject("/asyncCompletable", String.class);

		log.info("After request");

		assertThat(result).isEqualTo(RESULT);

	}


}

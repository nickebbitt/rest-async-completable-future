package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
@Slf4j
public class Application {

    public static final String ASYNC_RESULT = "Async Result";

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping(path = "/async", method = RequestMethod.GET)
	public DeferredResult<String> getValueAsyncUsingCompletableFuture() {

	    log.info("Request received: {} : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

		final DeferredResult<String> deferredResult = new DeferredResult<>();

		CompletableFuture
            .supplyAsync(this::processRequest)
            .whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));

        log.info("Servlet thread released: {} : {}", Thread.currentThread().getId(), Thread.currentThread().getName());

		return deferredResult;

	}

    private String processRequest() {
        log.info("Processing request: {} : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Completed processing request: {} : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        return ASYNC_RESULT;
    }
}

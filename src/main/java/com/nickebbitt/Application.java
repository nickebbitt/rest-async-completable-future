package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
@Slf4j
public class Application {

    public static final String RESULT = "Result";

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    private String processRequest() {
        log.info("Start processing request");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Completed processing request");
        return RESULT;
    }

    @RequestMapping(path = "/sync", method = RequestMethod.GET)
    public String getValueSync() {

        log.info("Request received");

        return processRequest();

    }

    @RequestMapping(path = "/asyncDeferred", method = RequestMethod.GET)
    public DeferredResult<String> getValueAsyncUsingDeferredResult() {

        log.info("Request received");

        DeferredResult<String> stringDeferredResult = new DeferredResult<>();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> stringDeferredResult.setResult(processRequest()));

        log.info("Servlet thread released");

        return stringDeferredResult;

    }

	@RequestMapping(path = "/asyncCompletable", method = RequestMethod.GET)
	public CompletableFuture<String> getValueAsyncUsingCompletableFuture() {

	    log.info("Request received");

        CompletableFuture<String> stringCompletableFuture = CompletableFuture
                .supplyAsync(this::processRequest);

        log.info("Servlet thread released");

		return stringCompletableFuture;

	}

}

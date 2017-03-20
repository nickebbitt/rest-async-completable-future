package com.nickebbitt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
@RestController
@Slf4j
public class Application {

    static final String RESULT = "Result";

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    private String processRequest() {
        log.info("Start processing request");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

        DeferredResult<String> deferredResult = new DeferredResult<>();

        ForkJoinPool.commonPool()
                .submit(() -> deferredResult.setResult(processRequest()));

        log.info("Servlet thread released");

        return deferredResult;

    }

    @RequestMapping(path = "/asyncCompletable", method = RequestMethod.GET)
    public CompletableFuture<String> getValueAsyncUsingCompletableFuture() {

        log.info("Request received");

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(this::processRequest);

        log.info("Servlet thread released");

        return completableFuture;

    }

}

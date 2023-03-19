package com.example.callbackspringtest.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureExUseGet {

    public static void main(String[] args)  throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> f = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        log.info(f.get());
        log.info("Exit");
//        get 메서드를 호출하게 되면 비동기 작업이 완료될 때까지 해당 스레드가 blocking
    }
}

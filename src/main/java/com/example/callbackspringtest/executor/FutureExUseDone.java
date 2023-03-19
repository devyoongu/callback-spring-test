package com.example.callbackspringtest.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureExUseDone {

    //해당 연산이 완료되었는지 확인하는(isDone) 메소드 제공

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> f = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        log.info(String.valueOf(f.isDone()));
        Thread.sleep(2000);
        log.info("Exit");
        log.info(String.valueOf(f.isDone()));
        log.info(f.get());
    }
}

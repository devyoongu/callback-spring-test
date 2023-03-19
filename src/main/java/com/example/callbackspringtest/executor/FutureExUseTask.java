package com.example.callbackspringtest.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class FutureExUseTask {
    //FutureTask는 비동기 작업 생성과 실행을 분리하여 진행할 수 있다.

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        });

        es.execute(f);

        log.info(String.valueOf(f.isDone()));
        Thread.sleep(2000);
        log.info("Exit");
        log.info(String.valueOf(f.isDone()));
        log.info(f.get());
    }
}

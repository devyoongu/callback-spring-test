package com.example.callbackspringtest.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Slf4j
public class FutureExUseTaskAndDone {
    // FutureTask의 비동기 작업이 완료될 경우 호출되는 done() 메서드를 재정의하여 callback을 이용하는 방법

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<String>(() -> {
            Thread.sleep(2000);
            log.info("Async");
            return "Hello";
        }) {
            @Override
            protected void done() {
                super.done();
                try {
                    log.info("done>>"+get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(f);
        es.shutdown();

        log.info("EXIT");
    }
}

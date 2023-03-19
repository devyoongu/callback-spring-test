package com.example.callbackspringtest.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);  //*race condition이 발생할 수 있기 때문에  여러 쓰레드에서 서로 간섭하지 않도록 값을 갖고 와서 증가시키키 위함

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es = Executors.newFixedThreadPool(100);     // 100개의 코어 스레드 생성

        RestTemplate rt = new RestTemplate();   // 웹 요청을 날릴거니까
//        String url = "http://localhost:8080/rest?idx={idx}";
        String url = "http://localhost:8080/callable";

//        CyclicBarrier barrier = new CyclicBarrier(101);

        for (int i=0; i<100; i++) {
            es.submit(() -> {
                //람다-별도 쓰레드에서 다른 쓰레드의 로컬 변수 i를 접근하지못한다.
                int idx = counter.addAndGet(1);

//                barrier.await();

                log.info("Thread {}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String res = rt.getForObject(url, String.class, idx);
                sw.stop();
                log.info("Elapsed: {} {} / {}", idx, sw.getTotalTimeSeconds(), res);

                return null;    // Callable로 인식하게 함. (Runnable -> 리턴값 없음)
            });
        }

//        barrier.await();
        StopWatch main = new StopWatch();
        main.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS); //* 지정된 시간이 타임아웃되기 전이라면 대기작업이 될때까지 기다려 준다?
         //여기 이후부터는 100초가 지났거나 작업이 종료됨

        main.stop();
        log.info("Total: {}", main.getTotalTimeSeconds());
    }
}

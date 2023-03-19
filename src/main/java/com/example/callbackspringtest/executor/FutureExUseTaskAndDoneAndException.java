package com.example.callbackspringtest.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class FutureExUseTaskAndDoneAndException {
    /*
        FutureTask를 상속받아 done() 메서드를 재정의,
        비동기 코드와 그 결과를 갖고 작업을 수행하는 callback으로 처리
     */
    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            super.done();
            try {
                this.sc.onSuccess(get());
                log.info("success callback>>",get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                // 래핑된 에러를 빼내어 전달한다.
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f1 = new CallbackFutureTask(() -> {
            Thread.sleep(1000);
            log.info("Thread success inner log");
            return "Success";
        },
                s -> log.info("Result: {}", s),
                e -> log.info("Error: {}", e.getMessage()));

        CallbackFutureTask f2 = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            if (1 == 1) throw new RuntimeException("Async ERROR!!!");
            log.info("Thread exception inner log");
            return "Exception";
        },
                s -> log.info("Result: {}", s),
                e -> log.info("Error: {}", e.getMessage()));


        es.execute(f1);
        es.execute(f2);
        es.shutdown();

        log.info("EXIT");
    }
}

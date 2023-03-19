package com.example.callbackspringtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@Slf4j
@EnableAsync
//@SpringBootApplication
public class CallbackSpringTestApplication {
	@Component
	public static class MyService {
		@Async(value = "tp") //Async 별로 어떤 쓰레드를 사용할지 고를수도 있다.
		public ListenableFuture<String> hello() throws InterruptedException {
			log.info("hello()");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.warn("Interrupted on thread {}", Thread.currentThread().getId());
				throw e;
			}
			return new AsyncResult<>("Hello"); // AsyncResult: 비동기적 결과를 리턴한는데 Future 타고서 결과를 리턴하도록 설정하는 방법
		}
	}

	@Bean
	ThreadPoolTaskExecutor tp(){
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(10); //기본 풀사이즈 지정 //jmax 를 통해서 런타입에 쓰레드 풀을 변경할 수 있다.
		te.setMaxPoolSize(100); //
		te.setQueueCapacity(200); //10개가 꽉차면 큐가 먼저 차면 그때 setMaxPoolSize가 찬다.
		te.setThreadNamePrefix("mythread");
		te.initialize();

		return te;
	};

	public static void main(String[] args) {
		try (ConfigurableApplicationContext run = SpringApplication.run(CallbackSpringTestApplication.class, args)){}
	}

	@Autowired
	MyService myService;

	@Bean
	ApplicationRunner run(){
		return args -> {
			log.info("run()");
			ListenableFuture<String> f = myService.hello();
			f.addCallback(s -> System.out.println(s),
					e -> System.out.println(e.getMessage())
			);
			log.info("exit");
		};
	}
}

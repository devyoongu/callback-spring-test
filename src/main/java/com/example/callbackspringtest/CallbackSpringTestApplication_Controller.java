package com.example.callbackspringtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableAsync
@SpringBootApplication
public class CallbackSpringTestApplication_Controller {

	@RestController
	public static class MyController {

		@GetMapping("/callable")
		public String callable() throws InterruptedException {
			log.info("callable");
			log.info("async..");
			Thread.sleep(2000);
			return "hello";

			/*return () -> { //Callable은 람다를 리턴한다.
				log.info("async..");
				Thread.sleep(2000);
				return "hello";
			};*/
		}
	}
//결과
//	 [nio-8080-exec-1] CallbackSpringTestApplication_Controller : callable
//   [         task-1] CallbackSpringTestApplication_Controller : async..

	public static void main(String[] args) {
		SpringApplication.run(CallbackSpringTestApplication_Controller.class, args);
	}

}

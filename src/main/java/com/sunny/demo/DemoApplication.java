package com.sunny.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DemoApplication {
	/**
	 * Springboot配置过滤器问题参考: https://www.cnblogs.com/begin2016/p/8947887.html
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

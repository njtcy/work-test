package com.taobao.hsf.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerStart {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext-server.xml");
		String[] names = context.getBeanDefinitionNames();
		System.out.println(names[0]);
        while (true) {  
            System.out.println("spring context server started.");  
            Thread.sleep(10000);  
        }  
	}
}


package com.taobao.hsf.service;

import com.taobao.hsf.lightapi.ServiceFactory;
/**
 *	@author 青宛
 *	@mail chunyang.tcy@alibaba-inc.com 
 *	@date 2017年5月1日 下午8:54:58
 */
public class ServicesContainer {
	// 作为一个Services的容器类，这个必须要放在【入口】类，并且要【static】，是为了不污染classloader，见谅。
	public static ServiceFactory factory = ServiceFactory.getInstance();

	static {
		initServices();
	}

	public static void initServices() {
		factory.provider("helloProvider")// 参数是一个标识，初始化后，下次只需调用provider("helloProvider")即可拿出对应服务
				.service("com.taobao.hsf.service.HelloWorldService")// 服务名 &
																		// 接口全类名
				.version("1.0.0.daily")// 版本号
				.group("light")// 组别
				// .writeMode("unit",0) //设置单元化服务的writeMode,非unit服务第二个参数随意
				.impl(new HelloWorldServiceImpl())// 对应的服务实现
				.publish();// 发布服务，至少要调用service()和version()才可以发布服务

		factory.consumer("helloConsumer")// 参数是一个标识，初始化后，下次只需调用consumer("helloConsumer")即可直接拿出对应服务
				.service("com.taobao.hsf.service.HelloWorldService")// 服务名 &
																		// 接口全类名
				.version("1.0.0.daily")// 版本号
				.group("light")// 组别
				.subscribe();// 消费服务并获得服务的接口，至少要调用service()和version()才可以消费服务
		// 这里调用subscribe()是为了提前订阅地址，也可以在真正使用的时候在调用，如下方的main。
		// 推荐还是在初始化的时候调用，可以让地址更快速地推送，虽然丑了一点。
	}

	public static void main(String[] args) throws Exception {
		factory.consumer("helloConsumer").sync();// 同步等待地址推送，最多6秒。
		HelloWorldService helloWorldService = (HelloWorldService) factory.consumer("helloConsumer").subscribe();// 用ID取出对应服务，subscribe()方法返回对应的接口
		System.out.println(helloWorldService.sayHello("lanshan1"));
	}
}

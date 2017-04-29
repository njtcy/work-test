package com.taobao.mq;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.taobao.metaq.client.MetaPushConsumer;

public class PushConsumer {

	public static void main(String[] args) throws InterruptedException, MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一<br>
         * ConsumerGroupName在生产环境需要申请，非生产环境不需要
         */
        MetaPushConsumer consumer = new MetaPushConsumer("TCY_Consumer_Group");

        /**
         * 订阅指定topic下tags分别等于TagA或TagC或TagD
         */
        //consumer.subscribe("TopicTest1", "TagA || TagC || TagD");
        //consumer.setConsumeMessageBatchMaxSize(3);
        /**
         * 订阅指定topic下所有消息<br>
         * 注意：一个consumer对象可以订阅多个topic
         */
        consumer.subscribe("JW_APP_ALIMONITOR_message_log", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            /**
             * 1、默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br>
             * 2、如果设置为批量消费方式，要么都成功，要么都失败。<br>
             * 3、此方法由MetaQ客户端多个线程回调，需要应用来处理并发安全问题<br>
             * 4、抛异常与返回ConsumeConcurrentlyStatus.RECONSUME_LATER等价<br>
             * 5、每条消息失败后，会尝试重试，重试5次都失败，则丢弃<br>
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
				for (MessageExt msg : msgs) {
					System.out.println("TAG:"+msg.getTags()+",Value:"+new String(msg.getBody()));
				}
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         */
        consumer.start();

        System.out.println("Consumer Started.");
    }
}

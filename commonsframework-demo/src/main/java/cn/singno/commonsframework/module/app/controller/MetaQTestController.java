///**<p>项目名：</p>
// * <p>包名：cn.singno.commonsframework.module.app.controller</p>
// * <p>文件名：MetaQTestController.java</p>
// * <p>版本信息：</p>
// * <p>日期：2015-4-7-下午8:58:50</p>
// * Copyright (c) 2015singno公司-版权所有
// */
//package cn.singno.commonsframework.module.app.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import cn.singno.commonsframework.MQ.metaQ.ProducerFactory;
//
//import com.taobao.metamorphosis.Message;
//import com.taobao.metamorphosis.client.producer.MessageProducer;
//import com.taobao.metamorphosis.exception.MetaClientException;
//
///**<p>名称：MetaQTestController.java</p>
// * <p>描述：</p>
// * <pre>
// *    
// * </pre>
// * @author 周光暖
// * @date 2015-4-7 下午8:58:50
// * @version 1.0.0
// */
//@Controller
//@RequestMapping("/metaQ")
//public class MetaQTestController
//{
//	@Autowired
//	private ProducerFactory producerFactory;
//	
//	@RequestMapping("/test")
//	public String test(String message) throws MetaClientException, InterruptedException{ 
//		String topic= "topic_test1";
//		MessageProducer producer = producerFactory.getByTopic(topic);
//		producer.sendMessage(new Message(topic, message.getBytes()));
//		
//		return null;
//	}
//}

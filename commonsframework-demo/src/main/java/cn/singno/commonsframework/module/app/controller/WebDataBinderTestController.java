package cn.singno.commonsframework.module.app.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.singno.commonsframework.bind.annotation.EnumConst;
import cn.singno.commonsframework.constants.SexEnum;
import cn.singno.commonsframework.module.app.entity.User;
import cn.singno.commonsframework.module.app.model.TestMap;
import cn.singno.commonsframework.module.app.model.TestModel;
import cn.singno.commonsframework.utils.NetworkUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class WebDataBinderTestController
{
	
	@RequestMapping(value = "/test/binder", method = RequestMethod.GET)
	@ResponseBody
	public Object test(String str, HttpServletRequest request, Boolean bool, User user,  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date date)
	{ 
		String LocalIp = NetworkUtils.getLocalIp();
		String NetIp = NetworkUtils.getNetIp();
		String RemortIp= NetworkUtils.getRemortIpStr(request);
		System.out.println("LocalIp：" + LocalIp);
		System.out.println("NetIp：" + NetIp);
		System.out.println("RemortIp：" + RemortIp);
		
		
		
		
		System.out.println(date);
		
		int ip = NetworkUtils.getRemortIpInt(request);
		String ipStr = NetworkUtils.getRemortIpStr(request);
		List<Object> list = Lists.newArrayList();
		list.add(ip);
		list.add(ipStr);
		list.add(str);
		list.add(bool);
		list.add(date);
		return list;
	};
	
	@RequestMapping(value = "/test/enum", method = RequestMethod.GET)
	@ResponseBody
	public Object test2(@EnumConst SexEnum sexEnum)
	{
		
		return sexEnum;
	}
	
	@RequestMapping(value = "/test/enum2", method = RequestMethod.GET)
	@ResponseBody
	public Object test3(User user)
	{
		
		return user.getSex();
	}
	
	@RequestMapping(value = "/test/map", method = RequestMethod.POST)
	@ResponseBody
	public Object test4(TestModel test)
	{
		
		return test;
	}
	
	@Test
	public void testname() throws Exception
	{
		Map<String, String> map = Maps.newHashMap();
		map.put("李四", "1111");
		map.put("李2", "1111");
		System.out.println(JSON.toJSONString(map));
	}
}

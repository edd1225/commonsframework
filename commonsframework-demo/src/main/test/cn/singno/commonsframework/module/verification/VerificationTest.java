package cn.singno.commonsframework.module.verification;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import cn.singno.commonsframework.generic.GenericControllerTest;

import com.google.common.collect.Maps;
import com.sun.mail.handlers.image_gif;

public class VerificationTest extends GenericControllerTest
{
	@Test
	public void testVerification2() throws Exception
	{
		Map<String, Object> params = Maps.newHashMap();
		ResultActions resultActions = super.getResultActionsByPost("/test/2", params);
		logger.debug(resultActions.andReturn().getResponse().getContentAsString());
	}
	
	@Test
	public void testVerification3() throws Exception
	{
		Map<String, Object> params = Maps.newHashMap();
		ResultActions resultActions = super.getResultActionsByPost("/test/3", params);
		logger.debug(resultActions.andReturn().getResponse().getContentAsString());
	}
	
	@Test
	public void testname() throws Exception
	{
		
	}
}

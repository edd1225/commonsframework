/**<p>项目名：</p>
 * <p>包名：	cn.singno.commonsframework.test</p>
 * <p>文件名：AbstractControllerTest.java</p>
 * <p>版本信息：</p>
 * <p>日期：2014年8月10日-下午11:59:17</p>
 * Copyright (c) 2014singno公司-版权所有
 */
package cn.singno.commonsframework.generic;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Maps;

/**<p>名称：AbstractControllerTest.java</p>
 * <p>描述：</p>
 * <pre>
 *    1、@WebAppConfiguration：测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的；value指定web应用的根
 *    2、@ContextHierarchy：指定容器层次，如果子类继承该类时，且需要扩展配置文件，可以配置子容器。如：
 *    	 @ContextHierarchy(@ContextConfiguration(name = "child", locations = "classpath:spring-jpa.xml, classpath:spring-cache.xml"))
 *    3、通过@Autowired WebApplicationContext wac：注入web环境的ApplicationContext容器
 *    4、然后通过MockMvcBuilders.webAppContextSetup(wac).build()创建一个MockMvc进行测试
 * 	  ------------------------------------------------------------------------------------------------------------------------------------- 
 * 
 *    整个测试的规律：
 *       1、准备测试环境
 *       2、通过MockMvc执行请求
 *       3.1、添加验证断言
 *       3.2、添加结果处理器
 *       3.3、得到MvcResult进行自定义断言/进行下一步的异步请求
 *       4、卸载测试环境
 *       
 *    1、mockMvc.perform执行一个请求；                                                                                                         
 *    2、MockMvcRequestBuilders.get("/user/1")构造一个请求                                                                                       
 *    3、ResultActions.andExpect添加执行完成后的断言                                                                                                 
 *    4、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。                                          
 *    5、ResultActions.andReturn表示执行完成后返回相应的结果。
 * 
 * </pre>
 * @author 周光暖
 * @date 2014年8月10日 下午11:59:17
 * @version 1.0.0
 */
public abstract class GenericControllerTest extends GenericTest
{
	protected MockMvc mockMvc;
	protected FileTypeMap defaultFileTypeMap = MimetypesFileTypeMap.getDefaultFileTypeMap();

	private static String PARAM_SEPARATOR = "&";// 参数分隔符
	private static String ATTRIBUTE_SEPARATOR = "=";// 属性(名、值)分隔符

	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void setUp()
	{
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/**
	 * 描述：执行模拟的HttpServletRequest请求
	 * 
	 * <pre>
	 * 	   执行模拟的HttpServletRequest请求，并返回ResultActions
	 *   可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param mockHttpServletRequestBuilder
	 * @return
	 * @throws Exception
	 */
	protected ResultActions perform(MockHttpServletRequestBuilder requestBuilder) throws Exception
	{
		return mockMvc.perform(requestBuilder);
	}

	/**
	 * 描述：模拟post请求
	 * 
	 * <pre>
	 *    模拟post请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 *            ：Map<paramName, paramValue>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByPost(String url,
			Map<String, Object> params) throws Exception
	{
		return getResultActions(url, params, HttpMethod.POST);
	}

	/**
	 * 描述：模拟post请求
	 * 
	 * <pre>
	 *    模拟post请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByPost(String url,
			String urlVariables) throws Exception
	{
		return getResultActions(url, toMapParams(urlVariables), HttpMethod.POST);
	}
	
	/**
	 * 描述：模拟post请求，带文件上传
	 * 
	 * <pre>
	 *    模拟post请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 *            ：Map<paramName, paramValue>
	 * @param files
	 * 			    ：Map<MultipartFile paramName, List<File>>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByPost(String url,
			Map<String, Object> params, Map<String, List<File>> files) throws Exception
	{
		MockMultipartHttpServletRequestBuilder requestBuilder = this.getMultipartHttpServletRequestBuilderByPost(url, params);
		if (null != files)
		{
			for (Map.Entry<String, List<File>> entry : files.entrySet())
			{
				String name = entry.getKey();
				List<File> list = entry.getValue();
				if (CollectionUtils.isNotEmpty(list))
				{
					for (File file : list)
					{
						MockMultipartFile mockFile = new MockMultipartFile(
								name,// paramName
								file.getName(), // originalFilename
								defaultFileTypeMap.getContentType(file),// contentType
				                FileUtils.readFileToByteArray(file));// content
						requestBuilder.file(mockFile);
					}
				}
			}
		}
		return mockMvc.perform(requestBuilder);
	}
	
	/**
	 * 描述：模拟post请求，带文件上传
	 * 
	 * <pre>
	 *    模拟post请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @param files
	 * 			    ：Map<MultipartFile paramName, List<File>>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByPost(String url, String urlVariables, Map<String, List<File>> files) 
			throws Exception
	{
		return this.getResultActionsByPost(url, toMapParams(urlVariables), files);
	}

	protected ResultActions getResultActionsByGet(String url) throws Exception
	{
		Map<String, String> params = Maps.newHashMap();
		return this.getResultActionsByGet(url, params);
	}
	
	/**
	 * 描述：模拟get请求
	 * 
	 * <pre>
	 *    模拟get请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByGet(String url, Object param)
			throws Exception
	{
		return getResultActions(url, toMapParams(BeanUtils.describe(param)), HttpMethod.GET);
	}

	/**
	 * 描述：模拟get请求
	 * 
	 * <pre>
	 *    模拟get请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 *            ：Map<paramName, paramValue>
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByGet(String url, Map<String, Object> params) throws Exception
	{
		return getResultActions(url, params, HttpMethod.GET);
	}

	/**
	 * 描述：模拟get请求
	 * 
	 * <pre>
	 *    模拟get请求，并返回ResultActions
	 *    可以通过ResultActions处理请求结果
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	protected ResultActions getResultActionsByGet(String url,
			String urlVariables) throws Exception
	{
		return getResultActions(url, toMapParams(urlVariables), HttpMethod.GET);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得get请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByGet(
			String url, Object param) throws Exception
	{
		return createRequestBuilder(url, toMapParams(BeanUtils.describe(param)), HttpMethod.GET);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得get请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 *            ：Map<paramName, paramValue>
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByGet(
			String url, Map<String, Object> params) throws Exception
	{
		return createRequestBuilder(url, params, HttpMethod.GET);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得get请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByGet(
			String url, String urlVariables) throws Exception
	{
		Map<String, Object> params = toMapParams(urlVariables);
		return createRequestBuilder(url, params, HttpMethod.GET);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得post请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByPost(
			String url, Object param) throws Exception
	{
		return createRequestBuilder(url, toMapParams(BeanUtils.describe(param)), HttpMethod.POST);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得post请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param params
	 *            ：Map<paramName, paramValue>
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByPost(
			String url, Map<String, Object> params) throws Exception
	{
		return createRequestBuilder(url, params, HttpMethod.POST);
	}

	/**
	 * 描述：获得MockHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得post请求方式的MockHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @return
	 * @throws Exception
	 */
	protected MockHttpServletRequestBuilder getHttpServletRequestBuilderByPost(
			String url, String urlVariables) throws Exception
	{
		Map<String, Object> params = toMapParams(urlVariables);
		return createRequestBuilder(url, params, HttpMethod.POST);
	}

	/**
	 * 描述：获得MockMultipartHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得带文件上传的MockMultipartHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param 
	 * @return
	 * @throws Exception
	 */
	protected MockMultipartHttpServletRequestBuilder getMultipartHttpServletRequestBuilderByPost(String url, Object param) throws Exception
	{
		return this.getMultipartHttpServletRequestBuilderByPost(url, BeanUtils.describe(param));
	}
	
	/**
	 * 描述：获得MockMultipartHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得带文件上传的MockMultipartHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：Map<paramName, paramValue>
	 * @return
	 * @throws Exception
	 */
	protected MockMultipartHttpServletRequestBuilder getMultipartHttpServletRequestBuilderByPost(String url, Map<String, Object> params) throws Exception
	{
		MockMultipartHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload(url);
		setParams(requestBuilder, params);
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		return requestBuilder;
	}
	
	/**
	 * 描述：获得MockMultipartHttpServletRequestBuilder
	 * 
	 * <pre>
	 * 获得带文件上传的MockMultipartHttpServletRequestBuilder
	 * </pre>
	 * 
	 * @param url
	 * @param urlVariables
	 *            ：paramName1=paramValue1%paramName2=paramValue2...
	 * @return
	 * @throws Exception
	 */
	protected MockMultipartHttpServletRequestBuilder getMultipartHttpServletRequestBuilderByPost(String url, String urlVariables) throws Exception
	{
		return this.getMultipartHttpServletRequestBuilderByPost(url, toMapParams(urlVariables));
	}

	// ===========================================================================================================================

	private MockHttpServletRequestBuilder createRequestBuilder(String url,
			Map<String, Object> params, HttpMethod requestType)
	{
		MockHttpServletRequestBuilder requestBuilder = null;
		if (HttpMethod.POST.equals(requestType))
		{
			requestBuilder = MockMvcRequestBuilders.post(url);
			setParams(requestBuilder, params);
		} else if (HttpMethod.GET.equals(requestType))
		{
			requestBuilder = MockMvcRequestBuilders.get(url, toUrlVariables(params));
		}
		requestBuilder.accept(MediaType.APPLICATION_JSON);
		return requestBuilder;
	}

	private void setParams(MockHttpServletRequestBuilder requestBuilder, Map<String, Object> params)
	{
		if (null != params)
		{
			for (Map.Entry<String, Object> entry : params.entrySet())
			{
				Object obj = entry.getValue();
				requestBuilder.param(entry.getKey(), ObjectUtils.toString(obj, ""));
			}
		}
	}

	private ResultActions getResultActions(String url,
			Map<String, Object> params, HttpMethod requestType)
			throws Exception
	{
		return mockMvc.perform(createRequestBuilder(url, params, requestType));
	}

	private static String toUrlVariables(Map<String, Object> params)
	{
		StringBuilder urlVariables = new StringBuilder();
		if (null != params)
		{
			for (Map.Entry<String, Object> entry : params.entrySet())
			{
				Object obj = entry.getValue();
				urlVariables.append(entry.getKey().trim())
						.append(ATTRIBUTE_SEPARATOR)
						.append(ObjectUtils.toString(obj, "").trim()).append(PARAM_SEPARATOR);
			}
		}
		if (urlVariables.length() == 0)
		{
			return null;
		}
		return urlVariables.substring(0, urlVariables.length() - 1);
	}

	private static Map<String, Object> toMapParams(String urlVariables)
	{
		Map<String, Object> map = Maps.newHashMap();
		String[] params = urlVariables.split(PARAM_SEPARATOR);
		for (String param : params)
		{
			String[] attribute = param.trim().split(ATTRIBUTE_SEPARATOR);
			if (attribute.length == 2)
			{
				map.put(attribute[0].trim(), attribute[1].trim());
			}
		}
		return map;
	}
	
	private Map<String, Object> toMapParams(Object param) throws Exception
	{
		Map<String, Object> paramMap = Maps.newHashMap();
		Map<String, String> map = BeanUtils.describe(param);
		if (null != map)
		{
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				paramMap.put(entry.getKey(), entry.getValue());
			}
		}
		return paramMap;
	}
}

package cn.singno.commonsframework.exception.handler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import cn.singno.commonsframework.bean.JsonResult;
import cn.singno.commonsframework.constants.CommonEnum;
import cn.singno.commonsframework.exception.BusinessException;
import cn.singno.commonsframework.exception.DescribableException;
import cn.singno.commonsframework.exception.ValidatingException;
import cn.singno.commonsframework.utils.ExceptionUtils;
import cn.singno.commonsframework.utils.ValidateUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

/**
 * <p>名称：GlobalExcaptionResolver.java</p>
 * <p>描述：全局异常解析器</p>
 * <pre>
 *    将全局异常解析成相应的错误信息提示
 * </pre>
 * @author 周光暖
 * @date 2015-3-30 下午10:58:39
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class GlobalExcaptionResolver extends AbstractHandlerExceptionResolver
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GlobalExcaptionResolver.class);

	private HttpMessageConverter<Object> jsonMessageConverter;

	public void setJsonMessageConverter(HttpMessageConverter<Object> jsonMessageConverter)
	{
		this.jsonMessageConverter = jsonMessageConverter;
	}

	public HttpMessageConverter<Object> getJsonMessageConverter()
	{
		return jsonMessageConverter;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
	{
		try
		{
			logger.error(ex.getMessage(), ex);
			return handleException(ex, request, response, handler);
		} catch (Exception handlerException)
		{
			logger.error("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
		}
		return null;
	}

	// ===============================================================================================

	/**
	 * 处理异常
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws IOException
	 * @author 周光暖
	 */
	private ModelAndView handleException(Exception ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException
	{
		if (isAjax(ex, request, response, handler))
		{
			return handleAjaxeException(ex, response);
		} else
		{
			return handleCommonException(ex, response);
		}
	}

	/**
	 * 处理ajax请求时的异常
	 * 
	 * @param ex
	 * @param response
	 * @return
	 * @throws IOException
	 * @author 周光暖
	 */
	private ModelAndView handleAjaxeException(Exception ex,
			HttpServletResponse response) throws IOException
	{
		HttpMessageConverter<Object> messageConverter = getJsonMessageConverter();
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		outputMessage.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
		JsonResult jsonResult = getExceptionJsonMessage(ex);
		messageConverter.write(jsonResult, MediaType.APPLICATION_JSON, outputMessage);
		return new ModelAndView();
	}

	/**
	 * 处理普通请求时的异常
	 * 
	 * @param ex
	 * @param response
	 * @return
	 * @throws IOException
	 * @author 周光暖
	 */
	private ModelAndView handleCommonException(Exception ex,
			HttpServletResponse response) throws IOException
	{
		int state = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		if (ex instanceof BindException)
		{
			state = HttpServletResponse.SC_NOT_FOUND;
		}
		response.sendError(state, JSON.toJSONString(getExceptionJsonMessage(ex)));
		return new ModelAndView();
		// return new ModelAndView("forward:/error/error_mobile.jsp",
		// "jsonMessage", getExceptionJsonMessage(ex));
	}

	/**
	 * 判断是否为 ajax 请求
	 * 
	 * @param request
	 * @param response
	 * @param handlerMethod
	 * @param ex
	 * @return
	 */
	private boolean isAjax(Exception ex, HttpServletRequest request,
			HttpServletResponse response, Object handler)
	{
		String contentType = request.getContentType();
		if (!StringUtils.isEmpty(contentType) && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE))
		{
			return true;
		}
		ResponseBody responseBody = null;
		if (handler instanceof HandlerMethod)
		{
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
		}
		return responseBody != null;
	}

	/**
	 * 获得异常描述信息
	 * 
	 * @return
	 * @author 周光暖
	 * @param ex
	 */
	private JsonResult getExceptionJsonMessage(Exception ex)
	{
		DescribableException describableException = null;
		if (ex instanceof UnauthorizedException)
		{
			describableException = new BusinessException(CommonEnum.AUTHORIZED_ERROR);
		}
		// 属性绑定异常
		if (ex instanceof BindException)
		{
			List<String> errorList = Lists.newArrayList();
			for (ObjectError error : ((BindException) ex).getAllErrors())
			{
				errorList.add(error.getDefaultMessage());
			}
			describableException = new ValidatingException(CommonEnum.PARAMES_ERROR, ArrayUtils.toString(errorList));
		} 
		// 参数约束校验异常
		else if (ex instanceof ConstraintViolationException)
		{
			ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
			List<String> errorList = ValidateUtils.getMessageListBySet2(constraintViolations, true);
			describableException = new ValidatingException(CommonEnum.PARAMES_ERROR, ArrayUtils.toString(errorList));
		} 
		// 非法参数异常
		else if (ex instanceof IllegalArgumentException)
		{
			// 检测提示信息是否中文，辨别是否是自定义提示信息
			String message = ex.getMessage();
			if (cn.singno.commonsframework.utils.StringUtils.isContainsChinese(message))
			{
				describableException = new BusinessException(CommonEnum.PARAMES_ERROR, message);
			}
		}
		// 文件上传大小越界异常
		else if (ex instanceof MaxUploadSizeExceededException)
		{
			describableException = new BusinessException(CommonEnum.UPLOAD_ERROR, ex.getMessage());
		}
		// 自定义异常
		else if (ex instanceof DescribableException)
		{
			describableException = (DescribableException) ex;
		} 
		
		if (null == describableException)
		{
			describableException = new BusinessException(CommonEnum.SYSTEM_ERROR);
		}
		return new JsonResult(describableException.getCode(), ExceptionUtils.promptInfo(describableException));
	}
}
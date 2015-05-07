/**
 * <p>包名：	cn.singno.validatorDemo.exception</p>
 * <p>文件名：DescribableException.java</p>
 * <p>版本信息：</p>
 * <p>日期：2014年7月24日-下午2:51:22</p>
 * Copyright (c) 2014singno公司-版权所有
 */
package cn.singno.commonsframework.exception;

import cn.singno.commonsframework.constants.DescribableInfo;

/**
 * <p>名称：DescribableException.java</p>
 * <p>描述：</p>
 * <pre>
 *         
 * </pre>
 * @author 周光暖
 * @date 2014-11-11 下午4:40:22
 * @version 1.0.0
 */
@SuppressWarnings("all")
public abstract class DescribableException extends RuntimeException {

	private Number code;// 编码
	
	private String Info;// 信息

	private String errorDetails;// 异常详细信息

	/**
	 * 构造器
	 * 
	 * <pre></pre>
	 * 
	 * @param exceptionDescribable
	 */
	public DescribableException(DescribableInfo describableInfo) {
		super();
		this.code = describableInfo.getCode();
		this.Info = describableInfo.getInfo();
	}

	/**
	 * 构造器
	 * 
	 * <pre></pre>
	 * 
	 * @param exceptionDescribable
	 * @param errorDetails
	 */
	public DescribableException(DescribableInfo describableInfo, String errorDetails) {
		super();
		this.code = describableInfo.getCode();
		this.Info = describableInfo.getInfo();
		this.errorDetails = errorDetails;
	}

	public Number getCode()
	{
		return code;
	}

	public void setCode(Number code)
	{
		this.code = code;
	}

	public String getInfo()
	{
		return Info;
	}

	public void setInfo(String info)
	{
		Info = info;
	}

	public String getErrorDetails()
	{
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails)
	{
		this.errorDetails = errorDetails;
	}
}

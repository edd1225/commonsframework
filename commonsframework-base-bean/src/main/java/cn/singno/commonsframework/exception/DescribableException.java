/**
 * <p>包名：cn.singno.validatorDemo.exception</p>
 * <p>文件名：DescribableException.java</p>
 * <p>版本信息：</p>
 * <p>日期：2014年7月24日-下午2:51:22</p>
 * Copyright (c) 2014singno公司-版权所有
 */
package cn.singno.commonsframework.exception;

import cn.singno.commonsframework.constants.Describable;

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

	private Number code;// 描述编码
	
	private String message;// 描述信息

	private String detail;// 详细描述

	/**
	 * 构造器
	 * 
	 * <pre></pre>
	 * 
	 * @param describableInfo
	 */
	public DescribableException(Describable describableInfo) {
		super();
		this.code = describableInfo.getCode();
		this.message = describableInfo.getMessage();
	}

	/**
	 * <p>构造器：</p>
	 * <pre>
	 *    
	 * </pre>
	 * @param describableInfo
	 * @param detail
	 */
	public DescribableException(Describable describableInfo, String detail) {
		super();
		this.code = describableInfo.getCode();
		this.message = describableInfo.getMessage();
		this.detail = detail;
	}

	public Number getCode()
	{
		return code;
	}

	public void setCode(Number code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getDetail()
	{
		return this.detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}
}

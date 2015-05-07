/**<p>项目名：</p>
 * <p>包名：	cn.singno.validatorDemo.constants</p>
 * <p>文件名：CommonsErrorConst.java</p>
 * <p>版本信息：</p>
 * <p>日期：2014年7月24日-下午3:25:27</p>
 * Copyright (c) 2014singno公司-版权所有
 */
package cn.singno.commonsframework.constants;


/**<p>名称：ResultEnum.java</p>
 * <p>描述：结果枚举</p>
 * <pre>
 *         
 * </pre>
 * @author 周光暖
 * @date 2014年7月24日 下午3:25:27
 * @version 1.0.0
 */
public enum ResultEnum implements DescribableInfo
{
	// 220000-229999
	EXISTS_USER(120011,"用户已存在!")
	;
	
	private Integer code;// 结果编码
	private String info;// 结果信息

	private ResultEnum(Integer code, String info)
	{
		this.code = code;
		this.info = info;
	}
	
	public Integer getCode()
	{
		return this.code;
	}

	public String getInfo()
	{
		return this.info;
	}
}

/**<p>项目名：</p>
 * <p>包名：cn.singno.commonsframework.module.app.model</p>
 * <p>文件名：TestModel.java</p>
 * <p>版本信息：</p>
 * <p>日期：2015-4-28-上午10:53:32</p>
 * Copyright (c) 2015singno公司-版权所有
 */
package cn.singno.commonsframework.module.app.model;

import java.util.List;
import java.util.Map;

/**<p>名称：TestModel.java</p>
 * <p>描述：</p>
 * <pre>
 *    
 * </pre>
 * @author 周光暖
 * @date 2015-4-28 上午10:53:32
 * @version 1.0.0
 */
public class TestModel
{
	private List<String> list;
	
	private Map<String, String> map;
	
	private String name;

	public List<String> getList()
	{
		return list;
	}

	public void setList(List<String> list)
	{
		this.list = list;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, String> getMap()
	{
		return map;
	}

	public void setMap(Map<String, String> map)
	{
		this.map = map;
	}
}

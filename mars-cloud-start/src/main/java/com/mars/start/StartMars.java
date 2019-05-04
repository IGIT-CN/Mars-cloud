package com.mars.start;

import com.mars.base.BaseStartMars;
import com.mars.mybatis.init.InitJdbc;

/**
 * 启动Mars框架
 * @author yuye
 *
 */
public class StartMars {

	/**
	 * 启动Mars框架
	 * @param clazz 类
	 */
	public static void start(Class<?> clazz) {
		BaseStartMars.start(clazz,new InitJdbc());
	}
	
}

package com.yuyenews.start;

import com.yuyenews.easy.init.InitJdbc;

/**
 * 初始化扩展模块
 * 
 * @author yuye
 *
 */
public final class InitExtends {


	/**
	 * 加载JDBC模块
	 */
	protected static void loadJdbc() {
		InitJdbc initJdbc = new InitJdbc();
		initJdbc.init();
	}

	/**
	 * 注册接口
	 */
	protected static void loadReg() {

	}
}

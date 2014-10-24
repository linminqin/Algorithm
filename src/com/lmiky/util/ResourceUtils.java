package com.lmiky.util;

import java.util.ResourceBundle;

/**
 * 配置文件工具类
 * @author lmiky
 * @date 2013-8-19
 */
public class ResourceUtils {
	private static String KEY_CONFIG = "config";

	/**
	 * 读取配置值
	 * @author lmiky
	 * @date 2013-8-19
	 * @param key 配置键
	 * @return
	 */
	public static String getConfigValue(String key) {
		return ResourceBundle.getBundle(KEY_CONFIG).getString(key);
	}
	
}

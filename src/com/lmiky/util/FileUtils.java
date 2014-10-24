package com.lmiky.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author lmiky
 * @date 2013-8-19
 */
public class FileUtils {

	/**
	 * 遍历获取指定下所有指定后缀的文件
	 * @author lmiky
	 * @date 2013-8-19
	 * @param file 要搜索的文件，如果是目录，则搜索目录下的所有文件
	 * @param files	保存结果文件的链表
	 * @param fileSubfix	指定后缀名
	 */
	public static void listFiles(File file, List<File> files, String fileSubfix) {
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				File f = subFiles[i];
				listFiles(f, files, fileSubfix);
			}
		} else {
			String fileName = file.getName();
			//后缀名不区分大小写
			if (fileName.toLowerCase().endsWith("." + fileSubfix.toLowerCase())) {
				if (files == null) {
					files = new ArrayList<File>();
				}
				files.add(file);
			}
		}
	}
}

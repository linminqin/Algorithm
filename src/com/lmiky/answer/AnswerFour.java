package com.lmiky.answer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmiky.util.FileUtils;
import com.lmiky.util.ResourceUtils;

/**
 * 第四个问题答案：程序支持多种语言的源代码行数统计，在config配置文件中配置多种语言注释解析格式
 * 因为我没接触过任何代码统计工具，所以不知道“package ...”，“import ...”等等算不算有效代码行数，我谷歌上搜了下，看了上面的例子好像都算入有效代码，所以本程序中也算入
 * 
 * 请设计一个程序：使用多线程，统计程序源代码行数；
 * 源代码是可以编译通过的合法的代码，统计其物理总行数、其中的空行行数、其中含有有效代码的行数、其中含有注释内容的行数；
 * (要求必须利用多线程编程，如果代码框架能更容易的扩展到支持多种语言的源代码行数统计，将获得更高的评价。)
 * @author lmiky
 * @date 2013-8-19
 */
public class AnswerFour {
	private static String CONFIGKEY_FILESUBFIX = "code.subfix";
	private static String CONFIGKEY_PREFIX_SINGLECOMMON = "code.comment.single.";
	private static String CONFIGKEY_PREFIX_MULTICOMMON = "code.comment.multi.";
	
	//一个线程读取几个文件
	private static int THREAD_BATCH_FILESIZE = 10;
	
	private static String subfixConfig;	//配置可解析的代码文件后缀，多个之间以","分割
	private static Map<String, List<String>> singleCommentCache;	//缓存单行注释格式
	private static Map<String, List<String[]>> multiCommentCache;	//缓存多行注释格式
	
	private long codeLines = 0;		//有效代码行数
	private long commentLines = 0;	//注释行
	private long blankLines = 0;	//空白行
	
	static {
		//读取需要解析的文件后缀
		subfixConfig = ResourceUtils.getConfigValue(CONFIGKEY_FILESUBFIX);
		//缓存单行、多行注释格式
		singleCommentCache = new HashMap<String, List<String>>();
		multiCommentCache = new HashMap<String, List<String[]>>();
		
		//循环解析配置
		String[] subfixes = subfixConfig.split(",");
		for(String subfix : subfixes) {
			String singleConfig = ResourceUtils.getConfigValue(CONFIGKEY_PREFIX_SINGLECOMMON + subfix);
			if(singleConfig != null && !singleConfig.trim().isEmpty()) {
				singleCommentCache.put(subfix, Arrays.asList(singleConfig.split(",")));
			}
			String multiConfig = ResourceUtils.getConfigValue(CONFIGKEY_PREFIX_MULTICOMMON + subfix);
			if(multiConfig != null && !multiConfig.trim().isEmpty()) {
				String[] configs = multiConfig.split(",");
				List<String[]> list = new ArrayList<String[]>();
				String[] strs = null;
				for(String str : configs) {
					//多行注释格式分注释头和注释尾
					strs = str.split("\\|");
					list.add(strs);
				}
				multiCommentCache.put(subfix, list);
			}
		}
	}
	
	/**
	 * 计算代码行
	 * @author lmiky
	 * @date 2013-8-19
	 * @param fileDirectoryPath 代码目录
	 */
	public void state(String fileDirectoryPath) {
		File file = new File(fileDirectoryPath);
		if(!file.exists() || !file.isDirectory()) {
			return;
		}
		//循环统计指定后缀的文件
		String[] subfixes = subfixConfig.split(",");
		for(String subfix : subfixes) {
			List<File> codeFiles = new ArrayList<File>();
			//获取所有文件
			FileUtils.listFiles(file, codeFiles, subfix);
			System.out.println("文件数：" + codeFiles.size());
			List<File> threadFiles = new ArrayList<File>();
			for(int i=0; i<codeFiles.size(); i++) {
				threadFiles.add(codeFiles.get(i));
				//如果满指定启动线程的文件数，或到文件列表尾，则启动线程统计
				if(((i+1) % THREAD_BATCH_FILESIZE) == 0 || i == codeFiles.size() - 1) {
					File[] files = new File[threadFiles.size()];
					new Thread(new StateCodeRunnable(threadFiles.toArray(files), subfix)).run();
					threadFiles.clear();
				}
			}
		}
		System.out.println("有效代码行数:" + codeLines);
		System.out.println("注释行数:" + commentLines);
		System.out.println("空白行数:" + blankLines);
		System.out.println("物理总行数:" + (codeLines + commentLines + blankLines));
	}
	
	/**
	 * 增加有效代码行数
	 * @author lmiky
	 * @date 2013-8-19
	 * @param lines 增加数
	 */
	private synchronized void addCodeLines(long lines) {
		codeLines = codeLines + lines;
	}
	
	/**
	 * 增加注释行数
	 * @author lmiky
	 * @date 2013-8-19
	 * @param lines 增加数
	 */
	private synchronized void addCommentLines(long lines) {
		commentLines = commentLines + lines;
	}
	
	/**
	 * 增加空白行数
	 * @author lmiky
	 * @date 2013-8-19
	 * @param lines 增加数
	 */
	private synchronized void addBlankLines(long lines) {
		blankLines = blankLines + lines;
	}
	
	/**
	 * 线程执行类
	 * @author lmiky
	 *
	 */
	private class StateCodeRunnable implements Runnable {
		private File[] codeFiles;
		private String subfix;
		private long tCodeLines = 0;		//有效代码行数
		private long tCommentLines = 0;	//注释行
		private long tBlankLines = 0;	//空白行
		public StateCodeRunnable(File[] codeFiles, String subfix) {
			this.codeFiles = codeFiles;
			this.subfix = subfix;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			state(this.codeFiles , singleCommentCache.get(subfix), multiCommentCache.get(subfix));
		}
		
		/**
		 * 统计文件列表代码数
		 * @author lmiky
		 * @date 2013-8-19
		 * @param codeFiles	文件列表
		 * @param singleComment	单行注释格式
		 * @param muiltComment	多行注释格式
		 */
		private void state(File[] codeFiles, List<String> singleComment, List<String[]> muiltComment) {
			for(File codeFile : codeFiles) {
				state(codeFile, singleComment, muiltComment);
			}
			//批量增加
			addCodeLines(tCodeLines);
			addCommentLines(tCommentLines);
			addBlankLines(tBlankLines);
		}
		
		/**
		 * 统计单个文件代码数
		 * @author lmiky
		 * @date 2013-8-19
		 * @param codeFile	代码文件
		 * @param singleComment	单行注释格式
		 * @param muiltComment	多行注释格式
		 */
		private void state(File codeFile, List<String> singleComment, List<String[]> muiltComment) {
			BufferedReader br = null;
			boolean comment = false;
			boolean isContinue = false;
			try {
				br = new BufferedReader(new FileReader(codeFile));
				String line = "";
				//读取文件内容
				while((line = br.readLine()) != null) {
					isContinue = false;
					line = line.trim();
					//空白行
					if(line.matches("^[\\s&&[^\\n]]*$")) {
						tBlankLines++;
						continue;
					}
					//循环检查是否单行注释
					for(String singleCommentStr : singleComment) {
						if (line.startsWith(singleCommentStr)) {
							tCommentLines++;
							isContinue = true;
							break;
						}
					}
					if(isContinue) {
						continue;
					}
					//如果当前行是被包含在注释中
					if (true == comment) {
						tCommentLines++;
						isContinue = true;
					}
					//循环检查是否多行注释
					for(String[] muiltCommentStr : muiltComment) {
						if (line.startsWith(muiltCommentStr[0]) && !line.endsWith(muiltCommentStr[1])) {
							tCommentLines++;
							isContinue = true;
							comment = true;	
							break;
						} else if (line.startsWith(muiltCommentStr[0]) && line.endsWith(muiltCommentStr[1])) {//以多行注释的格式来注释单行
							tCommentLines++;
							isContinue = true;
							break;
						} else if (true == comment) {
							//多行注释的结束
							if(line.endsWith(muiltCommentStr[1])) {
								comment = false;
							}
							break;
						}
					}
					if(isContinue) {
						continue;
					}
					//剩余的就是有效代码行
					tCodeLines++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(br != null) {
					try {
						br.close();
						br = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new AnswerFour().state("D:\\LWorkSpace\\lmiky");
	}

}

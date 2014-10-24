package com.lmiky.answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 第三个问题答案
 * 
 * 请实现一个函数：最长顺子；输入很多个整数(1<=数值<=13)，返回其中可能组成的最长的一个顺子(顺子中数的个数代表顺的长度)； 
 * 其中数字1也可以代表14； 顺子包括单顺\双顺\3顺；
 * 单顺的定义是连续5个及以上连续的数，比如1,2,3,4,5、3,4,5,6,7,8和10,11,12,13,1等；
 * 双顺的定义是连续3个及以上连续的对(对:两个相同的数被称为对),比如1,1,2,2,3,3、4,4,5,5,6,6,7,7和11,11,12,12,13,13,1,1等;
 * 3顺的定义是连续2个及以上连续的3张(3张:3个相同的数被称为3张),比如1,1,1,2,2,2、3,3,3,4,4,4,5,5,5,6,6,6和13,13,13,1,1,1等等;
 * @author lmiky
 * @date 2013-8-19
 */
public class AnswerThree {
	private static final int NUMBER_MIN = 1;	//最小数
	private static final int NUMBER_MAX = 13;	//最大数
	
	/**
	 * 计算可能的最长的顺子
	 * @author lmiky
	 * @date 2013-8-19
	 * @param params	输入的数字
	 * @return	最长的顺子,如果没有顺子，则返回空数组
	 */
	public static int[] compute(int ...params) {
		if(params == null || params.length == 0) {
			return new int[0];
		}
		//保存每个数字出现的数次
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0; i<params.length; i++) {
			if(map.get(params[i] + "") != null) {
				map.put(params[i] + "", map.get(params[i] + "") + 1);
			} else {
				map.put(params[i] + "", 1);
			}
		}
		//分别获取单顺、双顺、三顺的最长顺子
		int[] maxSingle = getMax(map, 1, 5);
		int[] maxDouble = getMax(map, 2, 3);
		int[] maxTree = getMax(map, 3, 2);
		//获取三种顺子中最长的顺子
		if(maxSingle.length >= maxDouble.length) {
			if(maxSingle.length >= maxTree.length) {
				return maxSingle;
			} else {
				return maxTree;
			}
		} else {
			if(maxDouble.length >= maxTree.length) {
				return maxDouble;
			} else {
				return maxTree;
			}
		}
	}
	
	/**
	 * 获取指定顺子类别的最长顺子
	 * @author lmiky
	 * @date 2013-8-19
	 * @param countMap	保存每个数字出现次数,格式为<数字, 次数>
	 * @param numberMinAppear	数字最少出现的次数，即要求的顺子类别，例如如果为单顺，则值为1，双顺为2
	 * @param numberMinContinue	指定顺子类别要求最少连续的长度，如单顺，则值为5
	 * @return
	 */
	private static int[] getMax(Map<String, Integer> countMap, int numberMinAppear, int numberMinContinue) {
		List<Integer> sortedList = new ArrayList<Integer>();
		Set<String> mapKey = countMap.keySet();
		//根据出现的次数，获取符合顺子类别的数
		for(Iterator<String> ite = mapKey.iterator(); ite.hasNext();) {
			String key = ite.next();
			if(countMap.get(key) >= numberMinAppear) {
				sortedList.add(Integer.parseInt(key));
			}
		}
		//按从小到大排序
		Collections.sort(sortedList);
		//如果有最小值,可以代表有最大的额外值
		if(sortedList.get(0) == NUMBER_MIN) {
			sortedList.add(NUMBER_MAX + 1);
		}
		
		int count = 1;
		int currentBegin = 1;	//计算过程中当前出现顺子的起始值
		int max = 0;	//最大顺子数
		int begin = 1;	//最大顺子数的起始值

		for(int i=1; i<sortedList.size(); i++) {
			if(sortedList.get(i) - sortedList.get(i-1) == 1) {
				count++;
				//如果是顺子开始位置，即刚出现两个连续的数字
				if(count == 2) {
					currentBegin = sortedList.get(i - 1);
				}
				//如果当前的顺子长度大于保存的历史最大长度
				if(count > max) {
					max = count;
					begin = currentBegin;
				}
			} else {
				count = 1;
			}
		}
		//如果存在顺子
		if(max >= numberMinContinue) {
			int[] ret = new int[max * numberMinAppear];
			for(int i=0; i<max; i++) {
				ret[i*numberMinAppear] = begin + i;
				if(ret[i*numberMinAppear] > NUMBER_MAX) {
					ret[i*numberMinAppear] = NUMBER_MIN;
				}
				for(int j = 1; j<numberMinAppear; j++) {
					ret[i*numberMinAppear + j] = ret[i*numberMinAppear];
				}
			}
			return ret;
		} else {
			return new int[0];
		}
	}

	public static void main(String[] args) {
		int[] params = new int[20];
		params[0] = 3;
		params[1] = 5;
		params[2] = 4;
		params[3] = 9;
		params[4] = 1;
		params[5] = 6;
		params[6] = 7;
		params[7] = 9;
		params[8] = 10;
		params[9] = 11;
		params[10] = 12;
		params[11] = 13;
		params[12] = 1;
		params[13] = 12;
		params[14] = 13;
		params[15] = 11;
		params[16] = 9;
		params[17] = 11;
		params[18] = 10;
		params[19] = 10;
		System.out.print("输入值：");
		for(int i=0; i<params.length; i++) {
			System.out.print(params[i]);
			if(i<params.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		int[] ret = compute(params);
		System.out.print("结果：");
		for(int i=0; i<ret.length; i++) {
			System.out.print(ret[i]);
			if(i<ret.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		
		//=====================================
		
		params = new int[14];
		params[0] = 1;
		params[1] = 5;
		params[2] = 2;
		params[3] = 3;
		params[4] = 4;
		params[5] = 4;
		params[6] = 5;
		params[7] = 9;
		params[8] = 6;
		params[9] = 7;
		params[10] = 2;
		params[11] = 3;
		params[12] = 3;
		params[13] = 4;
		System.out.print("输入值（文档例子）：");
		for(int i=0; i<params.length; i++) {
			System.out.print(params[i]);
			if(i<params.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		ret = compute(params);
		System.out.print("结果：");
		for(int i=0; i<ret.length; i++) {
			System.out.print(ret[i]);
			if(i<ret.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
	}

}

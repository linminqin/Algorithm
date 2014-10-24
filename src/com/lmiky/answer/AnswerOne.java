package com.lmiky.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 第一个问题的答案
 * 
 * 请实现一个函数：凑14；输入很多个整数(1<=数值<=13),任意两个数相加等于14就可以从数组中删除这两个数，
 * 求剩余数(按由小到大排列)；比如： 输入数组[9,1,9,7,5,13], 输出数组[7,9]
 * @author lmiky
 * @date 2013-8-19
 */
public class AnswerOne {
	
	/**
	 * 移除相加结果等于指定的值的整数
	 * @author lmiky
	 * @date 2013-8-19
	 * @param sum 相加凑的和
	 * @param params 相加的整数
	 * @return 剩余的整数所组成的数组
	 */
	public static int[] compute(int sum, int... params) {
		if (params == null || params.length == 0) {
			return new int[0];
		}
		//从小到大排序
		Arrays.sort(params);
		//排序后构建链表数组
		List<Integer> list = new ArrayList<Integer>();
		for (int i : params) {
			list.add(i);
		}
		int index = 0;
		int listSize = 0;	//实时链表长度
		//从小到大开始匹配值
		for (int i = 0; i < list.size(); i++) {
			//如果当前值大于指定和的一半，则说明接下来的整数都不需要移除
			if (list.get(i) > sum / 2) {
				break;
			}
			index = 0;
			listSize = list.size() - 1;
			//由于数组从小到大排序过且从第一个开始，所以优先和后面最大的数据开始倒序匹配
			while(listSize - index > i) {
				//如果被匹配的值小于需要匹配的值，或者位置小于等于当前匹配的值的位置
				if (sum - list.get(i) > list.get(listSize - index) || i >= (listSize-index)) {
					break;
				}
				//如果值匹配,则移除数据
				if (sum - list.get(i) == list.get(listSize - index)) {
					list.remove(listSize - index);
					list.remove(i);
					i--;//原先的数据位置被往前移动了一位
					break;
				}
				index++;
			}
		}
		int[] ret = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		int[] params = new int[10];
		params[0] = 2;
		params[1] = 1;
		params[2] = 12;
		params[3] = 13;
		params[4] = 1;
		params[5] = 9;
		params[6] = 5;
		params[7] = 8;
		params[8] = 8;
		params[9] = 12;
		System.out.print("输入值：");
		for (int i=0; i<params.length; i++) {
			System.out.print(params[i]);
			if(i < params.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		int[] ret = compute(14, params);
		System.out.print("结果：");
		for (int i=0; i<ret.length; i++) {
			System.out.print(ret[i]);
			if(i < ret.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		
		//=====================================
		
		params = new int[14];
		params[0] = 2;
		params[1] = 1;
		params[2] = 12;
		params[3] = 13;
		params[4] = 1;
		params[5] = 9;
		params[6] = 10;
		params[7] = 8;
		params[8] = 4;
		params[9] = 12;
		params[10] = 7;
		params[11] = 6;
		params[12] = 8;
		params[13] = 12;
		System.out.print("输入值：");
		for (int i=0; i<params.length; i++) {
			System.out.print(params[i]);
			if(i < params.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		ret = compute(14, params);
		System.out.print("结果：");
		for (int i=0; i<ret.length; i++) {
			System.out.print(ret[i]);
			if(i < ret.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		
		//=====================================
		
		params = new int[6];
		params[0] = 9;
		params[1] = 1;
		params[2] = 9;
		params[3] = 7;
		params[4] = 5;
		params[5] = 13;
		System.out.print("输入值（文档例子）：");
		for (int i=0; i<params.length; i++) {
			System.out.print(params[i]);
			if(i < params.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
		ret = compute(14, params);
		System.out.print("结果：");
		for (int i=0; i<ret.length; i++) {
			System.out.print(ret[i]);
			if(i < ret.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println();
	}
}

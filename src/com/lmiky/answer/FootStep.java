package com.lmiky.answer;

import java.util.Arrays;

/**
 * 一个台阶总共有n级，如果一次可以跳1级，也可以跳2级，求总共有多少种跳法
 * @author lmiky
 * @date 2014年10月24日 下午3:51:27
 */
public class FootStep {
	
	/**
	 * 说明
	 * @author lmiky
	 * @date 2014年10月24日 下午3:48:00
	 * @param currentStep
	 * @param totalStep
	 * @param allowStep 允许的步伐，按从小到大排序
	 * @return
	 */
	private static int compute(int currentStep, int totalStep, int[] allowStep) {
		int compute = 0;
		for(int i=0; i<allowStep.length; i++) {
			if(currentStep + allowStep[i] < totalStep) {
				compute += compute(currentStep + allowStep[i], totalStep, allowStep);
			} else if(currentStep + allowStep[i] ==totalStep) {
				compute++;
			} else {
				break;
			}
		}
		return compute;
	}
	
	/**
	 * 说明
	 * @author lmiky
	 * @date 2014年10月24日 下午3:51:24
	 * @param totalStep
	 * @param allowStep
	 * @return
	 */
	public static int compute(int totalStep, int[] allowStep) {
		int[] newAllowStep = new int[allowStep.length];
		for(int i=0; i<allowStep.length; i++) {
			newAllowStep[i] = allowStep[i];
		}
		Arrays.sort(newAllowStep);
		return compute(0, totalStep, newAllowStep);
	}

	public static void main(String[] args) {
		System.out.println(FootStep.compute(5, new int[]{1, 2}));
	}

}

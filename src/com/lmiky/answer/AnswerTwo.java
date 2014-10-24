package com.lmiky.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lmiky.model.Line;


/**
 * 第二个问题答案
 * 
 * 请实现一个函数：线段重叠; 输入多个一维线段,求出这些线段相交的所有区域(也用线段表示);  
 * 一条线段用两个值表示(x0,x1), 其中x1>x0; 
 * 比如： 输入线段数组[(2,4),(1.5,6),(0.5,3.5),(5,7),(7.5,9)], 输出线段数组[(1.5,4),(5,6)]
 * @author lmiky
 * @date 2013-8-19
 */
public class AnswerTwo {
	private static LineComparator lineComparator = new LineComparator();

	/**
	 * 计算输入现场重叠的区间线段
	 * @author lmiky
	 * @date 2013-8-19
	 * @param params 要计算的线段列表
	 * @return 重叠的区间线段
	 */
	public static Line[] compute(Line ... params) {
		if (params == null || params.length == 0) {
			return new Line[0];
		}
		List<Line> list = Arrays.asList(params);
		Collections.sort(list, lineComparator);
		Line line = null;
		List<Line> ret = new ArrayList<Line>();
		//取所有的交叉区域
		for(int i=0; i<list.size()-1; i++) {
			for(int j=i+1; j<list.size(); j++) {
				//被比较线的起点大于比较线的尾点
				if(list.get(j).getBeginPoint() > list.get(i).getEndPoint()) {
					break;
				}
				//被比较线的尾点大于比较点的起点
				if(list.get(j).getEndPoint() > list.get(i).getEndPoint()) {
					line = new Line(list.get(j).getBeginPoint(), list.get(i).getEndPoint());
					if(!ret.contains(line)) {
						ret.add(line);
					}
				} else {
					line = new Line(list.get(j).getBeginPoint(), list.get(j).getEndPoint());
					if(!ret.contains(line)) {
						ret.add(line);
					}
				}
			}
		}
		ret = regroup(ret);
		return ret.toArray(new Line[ret.size()]);
	}
	
	/**
	 * 重组线段，将交叉的线段重拼成新的线段
	 * @author lmiky
	 * @date 2013-8-19
	 * @param lines 重组前的线段列表
	 * @return 重组后的线段列表，线段之间没有交叉
	 */
	private static List<Line> regroup(List<Line> lines) { 
		if(lines.isEmpty()) {
			return lines;
		}
		List<Line> regroupLines = new ArrayList<Line>();
		Collections.sort(lines, lineComparator);
		Line line = lines.get(0);
		for(int i=1; i<lines.size(); i++) {
			//如果没有相交
			if(lines.get(i).getBeginPoint() > line.getEndPoint()) {
				regroupLines.add(new Line(line.getBeginPoint(), line.getEndPoint()));
				line = lines.get(i);
				continue;
			}
			//如果有相交，则延长线段
			if(lines.get(i).getEndPoint() >= line.getEndPoint()) {
				line.setEndPoint(lines.get(i).getEndPoint());
			}
		}
		regroupLines.add(line);
		return regroupLines;
	}
	
	/**
	 * 线段排序
	 * 从小到大排序，优先比较起点，如果起点相等，再比较尾点
	 * @author lmiky
	 *
	 */
	private static class LineComparator implements Comparator<Line> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Line arg0, Line arg1) {
			if(arg0.equals(arg1)) {
				return 0;
			}
			//先比较七点
			if(arg0.getBeginPoint() > arg1.getBeginPoint()) {
				return 1;
			} else if(arg0.getBeginPoint() < arg1.getBeginPoint()) {
				return -1;
			} else {
				//再比较尾点
				if(arg0.getEndPoint() > arg1.getEndPoint()) {
					return 1;
				} else {
					return -1;
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		Line[] lines = new Line[10];
		lines[0] = new Line(1.0, 3.3);
		lines[1] = new Line(3.0, 3.3);
		lines[2] = new Line(3.3, 5.3);
		lines[3] = new Line(3.3, 4.1);
		lines[4] = new Line(1.2, 1.3);
		lines[5] = new Line(1.0, 2.3);
		lines[6] = new Line(3.6, 4.1);
		lines[7] = new Line(7.0, 8.7);
		lines[8] = new Line(2.4, 5.2);
		lines[9] = new Line(3.4, 6.1);
		System.out.print("输入值：");
		for(int i=0; i<lines.length; i++) {
			Line line = lines[i];
			System.out.print("(" + line.getBeginPoint() + ", " + line.getEndPoint() + ")");
			if(i < lines.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
		Line[] retLines = compute(lines);
		System.out.print("结果：");
		for(int i=0; i<retLines.length; i++) {
			Line line = retLines[i];
			System.out.print("(" + line.getBeginPoint() + ", " + line.getEndPoint() + ")");
			if(i < retLines.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
		
		//=====================================
		
		lines = new Line[5];
		lines[0] = new Line(2, 4);
		lines[1] = new Line(1.5, 6);
		lines[2] = new Line(0.5, 3.5);
		lines[3] = new Line(5, 7);
		lines[4] = new Line(7.5, 9);
		System.out.print("输入值（文档例子）：");
		for(int i=0; i<lines.length; i++) {
			Line line = lines[i];
			System.out.print("(" + line.getBeginPoint() + ", " + line.getEndPoint() + ")");
			if(i < lines.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
		retLines = compute(lines);
		System.out.print("结果：");
		for(int i=0; i<retLines.length; i++) {
			Line line = retLines[i];
			System.out.print("(" + line.getBeginPoint() + ", " + line.getEndPoint() + ")");
			if(i < retLines.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}

}

package com.lmiky.model;

import java.io.Serializable;

/**
 * 线段
 * @author lmiky
 * @date 2013-8-19
 */
public class Line implements Serializable {
	private static final long serialVersionUID = 8578555105630222827L;
	
	private double beginPoint;	//起点
	private double endPoint;	//尾点
	
	/**
	 * 
	 */
	public Line() {
		
	}
	
	/**
	 * @param beginPoint
	 * @param endPoint
	 */
	public Line(double beginPoint, double endPoint) {
		this.beginPoint = beginPoint;
		this.endPoint = endPoint;
	}
	
	/**
	 * @return the beginPoint
	 */
	public double getBeginPoint() {
		return beginPoint;
	}

	/**
	 * @param beginPoint the beginPoint to set
	 */
	public void setBeginPoint(double beginPoint) {
		this.beginPoint = beginPoint;
	}

	/**
	 * @return the endPoint
	 */
	public double getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint the endPoint to set
	 */
	public void setEndPoint(double endPoint) {
		this.endPoint = endPoint;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		//比较对象
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final Line compareLine = (Line)obj;
		//比较属性
		if(!(beginPoint == compareLine.getBeginPoint())) {
			return false;
		}
		if(!(endPoint == compareLine.getEndPoint())) {
			return false;
		}
		return true;
	}
}

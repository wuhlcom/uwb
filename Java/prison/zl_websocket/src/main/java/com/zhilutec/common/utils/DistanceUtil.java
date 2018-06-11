package com.zhilutec.common.utils;

import java.util.HashMap;
import java.util.Map;

public class DistanceUtil {

	/* X为宽度，Y为长度,WALL为墙的厚度 */
	public static final double X = 2.675;//302
	public static final double X303 = 3.267;//303
	public static final double y = 8.84;
	public static final double WALL = 0.105;
	
	
	public final static Map<String, Double> distanceMap = new HashMap<String, Double>() {

		private static final long serialVersionUID = 1L;
	

		{
			/*x坐标减去墙和其它房间宽度得到x在每个房间坐标系的实际值**/
			//303 403
			double ex1000 = ArithUtil.add(X, ArithUtil.mul(WALL,1));
			//304
			double ex1100 =  ArithUtil.add(ArithUtil.mul(X,2),ArithUtil.mul(WALL,3));
			//205
			double ex1200 =  ArithUtil.add(ArithUtil.mul(X,3),ArithUtil.mul(WALL,4));
			put("0900", WALL);
			put("0901", WALL);
			put("1000", ex1000);
			put("1001", ex1000);
			put("1100", ex1100);
			put("1101", ex1100);
			put("1200", ex1200);
			put("1201", ex1200);
		}
	};

}

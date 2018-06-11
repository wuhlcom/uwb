package com.zhilutec.common.utils;

import java.util.HashMap;
import java.util.Map;

public class PositionUtil {
//	0100
//	0200
//	0300
//	0400
//	0500
//	0600
//	0700
//	0800  0801
//	0900  0901
//	1000  1001
//	1100  1101
//	1200  1201
//	1300  1301
//	1400  1401
//	1500  1501
//	1600  1601
//	1700  1701
//	1800  1801
//	1900  1901
	
	public final static String AREA1 = "活动室";
	public final static String AREA2 = "过厅B";
	public final static String AREA3 = "走廊";
	public final static String AREA4 = "过厅A";
	public final static String AREA5 = "谈话室";
	public final static String AREA6 = "更衣室";
	public final static String AREA7 = "阅览室";
	public final static String AREA8 = "监仓";
	
	public final static Map<String, String> positionMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;	

		{
			put("0100", AREA1);
			put("0200", AREA2);
			put("0300", AREA3);			
			put("0400", AREA4);
			put("0500", AREA5);
			put("0600", AREA6);			
			put("0700", AREA7);
			
			put("0800", AREA8);		
			put("0801", AREA8);	
			
			put("1000", AREA8);
			put("1001", AREA8);
			
			put("1100", AREA8);		
			put("1101", AREA8);			
			
			put("1200", AREA8);
			put("1201", AREA8);
			
			put("1300", AREA8);
			put("1301", AREA8);
			
			put("1400", AREA8);
			put("1401", AREA8);
			
			put("1500", AREA8);
			put("1501", AREA8);
			
			put("1600", AREA8);	
			put("1601", AREA8);	
			
			put("1700", AREA8);
			put("1701", AREA8);
			
			put("1800", AREA8);
			put("1801", AREA8);
			
			put("1900", AREA8);
			put("1901", AREA8);
		}
	};
}

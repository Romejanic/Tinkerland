package com.tinkerland.utils;

import java.util.ArrayList;

public class ArrayUtils {

	public static String joinList(ArrayList<String> list, String sep) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < list.size(); i++) {
			out.append(list.get(i));
			if(i < (list.size() - 1)) {
				out.append(sep);
			}
		}
		return out.toString();
	}
	
}
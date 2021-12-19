package com.example.util;

import java.text.Normalizer;

public class Utils {
	
	public static String adaptString (String str) {
		if(str==null) {
			return "";
		}
		str = stripAccents(str);
		return str.replaceAll(",", " ").replaceAll("-", " ").replaceAll("/", " ").toUpperCase();
	}
	
	
	public static String stripAccents(String s) 
	{
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}

}

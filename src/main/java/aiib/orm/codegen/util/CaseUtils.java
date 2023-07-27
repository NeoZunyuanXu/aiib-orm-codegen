/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.util;

/**
 * @ClassName: CaseUtils 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 27, 2023 10:24:45 AM	 
 */
public class CaseUtils {
	public static char[] NAME_DELIMETERS = new char[] {'_', '-'};
	
	public static String toCamelCase(String str, final boolean capitalizeFirstLetter) {
		return org.apache.commons.text.CaseUtils.toCamelCase(str, capitalizeFirstLetter, NAME_DELIMETERS);
	}
	
	public static String toCamelCase(String str) {
		return toCamelCase(str, true);
	}
}

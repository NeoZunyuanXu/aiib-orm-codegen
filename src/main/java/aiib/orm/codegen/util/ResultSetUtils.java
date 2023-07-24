/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: ResultSetUtils 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 6:26:00 PM	 
 */
public final class ResultSetUtils {
	public static String getString(ResultSet rs, String columnName) {
		try {			
			return rs.getString(columnName);
			
		} catch (SQLException e) {	
			
			return null;			
		}		
	}
	
	public static Integer getInteger(ResultSet rs, String columnName) {
		try {		
			return rs.getInt(columnName);
			
		} catch (SQLException e) {	
			
			return null;			
		}		
	}
	
	public static Short getShort(ResultSet rs, String columnName) {
		try {		
			return rs.getShort(columnName);
			
		} catch (SQLException e) {	
			
			return null;			
		}		
	}
}

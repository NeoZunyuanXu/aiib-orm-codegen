/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.type;

import lombok.Data;

/**
 * @ClassName: FullyQuanlifiedJavaType 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 25, 2023 4:24:43 PM	 
 */
@Data
public class FullyQualifiedJavaType {
	private boolean isArray;
	
	private String fullName;
	
	private String shortName;
	
	private String packageName;
	
	private boolean explicitImported;
	
	public FullyQualifiedJavaType(String spec) {
		parse(spec);
	}
	
	private void parse(String spec) {
		fullName = spec.trim();
		
		if (fullName.contains(".")) {
			packageName = fullName.substring(0, fullName.lastIndexOf('.'));
			shortName = fullName.substring(packageName.length() + 1);
			explicitImported = "java.lang".equals(packageName);
			
		} else {
			packageName = "";
			shortName  = fullName;
			explicitImported = false;
		}
		
		isArray = fullName.endsWith("]");
	}

}

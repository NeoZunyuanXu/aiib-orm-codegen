/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.type;

import aiib.orm.codegen.db.ColumnMeta;

/**
 * @ClassName: JavaTypeResolver 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 25, 2023 5:13:33 PM	 
 */
public interface JavaTypeResolver {
	FullyQualifiedJavaType resolveColumnType(ColumnMeta columnMeta);
}

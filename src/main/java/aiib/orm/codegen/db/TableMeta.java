/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

/**
 * @ClassName: TableMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 4:34:42 PM	 
 */
public record TableMeta(
		String tableName,
		String tableCat,
		String tableSchem, 
		String tableType,
		String remarks,
		String typeCat, 
		String typeSchem, 
		String typeName,
		String selfReferencingColName,
		String refGeneration){
	
}

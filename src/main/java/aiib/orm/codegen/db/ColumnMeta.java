/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import lombok.Data;

/**
 * @ClassName: ColumnMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 24; 2023 1:32:09 PM	 
 */
@Data	
public class ColumnMeta {
	private String tableCat;
	private String tableSchem;
	private String tableName;
	private String columnName;
	private Integer dataType;
	private String typeName;
	private Integer columnSize;
	private Integer decimalDigits;
	private Integer numPrecRadix;
	private Integer nullable;
	private String remarks;
	private String columnDef;
	private Integer charOctetLength;
	private Integer ordinalPosition;
	private String isNullable;
	private String scopeCatalog;
	private String scopeSchema;
	private String scopeTable;
	private Short sourceDataType;
	private String isAutoIncrement;
	private String isGeneratedColumn;
}

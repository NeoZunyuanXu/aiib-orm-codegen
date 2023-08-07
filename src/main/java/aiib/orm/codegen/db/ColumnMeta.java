/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import aiib.orm.codegen.type.FullyQualifiedJavaType;
import aiib.orm.codegen.util.CaseUtils;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName: ColumnMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 24; 2023 1:32:09 PM	 
 */
@Data	
@ToString(exclude = {"tableMeta"})
public class ColumnMeta {
	private String tableCatalog;
	private String tableSchema;
	private String tableName;	
	private String name;
	private int jdbcType;
	private String typeName;
	private int length;
	private int scale;
	private boolean nullable;
	private String remarks;
	private String defaultValue;
	private Integer ordinalPosition;
	private boolean isAutoIncrement;
	private boolean isGeneratedColumn;
		
	private FullyQualifiedJavaType javaType;
	
	private boolean isPrimaryKey = false;
	
	private boolean isGeneratedKey = false;
	
	private String generatedKeyType;
	
	private String generatedKeyParameters;

	private TableMeta tableMeta; 		
	
	public String getCapitalizedCamelCaseName() {
		return CaseUtils.toCamelCase(name, true);
	}
	
	public String getUncapitalizedCamelCaseName() {
		return CaseUtils.toCamelCase(name, false);
	}

}

/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @ClassName: TableMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 4:34:42 PM	 
 */
@Data
public class TableMeta {
	private String tableName;
	private String tableCat;
	private String tableSchem; 
	private String tableType;
	private String remarks;
	private String typeCat; 
	private String typeSchem; 
	private String typeName;
	private String selfReferencingColName;
	private String refGeneration;
	
	@Setter(AccessLevel.NONE)
	private List<ColumnMeta> primaryKeyColumns = new ArrayList<ColumnMeta>(16);
	
	public void addPrimaryKeyColumn(ColumnMeta columnMeta) {
		primaryKeyColumns.add(columnMeta);
	}
	
	public void addPrimaryKeyColumns(List<ColumnMeta> columnMetas) {
		primaryKeyColumns.addAll(columnMetas);
	}
	
	@Setter(AccessLevel.NONE)
	private List<ColumnMeta> ordinaryColumns = new ArrayList<ColumnMeta>(64);
	
	public void addOridaryColumn(ColumnMeta columnMeta) {
		ordinaryColumns.add(columnMeta);
	}
	
	public void addOrdinaryColumns(List<ColumnMeta> columnMetas) {
		ordinaryColumns.addAll(columnMetas);
	}
}

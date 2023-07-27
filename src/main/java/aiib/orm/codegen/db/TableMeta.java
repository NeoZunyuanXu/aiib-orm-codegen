/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import aiib.orm.codegen.Context;
import aiib.orm.codegen.util.CaseUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName: TableMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 4:34:42 PM	 
 */
@Data
@ToString(exclude = {"context"})
public class TableMeta {
	private String name;
	private String catalog;
	private String schema;	
	private String type;	// TABLE || VIEW	
	private String remarks;
	private String naming;
	
	private Context context;
	
	public boolean isTable() 
	{
		return "TABLE".equals(type);
	}
	
	public boolean isView() {
		return "VIEW".equals(type);
	}
	
	public String getCapitalizedCamelCaseName() {
		return CaseUtils.toCamelCase(name, true);
	}
	
	public String getUncapitalizedCamelCaseName() {
		return CaseUtils.toCamelCase(name, false);
	}
	
	public String getUncapitalizedNaming() {
		return StringUtils.uncapitalize(naming);
	}
	
	@Setter(AccessLevel.NONE)
	private List<ColumnMeta> primaryKeyColumns = new ArrayList<ColumnMeta>(16);
	
	public void addPrimaryKeyColumn(ColumnMeta columnMeta) {
		columnMeta.setTableMeta(this);
		columnMeta.setPrimaryKey(true);
		
		primaryKeyColumns.add(columnMeta);
	}
	
	public void addPrimaryKeyColumns(List<ColumnMeta> columnMetas) {
		columnMetas.forEach(e -> {
			e.setTableMeta(this);
			e.setPrimaryKey(true);
		});
		
		primaryKeyColumns.addAll(columnMetas);
	}
	
	@Setter(AccessLevel.NONE)
	private List<ColumnMeta> ordinaryColumns = new ArrayList<ColumnMeta>(64);
	
	public void addOridaryColumn(ColumnMeta columnMeta) {
		columnMeta.setTableMeta(this);
		
		ordinaryColumns.add(columnMeta);
	}
	
	public void addOrdinaryColumns(List<ColumnMeta> columnMetas) {
		columnMetas.forEach(e -> e.setTableMeta(this));
		
		ordinaryColumns.addAll(columnMetas);
	}
	
	public List<ColumnMeta> getColumns() {
		return Stream.concat(primaryKeyColumns.stream(), ordinaryColumns.stream()).toList();
	}
	
	public ColumnMeta getColumn(String name) {
		for (var column : primaryKeyColumns) {
			if (name.equals(column.getName())) return column;
		}
		
		for (var column : ordinaryColumns) {
			if (name.equals(column.getName())) return column;
		}
		
		return null;
	}
	
	@Setter(AccessLevel.NONE)
	private Set<String> explicitImportedJavaTypes = new HashSet<>(32);
	
	public void addExplicitImportedJavaType(String javaType) {
		explicitImportedJavaTypes.add(javaType);
	}
	
	public void addExplicitImportedJavaTypes(List<String> javaTypes) {
		explicitImportedJavaTypes.addAll(javaTypes);
	}
}

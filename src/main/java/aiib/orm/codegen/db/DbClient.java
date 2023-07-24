/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import aiib.orm.codegen.config.GeneratorConfiguration.DbType;
import aiib.orm.codegen.config.GeneratorConfiguration.JdbcConnection;
import aiib.orm.codegen.util.ResultSetUtils;

/**
 * @ClassName: DbClient
 * @Desc: A brief desc to the class function.
 * @Author: zunyuan.xu
 * @Date: Jul 20, 2023 3:42:02 PM
 */
public class DbClient implements AutoCloseable {

	private final String[] IN_SCOPE_TABLE_TYPES = new String[] { "TABLE", "VIEW" };

	private Connection connection;
	private DatabaseMetaData meta;

	@SuppressWarnings("unused")
	private DbClient() {
	}

	public DbClient(DbType dbType, JdbcConnection jdbcConnection) throws SQLException {
		connection = DriverManager.getConnection(jdbcConnection.getConnectionURL(), jdbcConnection.getUserId(),
				jdbcConnection.getPassword());

		meta = connection.getMetaData();
	}

	public List<TableMeta> getTableMetas(String catalog, String schema, String tableName) throws SQLException {
		var rsTable = meta.getTables(
				StringUtils.isEmpty(catalog) ? connection.getCatalog() : catalog,
				StringUtils.isEmpty(schema) ? connection.getSchema() : schema, 
				tableName, 
				IN_SCOPE_TABLE_TYPES);
		
		var columns = getColumnMetas(
				StringUtils.isEmpty(catalog) ? catalog : connection.getCatalog(),
				StringUtils.isEmpty(schema) ? connection.getSchema() : schema, 
				tableName);
		
		var result = new ArrayList<TableMeta>(256);
		
		while (rsTable.next()) {
			var table = convertTableMeta(rsTable);
			
			result.add(table);
			
			var tableColumns = new ArrayList<>(columns.stream().filter(e -> 
					StringUtils.equals(table.getTableCat(), e.getTableCat()) &&
					StringUtils.equals(table.getTableSchem(), e.getTableSchem()) &&
					StringUtils.equals(table.getTableName(), e.getTableName())).toList());

			var primaryKeys = getPrimaryKeyMetas(table.getTableCat(), table.getTableSchem(), table.getTableName());
			
			for (var primaryKey : primaryKeys) {
				var primaryKeyColumn = tableColumns.stream()
						.filter(e -> e.getColumnName().equals(primaryKey.getColumnName()))	
						.findFirst();
				
				if (primaryKeyColumn.isPresent()) {
					table.addPrimaryKeyColumn(primaryKeyColumn.get());
					tableColumns.remove(primaryKeyColumn.get());
				}				
			}
			
			table.addOrdinaryColumns(tableColumns);
		}

		return result;
	}
	
	protected List<ColumnMeta> getColumnMetas(String catalog, String schema, String tableName) throws SQLException {
		var rsColumn = meta.getColumns(
				StringUtils.isEmpty(catalog) ? connection.getCatalog() : catalog,
				StringUtils.isEmpty(schema) ? connection.getSchema() : schema, 
				tableName, 
				"%");
		
		var result = new ArrayList<ColumnMeta>(256 * 64);
		
		while (rsColumn.next()) {
			result.add(convertColumnMeta(rsColumn));
		}
		
		return result;
	}
	
	protected List<PrimaryKeyMeta> getPrimaryKeyMetas(String catalog, String schema, String tableName) throws SQLException {
		var result = new ArrayList<PrimaryKeyMeta>(16);
		
		var rsPrimaryKey = meta.getPrimaryKeys(catalog, schema, tableName);
		
		while (rsPrimaryKey.next()) {
			result.add(convertPrimaryKeyMeta(rsPrimaryKey));
		}
		
		result.sort((a, b) -> a.getKeySeq().compareTo(b.getKeySeq()));
		
		return result;
	}

	protected TableMeta convertTableMeta(ResultSet rs) throws SQLException {
		var result = new TableMeta();
		
		result.setTableName(ResultSetUtils.getString(rs, "TABLE_NAME"));
		result.setTableCat(ResultSetUtils.getString(rs, "TABLE_CAT"));
		result.setTableSchem(ResultSetUtils.getString(rs, "TABLE_SCHEM"));
		result.setTableType(ResultSetUtils.getString(rs, "TABLE_TYPE"));
		result.setTypeName(ResultSetUtils.getString(rs, "TYPE_NAME"));
		result.setTypeCat(ResultSetUtils.getString(rs, "TYPE_CAT"));
		result.setTypeSchem(ResultSetUtils.getString(rs, "TYPE_SCHEM"));
		result.setRemarks(ResultSetUtils.getString(rs, "REMARKS"));
		result.setSelfReferencingColName(ResultSetUtils.getString(rs, "SELF_REFERENCING_COL_NAME"));
		result.setRefGeneration(ResultSetUtils.getString(rs, "REF_GENERATION"));
				
		return result;
	}
	
	protected ColumnMeta convertColumnMeta(ResultSet rs) throws SQLException {
		var result = new ColumnMeta();
		
		result.setTableCat(ResultSetUtils.getString(rs, "TABLE_CAT"));
		result.setTableSchem(ResultSetUtils.getString(rs, "TABLE_SCHEM"));
		result.setTableName(ResultSetUtils.getString(rs, "TABLE_NAME"));
		result.setColumnName(ResultSetUtils.getString(rs, "COLUMN_NAME"));
		result.setDataType(ResultSetUtils.getInteger(rs, "DATA_TYPE"));
		result.setTypeName(ResultSetUtils.getString(rs, "TYPE_NAME"));
		result.setColumnSize(ResultSetUtils.getInteger(rs, "COLUMN_SIZE"));
		result.setDecimalDigits(ResultSetUtils.getInteger(rs, "DECIMAL_DIGITS"));
		result.setNumPrecRadix(ResultSetUtils.getInteger(rs, "NUM_PREC_RADIX"));
		result.setNullable(ResultSetUtils.getInteger(rs, "NULLABLE"));
		result.setRemarks(ResultSetUtils.getString(rs, "REMARKS"));
		result.setColumnDef(ResultSetUtils.getString(rs, "COLUMN_DEF"));
		result.setCharOctetLength(ResultSetUtils.getInteger(rs, "CHAR_OCTET_LENGTH"));
		result.setOrdinalPosition(ResultSetUtils.getInteger(rs, "ORDINAL_POSITION"));
		result.setIsNullable(ResultSetUtils.getString(rs, "IS_NULLABLE"));
		result.setScopeCatalog(ResultSetUtils.getString(rs, "SCOPE_CATALOG"));
		result.setScopeSchema(ResultSetUtils.getString(rs, "SCOPE_SCHEMA"));
		result.setScopeTable(ResultSetUtils.getString(rs, "SCOPE_TABLE"));
		result.setSourceDataType(ResultSetUtils.getShort(rs, "SOURCE_DATA_TYPE"));
		result.setIsAutoIncrement(ResultSetUtils.getString(rs, "IS_AUTOINCREMENT"));
		result.setIsGeneratedColumn(ResultSetUtils.getString(rs, "IS_GENERATEDCOLUMN"));
		
		return result;		
	}
	
	protected PrimaryKeyMeta convertPrimaryKeyMeta(ResultSet rs) throws SQLException {
		var result = new PrimaryKeyMeta();
		
		result.setTableCat(ResultSetUtils.getString(rs, "TABLE_CAT"));
		result.setTableSchem(ResultSetUtils.getString(rs, "TABLE_SCHEM"));
		result.setTableName(ResultSetUtils.getString(rs, "TABLE_NAME"));
		result.setColumnName(ResultSetUtils.getString(rs, "COLUMN_NAME"));
		result.setKeySeq(ResultSetUtils.getShort(rs, "KEY_SEQ"));
		result.setPkName(ResultSetUtils.getString(rs, "PK_NAME"));
		
		return result;	
	}

	/**
	 * Close the opened db connection.
	 * 
	 * @throws SQLException
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

}

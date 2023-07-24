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

	public List<TableMeta> getTableMetas(String schema, String tableName) throws SQLException {
		var tables = meta.getTables(connection.getCatalog(),
				StringUtils.isEmpty(schema) ? connection.getSchema() : schema, tableName, IN_SCOPE_TABLE_TYPES);
		var result = new ArrayList<TableMeta>(256);
		
		while (tables.next()) {
			result.add(convertTableMeta(tables));
		}

		return result;
	}

	private TableMeta convertTableMeta(ResultSet rs) throws SQLException {
		return new TableMeta(
				ResultSetUtils.getString(rs, "TABLE_NAME"),
				ResultSetUtils.getString(rs, "TABLE_CAT"),
				ResultSetUtils.getString(rs, "TABLE_SCHEM"),
				ResultSetUtils.getString(rs, "TABLE_TYPE"),
				ResultSetUtils.getString(rs, "TYPE_NAME"),
				ResultSetUtils.getString(rs, "TYPE_CAT"),
				ResultSetUtils.getString(rs, "TYPE_SCHEM"),
				ResultSetUtils.getString(rs, "REMARKS"),
				ResultSetUtils.getString(rs, "SELF_REFERENCING_COL_NAME"),
				ResultSetUtils.getString(rs, "REF_GENERATION")
				);
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

/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @ClassName: GeneratorConfiguration 
 * @Desc:		Represent a generator configuration xml.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 11:26:29 AM	 
 */
@Data
public final class GeneratorConfiguration {
	
	private DbType dbType;
	
	private JdbcConnection jdbcConnection;
	
	private List<Template> templates;	

	private List<Table> tables;
	
	@Data
	public static class JdbcConnection {
		
		private String connectionURL;
		
		private String userId;
		
		private String password;
	}
	
	@Data
	public static class Template {
		
		private String file;
		
		private String codegenPath;
		
		private String codegenFile;
	}
	
	@Data
	public static class Table {
		private String catalog;
		
		private String schema;
		
		private String tableName;
	}
	
	@Getter
	@ToString
	public static enum DbType {
		SQL_SERVER("sqlserver"),
		MYSQL("myql");		
		
		@JsonValue
		private String name;
		
		private DbType(String name) {
			this.name = name;
		}	
	}
}







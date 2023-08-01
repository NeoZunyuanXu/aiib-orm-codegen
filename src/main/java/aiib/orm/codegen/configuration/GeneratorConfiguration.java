/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
	
	private String javaTypeResolver;
	
	private Templates templates;	

	private Tables tables;
	
	@Data
	public static class JdbcConnection {
		
		private String connectionURL;
		
		private String userId;
		
		private String password;
	}
	
	@Data
	public static class Templates {
		@JacksonXmlProperty(isAttribute = true)
		private String path;
		
		@JacksonXmlProperty(isAttribute = true)
		private String targetRootPath;
		
		@JacksonXmlProperty(isAttribute = false, localName = "template")
		@JacksonXmlElementWrapper(useWrapping = false)
		private List<Template> templates;
	}
	
	@Data
	public static class Template {
		
		private String file;
		
		private String targetProject;
		
		private String targetPackage;
		
		private String targetFile;
	}
	
	@Data
	public static class Tables {
		@JacksonXmlProperty(isAttribute = false, localName = "table")
		@JacksonXmlElementWrapper(useWrapping = false)
		private List<Table> tables;
	}
	
	@Data
	public static class Table {
		private String catalog;
		
		private String schema;
		
		private String tableName;
		
		@JacksonXmlProperty(isAttribute = false, localName = "generatedKey")
		@JacksonXmlElementWrapper(useWrapping = false)
		@JsonMerge
		private List<GeneratedKey> generatedKeys;
	}
	
	@Data
	public static class GeneratedKey {
		private String column;
		
		private String strategy;
		
		private String parameters;
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







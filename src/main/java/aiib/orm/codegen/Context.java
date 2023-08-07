/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import aiib.orm.codegen.configuration.GeneratorConfiguration;
import aiib.orm.codegen.configuration.GeneratorConfigurationParser;
import aiib.orm.codegen.db.DbClient;
import aiib.orm.codegen.db.TableMeta;
import aiib.orm.codegen.type.JavaTypeResolver;
import aiib.orm.codegen.type.JavaTypeResolverFactory;
import aiib.orm.codegen.util.CollectionUtils;
import lombok.Getter;

/**
 * @ClassName: Context 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 26, 2023 11:36:52 AM	 
 */
public class Context {	
	@Getter
	private final GeneratorConfiguration configuration;
	
	@Getter
	private final List<TableMeta> tableMetas;
	
	private final JavaTypeResolver javaTypeResolver;
	
	public Context(String configurationFilePath) throws Exception {
		configuration = GeneratorConfigurationParser.parse(configurationFilePath);
		tableMetas = new ArrayList<TableMeta>(1024);		
		javaTypeResolver = JavaTypeResolverFactory.createInstance(configuration.getJavaTypeResolver());
		
		initializeTableMetas();
	}
	
	private void initializeTableMetas() throws Exception {
		try (var dbClient = new DbClient(configuration.getDbType(), configuration.getJdbcConnection())) {
			for (var table : configuration.getTables().getTables()) {
				var tableMetas = dbClient.getTableMetas(table.getCatalog(), table.getSchema(), table.getTableName());
				
				for (var tableMeta : tableMetas) {
					tableMeta.setContext(this);						
										
					for (var columnMeta : tableMeta.getColumns()) {
						var javaType = javaTypeResolver.resolveColumnType(columnMeta);
						
						columnMeta.setJavaType(javaType);
						
						if (javaType.isExplicitImported()) {
							tableMeta.addExplicitImportedJavaType(javaType.getFullName());
						}
						
						if (CollectionUtils.isNotEmpty(table.getGeneratedKeys())) {
							var generatedKey = table.getGeneratedKeys().stream()
									.filter(e -> StringUtils.equalsIgnoreCase(columnMeta.getName(), e.getColumn()))
									.findFirst();
							
							if (generatedKey.isPresent()) {
								columnMeta.setGeneratedKey(true);
								columnMeta.setGeneratedKeyType(generatedKey.get().getType());
								columnMeta.setGeneratedKeyParameters(generatedKey.get().getParameters());
							}
						}
					}
					
					this.tableMetas.add(tableMeta);
				}
			}
		} 
	}
	
}

/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import aiib.orm.codegen.configuration.GeneratorConfiguration;
import aiib.orm.codegen.configuration.GeneratorConfiguration.NamingStrategy;
import aiib.orm.codegen.configuration.GeneratorConfigurationParser;
import aiib.orm.codegen.db.DbClient;
import aiib.orm.codegen.db.TableMeta;
import aiib.orm.codegen.type.JavaTypeResolver;
import aiib.orm.codegen.type.JavaTypeResolverFactory;
import aiib.orm.codegen.util.CaseUtils;
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
					tableMeta.setNaming(namingTable(tableMeta.getName(), configuration.getTables().getNamingStrategy()));
					
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
								columnMeta.setGeneratedKeyStrategy(generatedKey.get().getStrategy());
								columnMeta.setGeneratedKeyParameters(generatedKey.get().getParameters());
							}
						}
					}
					
					this.tableMetas.add(tableMeta);
				}
			}
		} 
	}
	
	private String namingTable(String name, NamingStrategy strategy) {
		if (strategy == null) strategy = NamingStrategy.DEFAULT;
		
		return switch (strategy) {
			case DEFAULT -> CaseUtils.toCamelCase(name, true);
			case CHOP_FIRST_PHASE -> {
				String chopedName;	
				var pos = StringUtils.indexOfAny(name, CaseUtils.NAME_DELIMETERS);
				
				if (pos == StringUtils.INDEX_NOT_FOUND) {
					chopedName = name;
				
				} else {
					chopedName = name.substring(pos + 1);
				}
								
				yield CaseUtils.toCamelCase(chopedName, true);
			}
		};
	}
	
}

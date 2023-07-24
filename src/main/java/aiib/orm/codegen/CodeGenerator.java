/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen;

import aiib.orm.codegen.config.GeneratorConfigurationParser;
import aiib.orm.codegen.db.DbClient;
import aiib.orm.codegen.util.CollectionUtils;

/**
 * @ClassName: CodeGenerator
 * @Desc: A brief desc to the class function.
 * @Author: zunyuan.xu
 * @Date: Jul 20, 2023 5:19:51 PM
 */
public final class CodeGenerator {

	public static void main(String[] args) throws Exception {
		var config = GeneratorConfigurationParser.parse(
				"C:\\Users\\zunyuan.xu\\source\\eclipse\\orm.codegen\\src\\main\\resources\\risk-rdm-codegen.xml");
		
		if (CollectionUtils.isEmpty(config.getTemplates()) || CollectionUtils.isEmpty(config.getTables())) {
			return;
		}			

		try (var dbClient = new DbClient(config.getDbType(), config.getJdbcConnection())) {
			for (var table : config.getTables()) {
				var tableMetas = dbClient.getTableMetas(table.getCatalog(), table.getSchema(), table.getTableName());
	
				System.out.println(tableMetas.toString());
			}
			
			
		} catch (Exception e) {
			throw e;
		}
	}

}

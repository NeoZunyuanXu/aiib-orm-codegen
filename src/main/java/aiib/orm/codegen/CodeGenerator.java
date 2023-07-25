/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;

import aiib.orm.codegen.config.GeneratorConfiguration;
import aiib.orm.codegen.config.GeneratorConfiguration.Template;
import aiib.orm.codegen.config.GeneratorConfigurationParser;
import aiib.orm.codegen.db.DbClient;
import aiib.orm.codegen.db.TableMeta;
import aiib.orm.codegen.util.CollectionUtils;

/**
 * @ClassName: CodeGenerator
 * @Desc: A brief desc to the class function.
 * @Author: zunyuan.xu
 * @Date: Jul 20, 2023 5:19:51 PM
 */
public final class CodeGenerator {
	private GeneratorConfiguration config;	
	private VelocityEngine engine;
	private ToolContext baseContext;
	
	public CodeGenerator(String configFilePath) throws Exception {
		config = GeneratorConfigurationParser.parse(configFilePath);
		engine = new VelocityEngine();
		
		var initProperties = new Properties();
		
		initProperties.put("file.resource.loader.path", config.getTemplates().getTemplateRootPath());
		initProperties.put("file.resource.loader.cache", true);
		
		engine.init(initProperties);
		
		Map<String, Object> map = Map.of("engine", engine, "config", config);		
		var manage = new ToolManager(true, true);
		
		baseContext = manage.createContext(map);		
	}
	
	public void execute() throws Exception {				
		if (CollectionUtils.isEmpty(config.getTemplates().getTemplates()) 
				|| CollectionUtils.isEmpty(config.getTables())) {
			return;
		}
		
		try (var dbClient = new DbClient(config.getDbType(), config.getJdbcConnection())) {
			for (var table : config.getTables()) {
				var tableMetas = dbClient.getTableMetas(table.getCatalog(), table.getSchema(), table.getTableName());
				
				for (var tableMeta : tableMetas) {
					for (var template : config.getTemplates().getTemplates()) {
						executeOne(tableMeta, template);
					}					
				}
			}			
			
		} catch (Exception e) {
			throw e;
		}
	}	
	
	private void executeOne(TableMeta tableMeta, Template template) throws IOException {
		var context = new VelocityContext(baseContext);
		var tableName = CaseUtils.toCamelCase(tableMeta.getTableName(), true, '_', '-');		
		var filePath = Paths.get(
				config.getTemplates().getTargetRootPath(), 
				template.getTargetProject(),
				RegExUtils.replaceAll(template.getTargetPackage(), "\\.", "/")).toAbsolutePath();		
		
		var fileName = StringUtils.join(template.getTargetFilePrefix(), 
				tableName, template.getTargetFileSuffix()); 
		
		var fileFullName = StringUtils.join(fileName, template.getTargetFileExt());
				
		context.put("tableMeta", tableMeta);
		context.put("template", template);
		context.put("tableName", tableName);
		context.put("fileName", fileName);
		
		if (StringUtils.isNotEmpty(template.getTargetPackage())) {
			context.put("package", template.getTargetPackage());
		}
		
		var file = new File(filePath + "/" + fileFullName);
		file.getParentFile().mkdirs();
		
		try (var fileWriter = new FileWriter(file, false)) {
			engine.mergeTemplate(template.getFile(), "UTF-8", context, fileWriter);
			
		} catch (IOException e) {
			throw e;
		}		
	}

	public static void main(String[] args) throws Exception {
		var generator = new CodeGenerator("C:\\Users\\zunyuan.xu\\source\\eclipse\\orm.codegen\\src\\main\\resources\\risk-rdm-codegen.xml");
	
		generator.execute();
	}

}

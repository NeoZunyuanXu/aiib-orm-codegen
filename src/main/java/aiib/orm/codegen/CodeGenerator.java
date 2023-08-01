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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;

import aiib.orm.codegen.configuration.GeneratorConfiguration.Template;
import aiib.orm.codegen.db.TableMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: CodeGenerator
 * @Desc: A brief desc to the class function.
 * @Author: zunyuan.xu
 * @Date: Jul 20, 2023 5:19:51 PM
 */
@Slf4j
public final class CodeGenerator {
	private final Context context;	
	private final VelocityEngine engine;
	private final ToolContext toolContext;
	
	private final String CLASSPATH_PREFIX = "classpath:";
	private String templatePath = "";
	
	public CodeGenerator(String configurationFilePath) throws Exception {
		context = new Context(configurationFilePath);
		engine = new VelocityEngine();
		
		var initProperties = new Properties();
		var path = context.getConfiguration().getTemplates().getPath().trim();	
				
		if (path.startsWith(CLASSPATH_PREFIX)) {
			initProperties.put(RuntimeConstants.RESOURCE_LOADERS, "class");
			initProperties.put("resource.loader.class.class", ClasspathResourceLoader.class.getName());
			
			templatePath = path.substring(CLASSPATH_PREFIX.length()).trim();
			if (!templatePath.endsWith("/")) templatePath = templatePath + "/";
			
		} else {
			initProperties.put(RuntimeConstants.RESOURCE_LOADERS, "file");
			initProperties.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, context.getConfiguration().getTemplates().getPath());
			initProperties.put(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, true);
		}		
		
		initProperties.put(RuntimeConstants.SPACE_GOBBLING, "structured");
		
		engine.init(initProperties);
		
		Map<String, Object> map = Map.of("engine", engine, "context", context);		
		var manage = new ToolManager(true, true);
		
		toolContext = manage.createContext(map);		
	}
	
	public void execute() throws Exception {
		for (var tableMeta : context.getTableMetas()) {
			log.info("table : {} is under generating...", tableMeta.getName());
			
			for (var template : context.getConfiguration().getTemplates().getTemplates()) {
				execute(tableMeta, template);
			}			
		}
		
		log.info("code generate finished. total {} table is done.", context.getTableMetas().size());
	}	
	
	private void execute(TableMeta tableMeta, Template template) throws IOException {
		var velocityContext = new VelocityContext(toolContext);		
		var packageName = popluatePackage(template.getTargetPackage(), tableMeta);
		
		var path = Paths.get(
				context.getConfiguration().getTemplates().getTargetRootPath(), 
				template.getTargetProject(),
				RegExUtils.replaceAll(packageName, "\\.", "/"))
				.toAbsolutePath().toString();		
		
		var pathFile = new File(path);
		if (!pathFile.exists()) pathFile.mkdirs();
	
		var fileName = populateProperties(template.getTargetFile(), tableMeta); 
				
		velocityContext.put("table", tableMeta);
		velocityContext.put("fileName", fileName);
		velocityContext.put("packageName", packageName == null ? "" : packageName);
		velocityContext.put("CaseUtils", CaseUtils.class);
		velocityContext.put("StringUtils", StringUtils.class);
		
		var file = new File(path + "/" + fileName);
		
		try (var fileWriter = new FileWriter(file, false)) {
			engine.mergeTemplate(templatePath + template.getFile(), "UTF-8", velocityContext, fileWriter);			
		}	
	}
	
	private final String PROPERTY_START = "${";
	private final String PROPERTY_END = "}";	
	
	private String populateProperties(String str, TableMeta tableMeta) {
		if (str == null) return str;
		
		var start = str.indexOf(PROPERTY_START);		
		if (start == StringUtils.INDEX_NOT_FOUND) return str;
		
		var end = str.indexOf(PROPERTY_END, start + 1);
		if (end == StringUtils.INDEX_NOT_FOUND) return str;
		
		var propertyName = str.substring(start + PROPERTY_START.length(), end).trim();
		String propertyValue;
		try {
			propertyValue = BeanUtils.getProperty(tableMeta, propertyName);
		} catch (Exception e) {
			propertyValue = StringUtils.EMPTY;
		}
					
		var populatedValue = str.substring(0, start) + propertyValue + str.substring(end + PROPERTY_END.length());
		
		return populateProperties(populatedValue, tableMeta);
	}
	
	private String popluatePackage(String str, TableMeta tableMeta) {
		if (str == null) return str;
		
		str = populateProperties(str, tableMeta);
		if (str.endsWith(".")) {
			str = str.substring(0, str.length() - 1);
		
		} else {
			str = RegExUtils.replaceAll(str, "\\.{2,}", ".");
		}
		
		return str;
	}

	public static void main(String[] args) throws Exception {
		var generator = new CodeGenerator("C:\\Users\\zunyuan.xu\\source\\eclipse\\orm.codegen\\src\\main\\resources\\risk-rdm-codegen.xml");
			
		generator.execute();
	}

}

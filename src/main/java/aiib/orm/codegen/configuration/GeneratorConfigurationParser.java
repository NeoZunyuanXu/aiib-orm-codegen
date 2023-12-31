/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.configuration;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @ClassName: GeneratorConfigurationParser 
 * @Desc:		The config file parse utility.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 12:07:30 PM	 
 */
public final class GeneratorConfigurationParser {
	
	/**
	 * Parse the config file to a {@link GeneratorConfiguration} instance.
	 * 
	 * @param configFilePathName
	 * @return
	 * @throws IOException 
	 * @throws DatabindException 
	 * @throws StreamReadException 
	 */
	public static GeneratorConfiguration parse(String configFilePathName) throws StreamReadException, DatabindException, IOException {
		var mapper = new XmlMapper();
		
		// ignore the unknown properties
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	
		return mapper.readValue(new File(configFilePathName), GeneratorConfiguration.class);
	}

}

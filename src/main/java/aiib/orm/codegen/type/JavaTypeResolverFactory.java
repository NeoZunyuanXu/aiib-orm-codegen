/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.type;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: JavaTypeResolverFactory 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 26, 2023 12:09:58 PM	 
 */
public class JavaTypeResolverFactory {
	
	public static JavaTypeResolver createInstance() throws Exception {
		return createInstance(null);
	}
	
	public static JavaTypeResolver createInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (StringUtils.isBlank(className)) return new JavaTypeResolverDefaultImpl();
		
		var clazz = ClassUtils.getClass(className.trim());
		
		return (JavaTypeResolver)clazz.getDeclaredConstructor().newInstance();
	}
}

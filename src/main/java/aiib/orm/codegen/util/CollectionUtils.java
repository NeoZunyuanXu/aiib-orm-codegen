/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.util;

import java.util.Collection;

/**
 * @ClassName: CollectionUtils 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 20, 2023 6:09:37 PM	 
 */
public final class CollectionUtils {
	public static <E> boolean isEmpty(Collection<E> coll) {
		return coll == null || coll.size() == 0;
	}
	
	public static <E> boolean isNotEmpty(Collection<E> coll) {
		return !isEmpty(coll);
	}
}

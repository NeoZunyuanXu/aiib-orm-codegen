/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.db;

import lombok.Data;

/**
 * @ClassName: PrimaryKeyMeta 
 * @Desc:		A brief desc to the class function.
 * @Author:  	zunyuan.xu
 * @Date:		Jul 24, 2023 5:30:02 PM	 
 */
@Data
public class PrimaryKeyMeta {
	private String tableCat;
	private String tableSchem;
	private String tableName;
	private String columnName;
	private Short keySeq;
	private String pkName;
}

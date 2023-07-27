/*
 * All rights reserved by the Asian Infrustructure Investment Bank(AIIB). 
 */
package aiib.orm.codegen.type;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aiib.orm.codegen.db.ColumnMeta;

/**
 * @ClassName: JavaTypeResolverDefaultImpl
 * @Desc: A brief desc to the class function.
 * @Author: zunyuan.xu
 * @Date: Jul 25, 2023 5:36:56 PM
 */
public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {
	protected final Map<Integer, FullyQualifiedJavaType> typeMap;

	public JavaTypeResolverDefaultImpl() {
		typeMap = new HashMap<>();

		typeMap.put(Types.ARRAY, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.BIGINT, new FullyQualifiedJavaType(Long.class.getName()));
		typeMap.put(Types.BINARY, new FullyQualifiedJavaType("byte[]"));
		typeMap.put(Types.BIT, new FullyQualifiedJavaType(Boolean.class.getName()));
		typeMap.put(Types.BLOB, new FullyQualifiedJavaType("byte[]"));
		typeMap.put(Types.BOOLEAN, new FullyQualifiedJavaType(Boolean.class.getName()));
		typeMap.put(Types.CHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.CLOB, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.DATALINK, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.DATE, new FullyQualifiedJavaType(Date.class.getName()));
		typeMap.put(Types.DECIMAL, new FullyQualifiedJavaType(BigDecimal.class.getName()));
		typeMap.put(Types.DISTINCT, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.DOUBLE, new FullyQualifiedJavaType(Double.class.getName()));
		typeMap.put(Types.FLOAT, new FullyQualifiedJavaType(Double.class.getName()));
		typeMap.put(Types.INTEGER, new FullyQualifiedJavaType(Integer.class.getName()));
		typeMap.put(Types.JAVA_OBJECT, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.LONGNVARCHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.LONGVARBINARY, new FullyQualifiedJavaType("byte[]"));
		typeMap.put(Types.LONGVARCHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.NCHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.NCLOB, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.NVARCHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.NULL, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.NUMERIC, new FullyQualifiedJavaType(BigDecimal.class.getName()));
		typeMap.put(Types.OTHER, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.REAL, new FullyQualifiedJavaType(Float.class.getName()));
		typeMap.put(Types.REF, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.SMALLINT, new FullyQualifiedJavaType(Short.class.getName()));
		typeMap.put(Types.STRUCT, new FullyQualifiedJavaType(Object.class.getName()));
		typeMap.put(Types.TIME, new FullyQualifiedJavaType(Date.class.getName()));
		typeMap.put(Types.TIMESTAMP, new FullyQualifiedJavaType(Date.class.getName()));
		typeMap.put(Types.TINYINT, new FullyQualifiedJavaType(Byte.class.getName()));
		typeMap.put(Types.VARBINARY, new FullyQualifiedJavaType("byte[]"));
		typeMap.put(Types.VARCHAR, new FullyQualifiedJavaType(String.class.getName()));
		typeMap.put(Types.TIME_WITH_TIMEZONE, new FullyQualifiedJavaType("java.time.OffsetTime"));
		typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, new FullyQualifiedJavaType("java.time.OffsetDateTime"));
	}

	/**
	 * @param columnMeta
	 * @return
	 * @see aiib.orm.codegen.type.JavaTypeResolver#resolveColumnType(aiib.orm.codegen.db.ColumnMeta)
	 */
	@Override
	public FullyQualifiedJavaType resolveColumnType(ColumnMeta columnMeta) {
		var answer = typeMap.get(columnMeta.getJdbcType());

		answer = overrideDefaultType(columnMeta, answer);

		return answer;
	}

	protected FullyQualifiedJavaType overrideDefaultType(ColumnMeta columnMeta, FullyQualifiedJavaType defaultType) {

		return switch (columnMeta.getJdbcType()) {
				case Types.BIT -> columnMeta.getLength() > 1 ? new FullyQualifiedJavaType("byte[]") : defaultType;
				case Types.DATE -> new FullyQualifiedJavaType("java.time.LocalDate");
				case Types.DECIMAL, Types.NUMERIC -> {
					if (columnMeta.getScale() > 0 || columnMeta.getLength() > 18) {
						yield defaultType;
					
					} else if (columnMeta.getLength() > 9) {
						yield new FullyQualifiedJavaType(Long.class.getName());
					
					} else {
						yield new FullyQualifiedJavaType(Integer.class.getName());
					}
				}
				case Types.TIME -> new FullyQualifiedJavaType("java.time.LocalTime");
				case Types.TIMESTAMP -> new FullyQualifiedJavaType("java.time.LocalDateTime");
				default -> defaultType;
			};
	}
}

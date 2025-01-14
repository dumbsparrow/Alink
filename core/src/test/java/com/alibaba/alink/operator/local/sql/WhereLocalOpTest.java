package com.alibaba.alink.operator.local.sql;

import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.operator.local.LocalOperator;
import com.alibaba.alink.operator.local.source.TableSourceLocalOp;
import org.junit.Test;

public class WhereLocalOpTest {
	@Test
	public void testWhereLocalOp() {
		String URL = "https://alink-test-data.oss-cn-hangzhou.aliyuncs.com/iris.csv";
		String SCHEMA_STR
			= "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";
		LocalOperator <?> data = new TableSourceLocalOp(
			new CsvSourceBatchOp().setFilePath(URL).setSchemaStr(SCHEMA_STR).collectMTable());
		data.link(new WhereLocalOp().setClause("category='Iris-setosa'")).print();
	}
}
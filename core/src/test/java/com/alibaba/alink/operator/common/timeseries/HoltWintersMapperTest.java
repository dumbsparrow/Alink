package com.alibaba.alink.operator.common.timeseries;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.types.Row;

import com.alibaba.alink.common.MTable;
import com.alibaba.alink.common.type.AlinkTypes;
import com.alibaba.alink.common.MTableUtil;
import com.alibaba.alink.params.timeseries.HoltWintersParams;
import com.alibaba.alink.testutil.AlinkTestBase;
import org.junit.Test;

import java.util.List;

public class HoltWintersMapperTest extends AlinkTestBase {

	@Test
	public void test() throws Exception {
		String data = "{\"data\":{\"ds\":[\"2021-04-03 00:00:00.0\",\"2021-04-04 00:00:00.0\",\"2021-04-05 "
			+ "00:00:00.0\",\"2021-04-06 00:00:00.0\",\"2021-04-07 00:00:00.0\",\"2021-04-08 00:00:00.0\",\"2021-04-09"
			+ " 00:00:00.0\",\"2021-04-10 00:00:00.0\",\"2021-04-11 00:00:00.0\",\"2021-04-12 00:00:00.0\","
			+ "\"2021-04-13 00:00:00.0\",\"2021-04-14 00:00:00.0\",\"2021-04-15 00:00:00.0\",\"2021-04-16 "
			+ "00:00:00.0\",\"2021-04-17 00:00:00.0\",\"2021-04-18 00:00:00.0\",\"2021-04-19 00:00:00.0\",\"2021-04-20"
			+ " 00:00:00.0\",\"2021-04-21 00:00:00.0\",\"2021-04-22 00:00:00.0\",\"2021-04-23 00:00:00.0\","
			+ "\"2021-04-24 00:00:00.0\",\"2021-04-25 00:00:00.0\",\"2021-04-26 00:00:00.0\",\"2021-04-27 "
			+ "00:00:00.0\",\"2021-04-28 00:00:00.0\",\"2021-04-29 00:00:00.0\",\"2021-04-30 00:00:00.0\",\"2021-05-01"
			+ " 00:00:00.0\",\"2021-05-02 00:00:00.0\",\"2021-05-03 00:00:00.0\",\"2021-05-04 00:00:00.0\","
			+ "\"2021-05-05 00:00:00.0\",\"2021-05-06 00:00:00.0\",\"2021-05-07 00:00:00.0\",\"2021-05-08 "
			+ "00:00:00.0\",\"2021-05-09 00:00:00.0\",\"2021-05-10 00:00:00.0\",\"2021-05-11 00:00:00.0\",\"2021-05-12"
			+ " 00:00:00.0\",\"2021-05-13 00:00:00.0\",\"2021-05-14 00:00:00.0\",\"2021-05-15 00:00:00.0\","
			+ "\"2021-05-16 00:00:00.0\",\"2021-05-17 00:00:00.0\",\"2021-05-18 00:00:00.0\",\"2021-05-19 "
			+ "00:00:00.0\",\"2021-05-20 00:00:00.0\",\"2021-05-21 00:00:00.0\",\"2021-05-22 00:00:00.0\",\"2021-05-23"
			+ " 00:00:00.0\",\"2021-05-24 00:00:00.0\",\"2021-05-25 00:00:00.0\",\"2021-05-26 00:00:00.0\","
			+ "\"2021-05-27 00:00:00.0\",\"2021-05-28 00:00:00.0\",\"2021-05-29 00:00:00.0\",\"2021-05-30 "
			+ "00:00:00.0\",\"2021-05-31 00:00:00.0\",\"2021-06-01 00:00:00.0\",\"2021-06-02 00:00:00.0\",\"2021-06-03"
			+ " 00:00:00.0\",\"2021-06-04 00:00:00.0\",\"2021-06-05 00:00:00.0\",\"2021-06-06 00:00:00.0\","
			+ "\"2021-06-07 00:00:00.0\",\"2021-06-08 00:00:00.0\",\"2021-06-09 00:00:00.0\",\"2021-06-10 "
			+ "00:00:00.0\",\"2021-06-11 00:00:00.0\",\"2021-06-12 00:00:00.0\",\"2021-06-13 00:00:00.0\",\"2021-06-14"
			+ " 00:00:00.0\",\"2021-06-15 00:00:00.0\",\"2021-06-16 00:00:00.0\",\"2021-06-17 00:00:00.0\","
			+ "\"2021-06-18 00:00:00.0\",\"2021-06-19 00:00:00.0\",\"2021-06-20 00:00:00.0\",\"2021-06-21 "
			+ "00:00:00.0\",\"2021-06-22 00:00:00.0\",\"2021-06-23 00:00:00.0\",\"2021-06-24 00:00:00.0\",\"2021-06-25"
			+ " 00:00:00.0\",\"2021-06-26 00:00:00.0\",\"2021-06-27 00:00:00.0\",\"2021-06-28 00:00:00.0\","
			+ "\"2021-06-29 00:00:00.0\",\"2021-06-30 00:00:00.0\",\"2021-07-01 00:00:00.0\",\"2021-07-02 "
			+ "00:00:00.0\",\"2021-07-03 00:00:00.0\",\"2021-07-04 00:00:00.0\",\"2021-07-05 00:00:00.0\",\"2021-07-06"
			+ " 00:00:00.0\",\"2021-07-07 00:00:00.0\",\"2021-07-08 00:00:00.0\",\"2021-07-09 00:00:00.0\","
			+ "\"2021-07-10 00:00:00.0\",\"2021-07-11 00:00:00.0\"],\"request_cnt\":[34002,32962,36312,38725,38946,"
			+ "42767,38813,37026,36228,38404,36739,37798,36198,34757,33089,34821,76381,72847,76770,72637,63416,59350,"
			+ "62816,51849,46223,42275,37765,32305,31773,30401,32356,33962,38361,42725,41915,40839,37607,40688,42341,"
			+ "42676,37784,33290,33178,38902,40305,42502,40571,37007,33891,31486,32133,52188,74300,72416,73886,76997,"
			+ "66838,51097,47738,95033,88243,92592,84959,71047,53164,58806,57415,115252,65742,54093,50521,48097,44476,"
			+ "52632,120242,111584,181650,144878,102792,94398,85064,68090,60413,53281,47004,44886,46542,43183,42861,"
			+ "47976,62843,75449,75843,76860,59401,45445,43007,36851,31323,30936]},\"schema\":\"ds TIMESTAMP,"
			+ "request_cnt BIGINT\"}";
		MTable mtable = MTable.fromJson(data);

		TableSchema tableSchema = new TableSchema(new String[] {"col"}, new TypeInformation[] {AlinkTypes.M_TABLE});

		Params params = new Params()
			.set(HoltWintersParams.FREQUENCY, 4)
			.set(HoltWintersParams.VALUE_COL, "col")
			.set(HoltWintersParams.PREDICT_NUM, 28)
			.set(HoltWintersParams.DO_TREND, true)
			.set(HoltWintersParams.DO_SEASONAL, true)
			.set(HoltWintersParams.PREDICTION_COL, "pred");

		HoltWintersMapper mapper = new HoltWintersMapper(tableSchema, params);
		mapper.open();
		Row row = mapper.map(Row.of(mtable));

		List<Object> result = MTableUtil.getColumn(((MTable)row.getField(1)), "request_cnt");
		for(Object re: result) {
			System.out.println(re);
		}

	}



}
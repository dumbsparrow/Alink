package com.alibaba.alink.operator.common.statistics.basicstatistic;

import com.alibaba.alink.common.linalg.DenseVector;
import com.alibaba.alink.common.linalg.tensor.LongTensor;
import com.alibaba.alink.common.utils.TableUtil;
import com.alibaba.alink.operator.common.utils.PrettyDisplayUtils;

/**
 * It is summary result of sparse vector.
 * You can get vectorSize, mean, variance, and other statistics from this class,
 * and get statistics with colName.
 */
public class TableSummary extends BaseSummary {

	private static final long serialVersionUID = 6895996471831836377L;
	/**
	 * col names which are calculated.
	 */
	String[] colNames;

	/**
	 * the number of missing value.
	 */
	long[] numMissingValue;

	/**
	 * sum_i = sum(x_i)
	 */
	DenseVector sum;

	/**
	 * sum2_i = sum(x_i * x_i)
	 */
	DenseVector sum2;

	/**
	 * sum3_i = sum(x_i * x_i * x_i)
	 */
	DenseVector sum3;

	/**
	 * sum4_i = sum(x_i * x_i * x_i * x_i)
	 */
	DenseVector sum4;

	/**
	 * min_i = min(x_i)
	 */
	DenseVector minDouble;

	/**
	 * max_i = max(x_i)
	 */
	DenseVector maxDouble;

	/**
	 * min.
	 */
	Object[] min;

	/**
	 * max.
	 */
	Object[] max;

	/**
	 * normL1_i = sum(|x_i|)
	 */
	DenseVector normL1;

	/**
	 * the indices of columns which type is numerical.
	 */
	int[] numericalColIndices;

	/**
	 * It will be generated by summary.
	 */
	TableSummary() {

	}

	@Override
	public String toString() {
		String[] colCols = new String[] {"colName", "count", "missing",
			"sum", "mean", "variance", "min", "max"};

		Object[][] vals = new Object[colNames.length][colCols.length];

		for (int i = 0; i < colNames.length; i++) {
			String colName = colNames[i];
			vals[i][0] = colName;
			vals[i][1] = count;
			vals[i][2] = numMissingValue(colName);
			vals[i][3] = sum(colName);
			vals[i][4] = mean(colName);
			vals[i][5] = variance(colName);
			vals[i][6] = min(colName);
			vals[i][7] = max(colName);
		}

		return "Summary: " + "\n" + PrettyDisplayUtils.displayTable(vals, colNames.length,
			colCols.length, null, colCols,
			"Summary", colNames.length, 11);
	}

	/**
	 * col names which are calculated.
	 */
	public String[] getColNames() {
		return this.colNames.clone();
	}

	/**
	 * given colName, return sum of the column.
	 */
	public double sum(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return 0;
			}
			return sum.get(idx);
		} else {
			return Double.NaN;
		}
	}

	/**
	 * given colName, return mean of the column.
	 */
	public double mean(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return Double.NaN;
			}
			return sum.get(idx) / numValidValue(colName);
		} else {
			return Double.NaN;
		}
	}

	/**
	 * given colName, return variance of the column.
	 */
	public double variance(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			long numVaildValue = numValidValue(colName);
			if (0 == numVaildValue || 1 == numVaildValue) {
				return 0;
			}
			return Math.max(0.0, (sum2.get(idx) - sum.get(idx) * sum.get(idx) / numVaildValue) / (numVaildValue
				- 1));
		} else {
			return Double.NaN;
		}
	}

	/**
	 * given colName, return standardDeviation of the column.
	 */
	public double standardDeviation(String colName) {
		return Math.sqrt(variance(colName));
	}

	/**
	 * given colName, return standardError of the column.
	 */
	public double standardError(String colName) {
		return standardDeviation(colName) / Math.sqrt(count);
	}

	/**
	 * given colName, return min of the column.
	 */
	public double minDouble(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return Double.NaN;
			}
			return minDouble.get(idx);
		} else {
			return Double.NaN;
		}
	}

	/**
	 * given colName, return max of the column.
	 */
	public double maxDouble(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return Double.NaN;
			}
			return maxDouble.get(idx);
		} else {
			return Double.NaN;
		}
	}

	public Object min(String colName) {
		int idx = findIdx(colName);
		if (idx < 0) {
			return Double.NaN;
		}
		return isEmpty(colName) ? Double.NaN : min[idx];
	}

	public Object max(String colName) {
		int idx = findIdx(colName);
		if (idx < 0) {
			return Double.NaN;
		}
		return isEmpty(colName) ? Double.NaN : max[idx];
	}

	/**
	 * given colName, return l1 norm of the column.
	 */
	public double normL1(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return Double.NaN;
			}
			return normL1.get(idx);
		} else {
			return Double.NaN;
		}
	}

	/**
	 * given colName, return l2 norm of the column.
	 */
	public double normL2(String colName) {
		int idx = findIdx(colName);
		if (idx >= 0) {
			if (isEmpty(colName)) {
				return Double.NaN;
			}
			return Math.sqrt(sum2.get(idx));
		} else {
			return Double.NaN;
		}
	}

	/**
	 * 二阶中心矩
	 */
	public double centralMoment2(String colName) {
		if (isEmpty(colName)) {
			return Double.NaN;
		}
		int idx = findIdx(colName);
		double mean = mean(colName);
		if (idx >= 0) {
			return sum2.get(idx) / count - mean * mean;
		} else {
			return Double.NaN;
		}
	}

	/**
	 * 三阶中心矩
	 */
	public double centralMoment3(String colName) {
		if (isEmpty(colName)) {
			return Double.NaN;
		}
		int idx = findIdx(colName);
		double mean = mean(colName);
		if (idx >= 0) {
			return (sum3.get(idx) - 3 * sum2.get(idx) * mean + 2 * sum.get(idx) * mean * mean) / count;
		} else {
			return Double.NaN;
		}
	}

	public double centralMoment4(String colName) {
		if (isEmpty(colName)) {
			return Double.NaN;
		}
		int idx = findIdx(colName);
		double mean = mean(colName);
		if (idx >= 0) {
			return (sum4.get(idx) - 4 * sum3.get(idx) * mean + 6 * sum2.get(idx) * mean * mean - 3 * sum.get(idx) * mean * mean * mean) / count;
		} else {
			return Double.NaN;
		}
	}

	/**
	 * Skewness
	 */
	public double skewness(String colName) {
		return centralMoment3(colName) / (centralMoment2(colName) * Math.sqrt(centralMoment2(colName)));
	}

	/**
	 * Kurtosis
	 */
	public double kurtosis(String colName) {
		return centralMoment4(colName) / (centralMoment2(colName) * centralMoment2(colName)) - 3;
	}

	/**
	 * given colName, return the number of valid value.
	 */
	public long numValidValue(String colName) {
		return count - numMissingValue(colName);
	}

	/**
	 * given colName, return the number of vaild value.
	 */
	public long numMissingValue(String colName) {
		int idx = TableUtil.findColIndexWithAssertAndHint(colNames, colName);
		if (this.count == 0) {
			return 0;
		}
		return numMissingValue[idx];
	}

	public double cv(String colName) {
		return standardDeviation(colName) / mean(colName);
	}

	/**
	 * given colName, return index of colNames.
	 */
	private int findIdx(String colName) {
		int idx = TableUtil.findColIndexWithAssertAndHint(colNames, colName);
		return findIdx(numericalColIndices, idx);
	}

	/**
	 * given idx, return idx.
	 */
	private static int findIdx(int[] colIndices, int idx) {
		for (int i = 0; i < colIndices.length; i++) {
			if (idx == colIndices[i]) {
				return i;
			}
		}
		return -1;
	}

	private boolean isEmpty(String colName) {
		return 0 == numValidValue(colName);
	}

}

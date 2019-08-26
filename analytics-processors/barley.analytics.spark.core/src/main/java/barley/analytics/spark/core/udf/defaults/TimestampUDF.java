/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package barley.analytics.spark.core.udf.defaults;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * This class is an UDF class to support Spark SQL UDFs
 * It returns the epoch time value (of type long) of a date string
 */
public class TimestampUDF {
	private final Calendar cal = Calendar.getInstance();

	public long timestamp(String timestampString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss zzz");
		Date date = null;
		try {
			date = sdf.parse(timestampString);
		} catch (ParseException e) {
			sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			try {
				date = sdf.parse(timestampString);
			} catch (ParseException e1) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				date = sdf.parse(timestampString);
			}
		}
		long timeInMillisSinceEpoch = date.getTime();
		return timeInMillisSinceEpoch;
	}
	
	// (추가) 2019.08.20 - 소스가 존재하지 않아 구글 검색하여 가져옴. 
	// 소스 출처 - https://github.com/wso2/shared-analytics/blob/master/components/spark-udf/org.wso2.carbon.analytics.shared.spark.common.udf/src/main/java/org/wso2/carbon/analytics/shared/common/udf/DateTimeUDF.java
	public String getHourStartingTime(Integer year, Integer month, Integer date, Integer hour) {		
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, date);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }

}

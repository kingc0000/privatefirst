/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.kekeinfo.web.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kekeinfo.core.constants.Constants;



public class DateUtil {

	private Date startDate = new Date(new Date().getTime());
	private Date endDate = new Date(new Date().getTime());
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	private final static String LONGDATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

	
	
	/**
	 * Generates a time stamp
	 * yyyymmddhhmmss
	 * @return
	 */
	public static String generateTimeStamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
		return format.format(new Date());
	}
	
	public static BigDecimal sunDate(Date date1,Date date2){
		BigDecimal ns =new BigDecimal(date1.getTime()-date2.getTime());
		//小时
		BigDecimal nh =new BigDecimal( 1000*60*60);
		return ns.divide(nh,3, BigDecimal.ROUND_UP);
	}
	
	/**
	 * yyyy-MM-dd
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDate(Date dt) {

		if (dt == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		return format.format(dt);

	}
	
	public static Date timeDate(String time) throws ParseException{
		if (time == null || time=="")
			return null;
		Date date =new Date();
		SimpleDateFormat format1 = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		time =format1.format(date)+" "+time;
		SimpleDateFormat format = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		
		return format.parse(time);
	}
	
	public static Date timeDate(Date dt) throws ParseException{
		if (dt == null )
			return null;
		SimpleDateFormat format = new SimpleDateFormat(Constants.LONG_DATE_FORMAT);
		
		return format.parse(formatDate(dt));
	}
	
	public static String formatDate(Date dt, String format) {
		if (dt == null)
			return null;
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(dt);
	}
	
	public static String formatYear(Date dt) {

		if (dt == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_YEAR);
		return format.format(dt);

	}
	
	public static String formatLongDate(Date date) {
		
		if (date == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(LONGDATE_FORMAT);
		return format.format(date);
		
	}

	/**
	 * yy-MMM-dd
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDateMonthString(Date dt) {

		if (dt == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		return format.format(dt);

	}

	public static Date getDate(String date) throws Exception {
		DateFormat myDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		return myDateFormat.parse(date);
	}

	public static Date addDaysToCurrentDate(int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, days);
		return c.getTime();

	}

	public static Date getDate() {

		return new Date(new Date().getTime());

	}

	public static String getPresentDate() {

		Date dt = new Date();

		SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		return format.format(new Date(dt.getTime()));
	}

	public static String getPresentYear() {

		Date dt = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(new Date(dt.getTime()));
	}

	public void processPostedDates(HttpServletRequest request) {
		Date dt = new Date();
		DateFormat myDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date sDate = null;
		Date eDate = null;
		try {
			if (request.getParameter("startdate") != null) {
				sDate = myDateFormat.parse(request.getParameter("startdate"));
			}
			if (request.getParameter("enddate") != null) {
				eDate = myDateFormat.parse(request.getParameter("enddate"));
			}
			this.startDate = sDate;
			this.endDate = eDate;
		} catch (Exception e) {
			LOGGER.error("",e);
			this.startDate = new Date(dt.getTime());
			this.endDate = new Date(dt.getTime());
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public static Date addDay(Date dt, int n) {   
		return offsetDay(Calendar.DATE, dt, n);
    }
	
	public static Date offsetDay(int type, Date dt, int n) {   
        try {   
            Calendar cd = Calendar.getInstance();   
            cd.setTime(dt);   
            cd.add(type, n);//增加一天   
            //cd.add(Calendar.MONTH, n);//增加一个月   
            return cd.getTime();
        } catch (Exception e) {   
            return null;   
        }   
    }
}

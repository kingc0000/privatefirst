package com.kekeinfo.core.constants;

import java.util.Currency;
import java.util.Locale;

public class Constants {
	
	public final static String TEST_ENVIRONMENT= "TEST";
	public final static String PRODUCTION_ENVIRONMENT= "PRODUCTION";
	public static final String ALL_REGIONS = "*";
	public static final String DEFAULTSTORE="DEFAULT";
	
	
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DEFAULT_DATE_FORMAT_YEAR = "yyyy";
	public final static String DEFAULT_DATE_FORMAT_TIME = "HH:mm:ss";
	
	public final static String EMAIL_CONFIG = "EMAIL_CONFIG";
	public final static String DREPORT_CONFIG = "DREPORT_CONFIG"; //设计报告审核/审定人配置
	
	public final static String UNDERSCORE = "_";
	public final static String SLASH = "/";
	public final static String TRUE = "true";
	public final static String FALSE = "false";
	public final static String OT_ITEM_PRICE_MODULE_CODE = "itemprice";
	public final static String OT_SUBTOTAL_MODULE_CODE = "subtotal";
	public final static String OT_TOTAL_MODULE_CODE = "total";
	public final static String OT_SHIPPING_MODULE_CODE = "shipping";
	public final static String OT_HANDLING_MODULE_CODE = "handling";
	
	public final static String OT_REFUND_MODULE_CODE = "refund";
	
	public final static Locale DEFAULT_LOCALE = Locale.US;
	public final static Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.US);
	
	public final static Integer BATCH_SIZE = 50; //数据库批量操作数量
	public final static String  MPOINT_SURFACE = "Surface";
	public final static String  MPOINT_BUILDING = "Building";
	public final static String  MPOINT_HIDDENLINE = "HiddenLine";
	public final static String  MPOINT_HORDISPLACE = "HorDisplace";
	public final static String  MPOINT_RINGBEAM = "RingBeam";
	public final static String  MPOINT_SUPAXIAL = "SupAxial";
	public final static String  MPOINT_UPRIGHT = "UpRight";
	public final static String  MPOINT_WATERLINE = "WaterLine";
}

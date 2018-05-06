package com.kekeinfo.web.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;

public class FormulaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FormulaService.class);


	private static FormulaService instance=new FormulaService();
	public static FormulaService getInstance(){
		return instance;
	}
	HashMap<String, Expression> expressMap=new HashMap<String, Expression>();
	private Object lock=new Object();
	private FormulaService(){
	}
	
	public void regExpress(String express) throws ExpressionSyntaxErrorException{
		synchronized (lock) {
			Expression compile = (Expression) AviatorEvaluator.compile(express);
			expressMap.put(express, compile);	
		}
	}
	
	/**
	 * 
	 * @param param	参数  
	 * @param express 表达式
	 * @return
	 * @throws FormualException
	 */
	public Double execute(Map<String, Object> param, String express)throws Exception {
		Expression expression = null ;
		synchronized (lock) {
			try {
				expression = expressMap.get(express);
				if (expression == null) {
					expression = (Expression) AviatorEvaluator.compile(express);
					expressMap.put(express, expression);
					LOGGER.error(express + "没有被注册过");
				}
			} catch (Exception ex) {
				LOGGER.error(express+"解析失败",ex);
			}
		}
		if(expression ==null){
			throw new Exception(express+"解析失败");
		}
		Object execute = expression.execute(param);
/*		if(createTick.tickNow()>10){
//			logger.error(express+"公式解析时间过长 请优化 耗时"+createTick.formatString());
		}
*/		return (Double) execute;
	}
	
	public static void main(String agrs[]){
		HashMap<String, Object> sss=new HashMap<String, Object>();
		sss.put("伙伴攻击力", 10);
		sss.put("技能等级", 1000l);
		long currentTimeMillis = System.currentTimeMillis();
		Double compile = null;
		try {
			compile = FormulaService.getInstance().execute(sss, "伙伴攻击力*(1+0.3*技能等级)");
			System.out.println(compile.doubleValue());;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-currentTimeMillis);
	}
}

package com.serotonin.modbus4j.base;

import java.io.Serializable;
import java.math.BigDecimal;

public class DataModal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 501209108247616356L;
	
	private Long businessId; //业务对象id
	private String businessName; //业务对象name
	private String serialNo; //网关序列号
	private Integer node1; //节点
	private Integer channel1; //通道
	private Integer node2; //节点
	private Integer channel2; //通道
	private BigDecimal value1; //采集数据值
	private BigDecimal value2; //采集数据值
	private String formula; //转换公式
	
	
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public Integer getNode1() {
		return node1;
	}
	public void setNode1(Integer node1) {
		this.node1 = node1;
	}
	public Integer getChannel1() {
		return channel1;
	}
	public void setChannel1(Integer channel1) {
		this.channel1 = channel1;
	}
	public Integer getNode2() {
		return node2;
	}
	public void setNode2(Integer node2) {
		this.node2 = node2;
	}
	public Integer getChannel2() {
		return channel2;
	}
	public void setChannel2(Integer channel2) {
		this.channel2 = channel2;
	}
	public BigDecimal getValue1() {
		return value1;
	}
	public void setValue1(BigDecimal value1) {
		this.value1 = value1;
	}
	public BigDecimal getValue2() {
		return value2;
	}
	public void setValue2(BigDecimal value2) {
		this.value2 = value2;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getBaseinfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("businessName:").append(businessName).append(", serialNo:").append(serialNo).append(", node1:").append(node1)
		.append(", channel1:").append(channel1).append(", node2:").append(node2).append(", channel2:").append(channel2);
		return sb.toString();
	}
}

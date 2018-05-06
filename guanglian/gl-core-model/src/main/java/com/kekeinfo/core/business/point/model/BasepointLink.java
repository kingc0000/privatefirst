package com.kekeinfo.core.business.point.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

/**
 * 数据配置，配置测点与施工现场设备之间的对应关系
 * 降水井：node1和channel1对应流量配置，node2和channel2对应水位配置，node3和channel3对应测点的启停配置，node4和channel4对应测点的断电告警配置
 * 疏干井：node1和channel1对应流量配置，node2和channel2对应水位配置，node3和channel3对应测点的启停配置，node4和channel4对应测点的断电告警配置
 * 回灌井：node1和channel1对应流量配置，node2和channel2对应井内水位配置，node3和channel3对应测点的启停配置
 * 观测井：node1和channel1对应水位配置，node2和channel2对应水温配置
 * 环境测点：node1和channel1对应测点的观察值配置
 * @author sam
 *
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasepointLink<E> extends KekeinfoEntity<Long, BasepointLink<E>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="GATEWAY_ID", nullable=true)
	private Gateway gateway; //所属网关
	
	@JsonIgnore
	@OneToOne(mappedBy="pointLink", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private E point;//关联测点
	
	@Column(name="NODE_1", nullable = true, length=2)
	private Integer node1; //节点号，一个网关下64个节点
	
	@Column(name="CHANNEL_1", nullable = true, length=1)
	private Integer channel1; //通道，本次采用4路通道
	
	@Column(name="NODE_2", nullable = true, length=2)
	private Integer node2; //节点号，一个网关下64个节点
	
	@Column(name="CHANNEL_2", nullable = true, length=1)
	private Integer channel2; //通道，本次采用4路通道
	
	@Column(name="NODE_3", nullable = true, length=2)
	private Integer node3; //节点号，一个网关下64个节点
	
	@Column(name="CHANNEL_3", nullable = true, length=1)
	private Integer channel3; //通道，本次采用4路通道
	
	@Column(name="NODE_4", nullable = true, length=2)
	private Integer node4; //节点号，一个网关下64个节点
	
	@Column(name="CHANNEL_4", nullable = true, length=1)
	private Integer channel4; //通道，本次采用4路通道

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

	public Integer getNode3() {
		return node3;
	}

	public void setNode3(Integer node3) {
		this.node3 = node3;
	}

	public Integer getChannel3() {
		return channel3;
	}

	public void setChannel3(Integer channel3) {
		this.channel3 = channel3;
	}

	public Integer getNode4() {
		return node4;
	}

	public void setNode4(Integer node4) {
		this.node4 = node4;
	}

	public Integer getChannel4() {
		return channel4;
	}

	public void setChannel4(Integer channel4) {
		this.channel4 = channel4;
	}
	
	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public E getPoint() {
		return point;
	}

	public void setPoint(E point) {
		this.point = point;
	}
}

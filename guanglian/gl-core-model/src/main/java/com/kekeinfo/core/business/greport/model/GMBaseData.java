package com.kekeinfo.core.business.greport.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GMBaseData<E> extends KekeinfoEntity<Long, GMBaseData<E>> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8124815162468472464L;

	/**
	 * 
	 */
	// zh:点号
		@Column(name = "MARKNO")
		private String markNO;
		
		// zh:初始高程
		@Column(name = "CURTHEIGHT")
		private BigDecimal curHeight;
		// zh:总累计位移
		@Column(name = "SUMTHEIGHT")
		private BigDecimal sum;	
		
		// zh:上次计位移
		@Column(name = "LASTTHEIGHT")
		private BigDecimal last;	
		
		@JsonIgnore
		@ManyToOne(targetEntity = Greport.class)
		@JoinColumn(name = "GREPORT_ID", nullable = false)
		private Greport greport;

		public String getMarkNO() {
			return markNO;
		}

		public void setMarkNO(String markNO) {
			this.markNO = markNO;
		}

		public BigDecimal getCurHeight() {
			return curHeight;
		}

		public void setCurHeight(BigDecimal curHeight) {
			this.curHeight = curHeight;
		}

		public BigDecimal getSum() {
			return sum;
		}

		public void setSum(BigDecimal sum) {
			this.sum = sum;
		}

		public Greport getGreport() {
			return greport;
		}

		public void setGreport(Greport greport) {
			this.greport = greport;
		}

		

		public BigDecimal getLast() {
			return last;
		}

		public void setLast(BigDecimal last) {
			this.last = last;
		}

}

package com.kekeinfo.core.business.monitor.statistical.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.MBaseType;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.model.WaterLineType;
import com.kekeinfo.core.business.monitor.service.MBaseTypeService;
import com.kekeinfo.core.business.monitor.service.WaterLineTypeService;
import com.kekeinfo.core.business.monitor.statistical.dao.MstatisticalDao;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;
import com.kekeinfo.core.business.monitor.statistical.model.PointManager;

@Service("mstatisticalService")
public class MstatisticalServiceImpl extends KekeinfoEntityServiceImpl<Long, Mstatistical>
		implements MstatisticalService {
	private MstatisticalDao mstatisticalDao;

	@Autowired MBaseTypeService mBaseTypeService;
	@Autowired WaterLineTypeService waterLineTypeService;
	@Autowired
	public MstatisticalServiceImpl(MstatisticalDao mstatisticalDao) {
		super(mstatisticalDao);
		this.mstatisticalDao = mstatisticalDao;
	}

	@Transactional
	@Override
	public void saveUpdate(Mstatistical mstatistical,List<BasedataType> blist) throws ServiceException {

		Set<PointManager> pmanagers = new HashSet<>();
		//获取基础数据警戒值
		List<MBaseType> ms =mBaseTypeService.getByMid(mstatistical.getMonitor().getId());
		List<WaterLineType> ws =waterLineTypeService.getByMid(mstatistical.getMonitor().getId());
		BigDecimal diaval=null;
		BigDecimal sumval=null;
		String memo="";
		// 获取各测点的最大值
		for (MPointEnumType e : MPointEnumType.values()) {
			List<String> p_attributes = new ArrayList<String>();
			p_attributes.add(e.toString().toUpperCase() + "_ID");
			List<Object[]> mps = null;
			if (e.equals(MPointEnumType.WaterLine)) {
				if(blist!=null && blist.size()>0){
					for(BasedataType bt:blist){
						mps = super.getBySql(p_attributes, "M" + e.toString().toUpperCase(),
								" where MONITOR_ID = " + mstatistical.getMonitor().getId() + " AND MARKNO LIKE '"+bt.getValue()+"%'");
						for(WaterLineType w:ws){
							if(w.getBaseType().getId().equals(bt.getId())){
								diaval=w.getDailyVar();
								sumval=w.getTotalValue();
								memo=w.getMemo();
								break;
							}
						}
						
						setmange(mps, e, mstatistical, pmanagers," '"+bt.getValue()+"%'",diaval,sumval,memo);
					}
				}
			} else {
				// 获取测点ID
				mps = super.getBySql(p_attributes, "M" + e.toString().toUpperCase(),
						" where MONITOR_ID = " + mstatistical.getMonitor().getId() + " ");
				for(MBaseType m:ms){
					if(m.getMtype().equals(e)){
						diaval=m.getDailyVar();
						sumval=m.getTotalValue();
						memo=m.getMemo();
					}
				}
				setmange(mps, e, mstatistical, pmanagers,null,diaval,sumval,memo);
			}

		}
		mstatistical.setPmanagers(pmanagers);
		super.saveOrUpdate(mstatistical);

	}

	private void setmange(List<Object[]> mps, MPointEnumType e, Mstatistical mstatistical,
			Set<PointManager> pmanagers,String linetype,BigDecimal diaval,BigDecimal sumval,String memo) {
		if (mps != null && mps.size() > 0) {
			Date date = mstatistical.getmDaily().getDatec();
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
			StringBuffer mids = new StringBuffer();
			for (Object mid : mps) {
				mids.append(mid.toString()).append(",");
			}
			mids.deleteCharAt(mids.lastIndexOf(","));
			List<String> attributes = new ArrayList<String>();
			attributes.add(" POINT.MARKNO ");
			attributes.add(" MAX(DATA.CURTHEIGHT-DATA.INITHEIGHT) AS MV");
			String table = " " + "M" + e.toString().toUpperCase() + " AS POINT," + e.toString().toUpperCase()
					+ "DATA AS DATA";
			String where = null;

			List<String> attributes1 = null;
			if(!e.getType().equalsIgnoreCase("SupAxial")){
				attributes1 =new ArrayList<String>();
				attributes1.add(" POINT.MARKNO ");
				attributes1.add(" MAX(DATA.CURTHEIGHT-POINT.INITHEIGHT) AS MV");
			}else{
				attributes1 =new ArrayList<String>();
				attributes1.add(" POINT.MARKNO ");
				attributes1.add(" MAX(DATA.CURTHEIGHT) AS MV");
			}
					
			
			List<String> attributes2 =null;
			if(!e.getType().equalsIgnoreCase("SupAxial")){
				 attributes2 = new ArrayList<String>();
				attributes2.add(" POINT.MARKNO ");
				attributes2.add(" MAX(POINT.FRONTDISPLACEMENT) AS MV");
			}
			
			
			where = " where DATA." + e.toString().toUpperCase() + "_ID in (" + mids.toString()
			+ ") AND DATA.CALIBRATION BETWEEN '" + dateFormater.format(date) + " 00:00:00' AND '"
			+ dateFormater.format(date) + " 23:59:59' " + " AND POINT." + e.toString().toUpperCase() + "_ID=DATA."+e.toString().toUpperCase()+ "_ID" ;
			if(linetype!=null){
				where+=" AND POINT.MARKNO LIKE"+linetype;
			}
			
			where+= " GROUP BY POINT.MARKNO ORDER BY MV desc limit 1";
			/**
			if (e.equals(MPointEnumType.WaterLine)) {
				where = " where DATA." + e.toString().toUpperCase() + "_ID in (" + mids.toString()
						+ ") AND DATA.CALIBRATION BETWEEN '" + dateFormater.format(date) + " 00:00:00' AND '"
						+ dateFormater.format(date) + " 23:59:59' " + " AND POINT." + e.toString().toUpperCase() + "_ID=DATA."
						+ e.toString().toUpperCase() + "_ID";
			} else {
				where = " where DATA." + e.toString().toUpperCase() + "_ID in (" + mids.toString()
						+ ") AND DATA.CALIBRATION BETWEEN '" + dateFormater.format(date) + " 00:00:00' AND '"
						+ dateFormater.format(date) + " 23:59:59'" + "  AND POINT."
						+ e.toString().toUpperCase() + "_ID=DATA." + e.toString().toUpperCase() + "_ID";
			}*/
			//System.out.println("测点："+e.getName());
			// 获取测点名称和最大变化量
			List<Object[]> mds = super.getBySql(attributes, table, where);
			// 获取测点名称和总累计最大变化量
			List<Object[]> mds1 = super.getBySql(attributes1, table, where);
			// 获取测点名称和开挖前最大变化量
			List<Object[]> mds2 =null;
			if(attributes2!=null){
				mds2 = super.getBySql(attributes2, table, where);
			}

			if (mds != null && mds.size() > 0 && mds1 != null && mds1.size() > 0) {
				PointManager pm = new PointManager();
				//最大变化
				pm.setPointNo(mds.get(0)[0].toString());
				pm.setCurMaxVar(mds.get(0)[1]==null?null:new BigDecimal(mds.get(0)[1].toString()));
				pm.setTpointNo(mds1.get(0)[0].toString());
				pm.setTotalMaxVar(mds1.get(0)[1]==null?null:new BigDecimal(mds1.get(0)[1].toString()));
				//开挖前最大
				if(mds2!=null && mds2.size()>0  && mds2.get(0)[1]!=null){
					pm.setEpointNo(mds2.get(0)[0].toString());
					pm.setEarlyMaxVar(new BigDecimal(mds2.get(0)[1].toString()));
				}
				//预警线
				pm.setDailyVar(diaval);
				pm.setTotalValue(sumval);
				pm.setMemo(memo);
				pm.setMstatistical(mstatistical);
				pm.setpType(e.toString());
				pmanagers.add(pm);
			}
		}
	}

	@Override
	public Mstatistical getByDate(Date date) throws ServiceException {
		// TODO Auto-generated method stub
		return this.mstatisticalDao.getByDate(date);
	}

}

package com.kekeinfo.core.business.xreport.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.oblique.model.Depth;
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;
import com.kekeinfo.core.business.monitor.oblique.service.DepthService;
import com.kekeinfo.core.business.monitor.oblique.service.ObliqueDataService;
import com.kekeinfo.core.business.monitor.oblique.service.ObliqueService;
import com.kekeinfo.core.business.xreport.dao.XMreportDao;
import com.kekeinfo.core.business.xreport.model.ROblique;
import com.kekeinfo.core.business.xreport.model.RObliqueData;
import com.kekeinfo.core.business.xreport.model.XMreport;

@Service("xmreportService")
public class XMreportServiceImpl extends KekeinfoEntityServiceImpl<Long, XMreport> implements XMreportService {
	@SuppressWarnings("unused")
	private XMreportDao xmreportDao;

	@Autowired ObliqueService obliqueService;
	@Autowired ObliqueDataService obliqueDataService;
	@Autowired DepthService depthService;
	@Autowired RObliqueService  rObliqueService;
	
	@Autowired
	public XMreportServiceImpl(XMreportDao xmreportDao) {
		super(xmreportDao);
		this.xmreportDao = xmreportDao;
	}

	@Transactional
	@Override
	public void createXreport(XMreport xr) throws ServiceException {
		// TODO Auto-generated method stub
		List<Oblique> obs =obliqueService.getByMid(xr.getMonitor().getId());
		if(obs!=null && obs.size()>0){
			Set<ROblique>roset=new HashSet<>();
			 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			for(Oblique ob:obs){
				ROblique ro=new ROblique();
				ro.setNamber(ob.getNamber());
				ro.setEvaluate(ob.getEvaluate());
				ro.setHorcorrect(ob.getHorcorrect());
				ro.setXmreport(xr);
				List<Depth> dps =depthService.getByOid(ob.getId());
				if(dps!=null && dps.size()>0){
					Set<RObliqueData>rset=new HashSet<>();
					Map<Long,RObliqueData> maps =new HashMap<>();
					StringBuffer ids =new StringBuffer();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					for(Depth dp:dps){
						ids.append(dp.getId()).append(",");
					}
					ids.deleteCharAt(ids.lastIndexOf(","));
					List<String> attributes = new ArrayList<String>();
					attributes.add("DEPTH_ID");
					attributes.add("curTotal");
					attributes.add("lastTotal");
					attributes.add("CURDATE");
					String table = " OBLIQUEDATA" ;
					String where = " where DEPTH_ID in( "+ids.toString()+" ) AND CURDATE BETWEEN '"
					 +dateFormat.format(xr.getThiser()) + " 00:00:00' AND '"
					 + dateFormat.format(xr.getThiser()) + " 23:59:59' "
					+ "ORDER BY CURDATE DESC";
					List<Object[]> mds = super.getBySql(attributes, table, where);
					if(mds!=null && mds.size()>0){
						for(Object[] md:mds){
							if(md[0]!=null){
								try{
									Long did =Long.parseLong(md[0].toString());
									Depth dp1=null;
									for(Depth dp:dps){
										if(dp.getId().equals(did)){
											dp1=dp;
											break;
										}
									}
									if(dp1!=null){
										RObliqueData mapd =maps.get(dp1.getId());
										if(mapd==null){
											RObliqueData obd =new RObliqueData();
											obd.setDepth(dp1.getDeep());
											obd.setCurTotal(md[1]==null?null:new BigDecimal(md[1].toString()));
											obd.setLastTotal(md[2]==null?null:new BigDecimal(md[2].toString()));
											if(md[3]!=null){
												String cdate=md[3].toString();
												cdate=cdate.substring(0,cdate.length()-2);
												obd.setCurDate(sdf.parse(cdate));
											}
											obd.setrObliqu(ro);
											rset.add(obd);
											maps.put(dp1.getId(), obd);
										}
									}
								}catch (Exception e){
									e.printStackTrace();
								}
							}
						}
					}
					ro.setrDepth(rset);
				}
				roset.add(ro);
			}
			xr.setrDepth(roset);
		}
		super.save(xr);
	}

	@Transactional
	@Override
	public void deleteXReport(XMreport xr) throws ServiceException {
		// TODO Auto-generated method stub
		List<ROblique> ords = rObliqueService.getByXid(xr.getId());
		if(ords!=null && ords.size()>0){
			for(ROblique or:ords){
				rObliqueService.delete(or);
			}
		}
		super.delete(xr);
	}
}

package com.kekeinfo.web.utils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.report.model.Rdewell;
import com.kekeinfo.core.business.report.model.Report;
import com.kekeinfo.core.business.report.model.Rewell;
import com.kekeinfo.core.business.report.model.Riwell;
import com.kekeinfo.core.business.report.model.Rowell;
import com.kekeinfo.core.business.report.model.Rpwell;
import com.kekeinfo.core.business.report.service.ReportService;

/**
 * 生成报表
 * @author yong chen
 *
 */
@Component
public class ReportUtils {
	@Autowired private CSiteService cSiteService;
	@Autowired private ReportService reportService;
	@Autowired private PumpwellService  pumpwellService;
	@Autowired private DewateringService dewateringService;
	@Autowired private InvertedwellService  invertedwellService;
	@Autowired private ObservewellService observewellService;
	@Autowired private DeformmonitorService  deformmonitorService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtils.class);
	public Report createReport(Long cid) throws ServiceException{
		
		//ConstructionSite csite = cSiteService.getByCidWithALLWell(cid);
		List<Pumpwell> pwells = pumpwellService.getByCid(cid);
		Report report = new Report();
		if(!pwells.isEmpty()){
			Set<Rpwell> rps = new HashSet<>();
			for(Pumpwell p:pwells){
				Rpwell rp = new Rpwell();
				rp.setLastAccu(p.getLastAccu());
				rp.setThisAccu(p.getThisAccu());
				rp.setFlow(p.getrFlow());
				rp.setWater(p.getrWater());
				rp.setDataStatus(p.getDataStatus());
				rp.setPowerStatus(p.getPowerStatus());
				rp.setPrecipitation(p.getPointInfo().getPrecipitation());
				rp.setName(p.getName());
				rp.setReport(report);
				rps.add(rp);
			}
			report.setPwells(rps);
		}
		LOGGER.warn("pump ......");
		//dewell
		List<Dewatering> dewells = dewateringService.getByCid(cid);
		if(!dewells.isEmpty()){
			Set<Rdewell> rdes = new HashSet<>();
			for(Dewatering p:dewells){
				Rdewell rp = new Rdewell();
				rp.setLastAccu(p.getLastAccu());
				rp.setThisAccu(p.getThisAccu());
				rp.setFlow(p.getrFlow());
				rp.setWater(p.getrWater());
				rp.setDataStatus(p.getDataStatus());
				rp.setPowerStatus(p.getPowerStatus());
				rp.setPrecipitation(p.getPointInfo().getPrecipitation());
				rp.setName(p.getName());
				rp.setReport(report);
				rdes.add(rp);
			}
			report.setDewells(rdes);
		}
		LOGGER.warn("dewell ......");
		//iwell
		List<Invertedwell> iwells = invertedwellService.getBycid(cid);
		if(!iwells.isEmpty()){
			Set<Riwell> ris = new HashSet<>();
			for(Invertedwell p:iwells){
				Riwell rp = new Riwell();
				rp.setLastAccu(p.getLastAccu());
				rp.setThisAccu(p.getThisAccu());
				rp.setFlow(p.getrFlow());
				rp.setPressure(p.getrPressure());
				rp.setDataStatus(p.getDataStatus());
				rp.setPowerStatus(p.getPowerStatus());
				rp.setPrecipitation(p.getPointInfo().getPrecipitation());
				rp.setName(p.getName());
				rp.setReport(report);
				ris.add(rp);
			}
			report.setIwells(ris);
		}
		LOGGER.warn("iwell ......");
		//owell
		List <Observewell> owells = observewellService.getBycid(cid);
		if(!owells.isEmpty()){
			Set<Rowell> ros = new HashSet<>();
			for(Observewell p:owells){
				Rowell rp = new Rowell();
				rp.setTemperature(p.getrTemperature());
				rp.setWater(p.getrWater());
				rp.setWaterDwon(p.getWaterDwon());
				rp.setWaterThreshold(p.getWaterMeasurement());
				rp.setDataStatus(p.getDataStatus());
				rp.setPowerStatus(p.getPowerStatus());
				rp.setPrecipitation(p.getPointInfo().getPrecipitation());
				rp.setName(p.getName());
				rp.setReport(report);
				ros.add(rp);
			}
			report.setOwells(ros);
		}
		LOGGER.warn("ewll ......");
		//ewell
		List<Deformmonitor> ewells = deformmonitorService.getBycid(cid);
		if(!ewells.isEmpty()){
			Set<Rewell> res = new HashSet<>();
			for(Deformmonitor p:ewells){
				Rewell rp = new Rewell();
				rp.setData(p.getrData());
				rp.setLastData(p.getlData());
				rp.setDataStatus(p.getDataStatus());
				rp.setPowerStatus(p.getPowerStatus());
				rp.setThreshold(p.getDeformData());
				rp.setName(p.getName());
				rp.setReport(report);
				res.add(rp);
			}
			report.setEwells(res);
		}
		LOGGER.warn("owell ......");
		ConstructionSite csite = cSiteService.getByCid(cid);
		LOGGER.warn("csite ......");
		report.setrDate(new Date());
		report.setOwner(csite.getProject().getProjectOwner());
		report.setName(csite.getProject().getName());
		report.setcSite(csite);
		report.getAuditSection().setModifiedBy("AUTO");
		//Daily daily = dailyService.getby
		reportService.saveOrUpdate(report);
		return report;
		
	}	
}

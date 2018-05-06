package com.kekeinfo.core.business.daily.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.daily.dao.DailyDao;
import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.model.DailyImage;
import com.kekeinfo.core.business.daily.model.WellCondition;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service("dailyService")
public class DailyServiceImpl extends KekeinfoEntityServiceImpl<Long, Daily> implements
		DailyService {
	
	private DailyDao dailyDao;
	@Autowired
	private ContentService contentService;
	@Autowired DewateringService deService;
	@Autowired DeformmonitorService ewllService;
	@Autowired InvertedwellService iwellService;
	@Autowired ObservewellService owellService;
	@Autowired PumpwellService pwellService;
	
	@Autowired
	public DailyServiceImpl(DailyDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	private int getInt(String s){
		int r=0;
		try{
			r=Integer.parseInt(s);
		}catch(Exception e){
			
		}
		return r;
	}
	
	public void save(Daily daily, String delImageIds) throws ServiceException {
		
		List<String> delFilenames = new ArrayList<String>();
		//处理wellCondition
		String[] designs = daily.getDesignQua();
		if(designs!=null && designs.length>0){
			Set<WellCondition> wcs = new HashSet<>();
			for(int i=0;i<designs.length;i++ ){
				WellCondition wc = new WellCondition();
				wc.setDesignQua(this.getInt(designs[i]));
				
				String[] wtypes = daily.getwType();
				wc.setwType(this.getInt(wtypes[i]));
				String[] wellIds =daily.getWellIds();
				wc.setWellIds(wellIds[i]);
				//设置完工状态
				//疏干井,降水井,回灌井,观测井,监测点
				if(wellIds.length>0){
					String wids = wellIds[i];
					String[] wa =wids.split(",");
					for(String s:wa){
						if(StringUtils.isNotBlank(s)){
							switch (wtypes[i]) {
							//梳干井
							case "0":
								Dewatering de =deService.getById(Long.parseLong(s));
								de.setDone(true);
								deService.saveOrUpdate(de);
								break;
							//降水井
							case "1":
								Pumpwell p =pwellService.getById(Long.parseLong(s));
								p.setDone(true);
								pwellService.saveOrUpdate(p);
								break;
							case "2":
								Invertedwell iw = iwellService.getById(Long.parseLong(s));
								iw.setDone(true);
								iwellService.saveOrUpdate(iw);
								break;
							case "3":
								Observewell o = owellService.getById(Long.parseLong(s));
								o.setDone(true);
								owellService.saveOrUpdate(o);
								break;
							case "4":
								Deformmonitor e =ewllService.getById(Long.parseLong(s));
								e.setDone(true);
								ewllService.saveOrUpdate(e);
								break;
							}
						}
					}
				}
				String[] planCmps =daily.getPlanCmp();
				wc.setPlanCmp(this.getInt(planCmps[i]));
				String[] cumCmps =daily.getCumCmp();
				wc.setCumCmp(this.getInt(cumCmps[i]));
				String[] dests =daily.getDest();
				wc.setDest(dests[i]);
				String[] memos =daily.getMemo();
				wc.setMemo(memos[i]);
				wc.setDaily(daily);
				wcs.add(wc);
			}
			daily.setWellCon(wcs);
		}
		//新增图片
		Set<DailyImage> images = daily.getDailyImages();
		if(images!=null && images.size()>0) {
			for(DailyImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {//新增图片
			        InputStream inputStream = image.getImage();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( image.getDailyImage());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.DAILY);
			        contentService.addContentFile(cmsContentImage);
			        if(image.getJdigital()!=null){
						InputContentFile cmsContentFilej = new InputContentFile();
						cmsContentFilej.setFileName(image.getJpeg());
						cmsContentFilej.setFileContentType( FileContentType.DAILY );
						cmsContentFilej.setFile(image.getJdigital() );
						contentService.addContentFile(cmsContentFilej);
					}
				}
			}
		}
		//获取数据库的最近一条记录
		List<String> attributes = new ArrayList<String>();
		attributes.add("cSite.id");
		List<String> fieldValues = new ArrayList<String>();
		fieldValues.add(daily.getcSite().getId().toString());
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("datec", "desc");
		Entitites<Daily> list  = this.getPageListByAttributes(attributes, fieldValues, 1, null, orderby);
		Daily dbDaily = null;
		if(list!=null && list.getEntites().size()>0){
			dbDaily=list.getEntites().get(0);
			try {
				if(this.DateEq(daily.getDatec(), dbDaily.getDatec())){
					daily.setId(dbDaily.getId());
					 
					
					//如果之前已经将某些井设置为完工状态，现在要改回来
					Set<WellCondition> wcs = dbDaily.getWellCon(); 
					for(WellCondition wc:wcs){
						for(WellCondition w:daily.getWellCon()){
							if(wc.getwType()==w.getwType()){
								if(StringUtils.isNotBlank(wc.getWellIds())){
									String[] wcids =wc.getWellIds().split(","); //旧的完工井ids集合
									String[] wids=w.getWellIds().split(","); //新的完工井ids集合
									@SuppressWarnings("unchecked")
									Set<String> widSet = new HashSet<String>(Arrays.asList(wids));
									
									List<String> lids=new ArrayList<>();
									for(String s:wcids){ //判断旧的完工井ids是否在新的完工井ids中
										if(!widSet.contains(s)) { //如果不存在，则需要重置为未完工状态
											lids.add(s);
										}
									}
									//查找到之前设置的id
									if(lids.size()>0){
										//疏干井,降水井,回灌井,观测井,监测点
										for(String sid:lids){
											switch (wc.getwType()) {
											case 0:
												Dewatering de =deService.getById(Long.parseLong(sid));
												de.setDone(false);
												deService.saveOrUpdate(de);
												break;
											case 1:
												Pumpwell p = pwellService.getById(Long.parseLong(sid));
												p.setDone(false);
												pwellService.saveOrUpdate(p);
												break;
											case 2:
												Invertedwell iw=iwellService.getById(Long.parseLong(sid));
												iw.setDone(false);
												iwellService.saveOrUpdate(iw);
												break;
											case 3:
												Observewell o = owellService.getById(Long.parseLong(sid));
												o.setDone(false);
												owellService.saveOrUpdate(o);
												break;
											case 4:
												Deformmonitor e =ewllService.getById(Long.parseLong(sid));
												e.setDone(false);
												ewllService.saveOrUpdate(e);
												break;
											}
										}
									}
								}
								
								break;
							}
						}
						
					}
					
					//处理之前的图片
					Set<DailyImage> imageSet = dbDaily.getDailyImages();
					for(DailyImage image:imageSet){
						boolean isadd=true;
						if (!StringUtils.isBlank(delImageIds)) {
							String[] dels = delImageIds.split("#");
							if (dels!=null && dels.length>0) {
								for (int i = 0; i < dels.length; i++) {
									if(image.getId().toString().equals(dels[i])) {
										delFilenames.add(image.getDailyImage());
										isadd=false;
									}
								}
							}
						}
						if(isadd){
							daily.getDailyImages().add(image);
						}
					}
					
				
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		super.saveOrUpdate(daily);
		//删除不要的日志图片
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.DAILY, delFilenames.get(i));
			}
		}
	}
	
	@Override
	public void deleteById(Object id) {
		Daily daily = super.getById((Long) id);
		try {
			super.delete(daily);
			//删除附件
			Set<DailyImage> images = daily.getDailyImages();
			for (DailyImage image : images) {
				contentService.removeFile(FileContentType.DAILY, image.getDailyImage());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	private boolean DateEq(Date date1,Date date2) throws ParseException{
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar cal = Calendar.getInstance();  
		 String d1=dateFormat.format(date1);
		 String d2=dateFormat.format(date2);
		 cal.setTime(dateFormat.parse(d1));
		 long time1 =cal.getTimeInMillis();
		 cal.setTime(dateFormat.parse(d2));
		long time2 = cal.getTimeInMillis();
		if(time1==time2){
			return true;
		}else{
			return false;
		}
		
	}
	@Override
	public Daily getByDate(Long pid, Date date1) throws ServiceException {
		// TODO Auto-generated method stub
		return dailyDao.getByDate(pid, date1);
	}
}

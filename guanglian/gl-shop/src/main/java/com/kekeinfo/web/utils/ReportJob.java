package com.kekeinfo.web.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.report.service.ReportService;

@Component
public class ReportJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportJob.class);
	@Autowired CSiteService  siteService;
	@Autowired ReportService reportService;
	@Autowired DepartmentService departmentService;
	@Autowired ReportUtils reportUtils;
	
	public void autoReport(){
		
		try{
			//Entitites<Department> waterlist = departmentService.getListByAttributes(new String[]{"code"}, new String[]{Constants.DEPARTMENT_WATER}, null);
			//if (waterlist.getTotalCount()==1) {
				/**HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				
				List<Object[]> where= new ArrayList<Object[]>();
				where.add(new Object[] {  
						"status",2,"3" });
				where.add(new Object[]{"department.id", waterlist.getEntites().get(0).getId(), "0"}); //获取地下水部门下的项目集合
				Entitites<ConstructionSite> list  = siteService.getPageListByAttributesLike(null, null, null, null, orderby,where,null,false);*/
				List<String> p_attributes = new ArrayList<String>();
				p_attributes.add("CSITE_ID");
				List<Object[]> plist = siteService.getBySql(p_attributes, "CONSTRUCTIONSITE"," WHERE STATUS>-1");
				if(plist !=null && plist.size()>0){
					for(Object c:plist){
						LOGGER.info(">>>>>>>开始生成项目报表，项目ID："+c);
						reportUtils.createReport(Long.parseLong(c.toString()));
					}
				}
			//}
		}catch (Exception e){
			LOGGER.error(e.getMessage());
		}
		
		
	}
}

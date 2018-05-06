package com.kekeinfo.web.admin.controller.deform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.zxing.WriterException;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.pointinfo.model.DeformInfo;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.QCodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DMonitorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DMonitorController.class);
	
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired CSiteService cSiteService;
	@Autowired DeformmonitorService deformmonitorService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired QCodeUtils qCodeUtils;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/dmonitor/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
       
		
		setMenu(model,request);
		Deformmonitor dmonitor = new Deformmonitor();
		model.addAttribute("oWell", dmonitor);
		request.setAttribute("activeFun", "dmonitor");  //指定当前操作功能
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			try{
				@SuppressWarnings("unchecked")
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite=null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				}
				if(csite==null) csite =pnodeUtils.getByCid(Long.parseLong(csiteID));
				model.addAttribute("csite", csite);
				 
				 boolean hasRight=false;
				 if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
			        	if(request.isUserInRole("EDIT-PROJECT")){
			        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
			        	}
					}else{
						hasRight=true;
					}
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-dmonitors";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
			
		}else{
			return "csite-wlist";
		}
		return "water-dmonitors";
	}
	
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/dmonitor/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Deformmonitor> dt = new DataTable<Deformmonitor>();
			try {
				
				//指定项目ID进行查询
				
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<String> where = new ArrayList<>();
				where.add("cSite.id");
				where.add(csiteID);
				List<String> join =new ArrayList<String>();
				join.add("pointInfo");
				join.add("pointLink");
				Entitites<Deformmonitor> list  = deformmonitorService.getPageListByAttributesLike(null, null, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,join);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        dt.setAaData(list.getEntites());
		        ObjectMapper mapper = new ObjectMapper();
		       // mapper.getSerializationConfig().addMixInAnnotations(Deformmonitor.class, PointLinkFilter.class);
				String json = mapper.writeValueAsString(dt);
				return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging csite", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dmonitor/active.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String active(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		
		try {
			Deformmonitor dmonitor = deformmonitorService.getById(Long.parseLong(sUserId));
			if(dmonitor!=null){
				if(dmonitor.getPowerStatus().intValue()==1 ){ 
					dmonitor.setPowerStatus(0);
				}else{
					dmonitor.setPowerStatus(1);
				}
				deformmonitorService.saveOrUpdate(dmonitor);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				resp.setStatusMessage("操作成功");
			}
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/dmonitor/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveBanner(@ModelAttribute("csite") Deformmonitor dmonitor, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(user!=null){
				dmonitor.getAuditSection().setModifiedBy(user.getId().toString());
				if(dmonitor.getId()==null){
					dmonitor.getAuditSection().setDateCreated(new Date());
				}else{
					dmonitor.getAuditSection().setDateModified(new Date());
				}
				if(dmonitor.getPointInfo()!=null){
					dmonitor.getPointInfo().setPoint(dmonitor);
				}
				deformmonitorService.saveOrUpdate(dmonitor);
				//增加二维码
				//if(dmonitor.getqCode()==null ){
					this.saveQCode(request, dmonitor);
				//}
			}
			//
		} catch (ServiceException | WriterException | IOException e) {
			LOGGER.error("Error while save banner ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dmonitor/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					deformmonitorService.deleteById(id);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					if(e.getMessage().startsWith("Cannot delete or update a parent row")){
						resp.setStatus(9997);
					}else{
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dmonitor/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String importCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		String cols = request.getParameter("cols");
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		if(!StringUtils.isBlank(cols)){
			try {
				String pid=request.getParameter("pid");
				ConstructionSite csite = cSiteService.getById(Long.parseLong(pid));
				
				HttpSession session = request.getSession();
				session.setAttribute("import", "0");
				String [] col= null;
				col= cols.split(",");
				List<String> returns = new ArrayList<String>();
				Workbook book=transExcelView.getBook(uploadfile.getInputStream());
				DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
		        Sheet sheet = book.getSheetAt(0);
		    	int trLength = sheet.getLastRowNum();
		    	if(trLength>transExcelView.getImpMaxLine())trLength =transExcelView.getImpMaxLine();
		    	for(int i=0;i<=trLength;i++){
		    		Row row = sheet.getRow(i);
		    		try{
		    			//必填的数据
		    			if(StringUtils.isBlank(transExcelView.getValue(row,col[0], true)) 
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[1], true))
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[2], true))
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[3], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			Deformmonitor ddata = new Deformmonitor();
		    			DeformInfo einfo = new DeformInfo();
		    			einfo.setPoint(ddata);
		    			ddata.setPointInfo(einfo);
		    			//name
		    			ddata.setName(transExcelView.getValue(row,col[0], true));
		    			//经度
		    			ddata.setLongitude(transExcelView.getBigValue(row,col[1], true));
		    			ddata.setLatitude(transExcelView.getBigValue(row,col[2], true));
		    			//流量
		    			ddata.setDeformData(transExcelView.getBigValue(row,col[3], true));
		    			//状态
		    			ddata.setPowerStatus(transExcelView.getIntValue(row, col[4], 0));
		    			//m默认可见
		    			ddata.setVisible(true);
		    			//备注
	    				ddata.getPointInfo().setNote(transExcelView.getValue(row,col[5], true));
	    				ddata.setcSite(csite);
		    			deformmonitorService.save(ddata);
		    		}catch (Exception e){
		    			//e.printStackTrace();
		    			returns.add(String.valueOf(i+1));
		    			float num= (float)i/trLength;  
		    			session.setAttribute("import", df.format(num));
		    			continue;
		    		}
		    	}	
		    	
				if(returns==null || returns.size()==0){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}else{
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					//全部失败
					if(returns.size()==1){
						resp.addEntry("cols",returns.get(0));
					}else{
						StringBuffer sb = new StringBuffer();
						for(String s:returns) sb.append(s).append(",");
						sb.deleteCharAt(sb.lastIndexOf(","));
						resp.addEntry("cols",sb.toString());
					}
					
				}
				session.removeAttribute("import");
			} catch (Exception e) {
			LOGGER.error(e.getMessage());
			}
		}
		
		
		return resp.toJSONString();
	}
	
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dmonitor/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		
			try {
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("cSite.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(pid)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				Entitites<Deformmonitor> list  = deformmonitorService.getPageListByAttributes(attributes, fieldValues,null,null, orderby);
				
				XSSFWorkbook wb=null;
				if(list!=null && !list.getEntites().isEmpty()){
					String fileName ="环境监控"+request.getParameter("ename");
					
					//日期加1
					Date edate = (new SimpleDateFormat("yyyy-MM-dd")).parse(endtm);
					Calendar cal = Calendar.getInstance();
					cal.setTime(edate);
					cal.add(Calendar.DATE, 1);
					endtm =(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
					for(Deformmonitor p:list.getEntites()){
						attributes = new ArrayList<String>();
						attributes.add("DATA");
						attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
						attributes.add("UPDT_ID");
						attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
						attributes.add("THRESHOLD");
						attributes.add("STATUS");
						
						StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND EMONITOR_ID = ").append(p.getId());
						List<Object[]> bbtype= deformmonitorService.getBySql(attributes, "HEMONITORDATA", include_where.toString());
						String[] title={"监测值","观测日期","观测者","修改日期","阈值","是否超限"};
						String sheet = p.getName();
						wb =transExcelView.export(sheet, title, bbtype,wb);
						
					}
					fileName = URLEncoder.encode(fileName, "UTF-8");
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
					//写文件
					OutputStream os = response.getOutputStream();
					wb.write(os);
					os.close();
				} else {
					response.addHeader("Content-Disposition", "attachment;filename=nodata.txt");
					OutputStream os = response.getOutputStream();
					os.write("当前没有测点数据".getBytes());
					os.close();
				}
				
			} catch (Exception e) {
				LOGGER.debug("fail export");
				response.sendError(404, Constants.FILE_NOT_FOUND);
			}
		
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/admin/dmonitor/print.html", method = RequestMethod.GET)
	public String prints(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ids =request.getParameter("ids");
		if(StringUtils.isNotBlank(ids)){
			try{
				List<Long> dids = new ArrayList<>();
				String [] is = ids.split(",");
				for(String s:is){
					dids.add(Long.parseLong(s));
				}
				List<Deformmonitor> dewells = deformmonitorService.getByIds(dids);
				for (Deformmonitor dewewll : dewells) {
					if (dewewll.getqCode()==null) {
						this.saveQCode(request, dewewll);
					}
					dewewll.setqCode(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(dewewll.getqCode(), FileContentType.QCODE));
				}
				model.addAttribute("qcodes", dewells);
			}catch(Exception e){
				LOGGER.error(e.getMessage());
			}
			
		}
		return "wells-print";
	}
	
	private void saveQCode(HttpServletRequest request,Deformmonitor dewell) throws WriterException, IOException, ServiceException{
		String domainUrl = ImageFilePathUtils.buildHttp(request);
		String src =domainUrl+"/water/edata/list.html?pid="+dewell.getId()+"&ptype=ptype";
		InputStream istram = qCodeUtils.createQrCodeWithLogo(src,1000,"JPEG");
		dewell.setDigital(istram);
		String filename = RadomSixNumber.getImageName(dewell.getId().toString(), Constants.IMAGE_JPG);
		dewell.setqCode(filename);
		deformmonitorService.saveQcode(dewell);
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}

package com.kekeinfo.web.admin.controller.dewatering;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import java.util.Set;

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
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.pointinfo.model.DewateringInfo;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.services.controller.system.ModbusListener;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.QCodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;
import com.serotonin.modbus4j.sero.util.ResultConstant;


@Controller
public class DewateringController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DewateringController.class);
	
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired CSiteService cSiteService;
	@Autowired DewateringService dewateringService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired ModbusListener modbusListener;
	@Autowired PointService pointService;
	@Autowired QCodeUtils qCodeUtils;
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/dewell/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
       
		
		setMenu(model,request);
		Dewatering pwell = new Dewatering();
		model.addAttribute("oWell", pwell);
		request.setAttribute("activeFun", "dewell");  //指定当前操作功能
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			try{
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite =null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				}
				if(csite==null)csite=pnodeUtils.getByCid(Long.parseLong(csiteID));
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
					return "phone-dewells";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
		}else{
			return "csite-wlist";
		}
		return "water-dewells";
	}
	
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/dewell/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Dewatering> dt = new DataTable<Dewatering>();
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
				Entitites<Dewatering> list  = dewateringService.getPageListByAttributesLike(null, null,Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,join);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        dt.setAaData(list.getEntites());
		        ObjectMapper mapper = new ObjectMapper();
		        //mapper.getSerializationConfig().addMixInAnnotations(Dewatering.class, PointLinkFilter.class);
		        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
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
	@RequestMapping(value="/water/dewell/active.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String active(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		String astatus =request.getParameter("astatus");
		boolean doOpen=false;
		try {
			Dewatering pwell = dewateringService.getByIdWithPointLink(Long.parseLong(sUserId));
			if(pwell!=null){
				if(StringUtils.isNotBlank(astatus)){
					if(Integer.parseInt(astatus)==0){
						if(pwell.getPowerStatus().intValue()==1){
							pwell.setPowerStatus(0);
						}else{
							pwell.setPowerStatus(1);
						}
						pwell.setAutoStatus(0);
						doOpen=true;
					}else {
						if(Integer.parseInt(astatus)==1){
							if(pwell.getOpenDepp()==null || pwell.getCloseDepp()==null){
								resp.setStatus(-4);
								resp.setStatusMessage("操作失败，自动开启的阈值没有设定！！！");
								return resp.toJSONString();
							}
							pwell.setAutoStatus(1);
						}else{
							if(pwell.getConMin()==null || pwell.getSpaceMin()==null){
								resp.setStatus(-4);
								resp.setStatusMessage("操作失败，自动开启的时长没有设定！！！");
								return resp.toJSONString();
							}
							pwell.setAutoStatus(2);
							doOpen=true;
							pwell.setPowerStatus(0);
						}
					}
				}else{
					if(pwell.getPowerStatus().intValue()==1){
						pwell.setPowerStatus(0);
					}else{
						pwell.setPowerStatus(1);
					}
					pwell.setAutoStatus(0);
					doOpen=true;
				}
				
				
				//如果没有配置网关信息，则不做自动化远程控制
				@SuppressWarnings("rawtypes")
				BasepointLink link = pwell.getPointLink();
				if (link==null||link.getGateway()==null || link.getChannel3()==null || link.getNode3()==null) {
					dewateringService.saveOrUpdate(pwell);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resp.setStatusMessage("操作成功");
				} else {
					if(doOpen){
						//进行自动化控制，对测点进行启停控制 0 成功；-11 失败； -12 没有网关连接； -13 连接失败； -14  没有配置网关信息
						int flag = modbusListener.modifyStart(pwell,PointEnumType.DEWATERING);
						String tmp = "";
						if (flag==0) {
							LOGGER.info("测点远程启停操作成功，测点："+pwell.getName());
							tmp = "测点远程启停操作成功";
							//dewateringService.saveOrUpdate(pwell);
							resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
							resp.setStatusMessage("操作成功");
						}  else {
							tmp = "测点远程启停操作失败";
							switch (flag) {
							case ResultConstant.RESPONSE_STATUS_NOGATEWAY:
								tmp += ", 没有网关连接";
								break;
							case ResultConstant.RESPONSE_STATUS_NOCONNECTION:
								tmp += "，连接失败";
								break;
							case ResultConstant.RESPONSE_STATUS_NOCONFIG:
								tmp += "，没有配置网关信息";
								break;
							default:
								break;
							}
							LOGGER.info("测点远程启停操作失败，测点："+pwell.getName()+tmp);
							resp.setStatus(flag);
							resp.setStatusMessage(tmp);
						}
					}else{
						dewateringService.saveOrUpdate(pwell);
					}
					
				}
				this.setAuto(pwell);
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
	@RequestMapping(value="/water/dewell/getCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String getCols(Model model, @RequestParam(required=false, value="uploadfile") CommonsMultipartFile uploadfile, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			List<String> reList= transExcelView.getCol(uploadfile.getInputStream());
			if(reList==null){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}else if(reList.size()==0){
				resp.setStatus(-2);
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				StringBuffer sb = new StringBuffer();
				for(String s:reList) sb.append(s).append(",");
				sb.deleteCharAt(sb.lastIndexOf(","));
				resp.addEntry("cols",sb.toString());
				
			}

		} catch (Exception e) {
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dewell/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
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
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[3], true))
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[4], true))
		    					|| StringUtils.isBlank(transExcelView.getValue(row,col[5], true))){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			BigDecimal water =transExcelView.getBigValue(row,col[4], true);
		    			BigDecimal waterDown =transExcelView.getBigValue(row,col[5], true);
		    			//下限大于上限不能导入
		    			if(waterDown.compareTo(water) ==1){
		    				returns.add(String.valueOf(i+1));
			    			float num= (float)i/trLength;  
			    			session.setAttribute("import", df.format(num));
		    				continue;
		    			}
		    			Dewatering pwell = new Dewatering();
		    			DewateringInfo deinfo = new DewateringInfo();
		    			deinfo.setPoint(pwell);
		    			//pwell.setPointInfo(deinfo);
		    			//name
		    			pwell.setName(transExcelView.getValue(row,col[0], true));
		    			//经度
		    			pwell.setLongitude(transExcelView.getBigValue(row,col[1], true));
		    			pwell.setLatitude(transExcelView.getBigValue(row,col[2], true));
		    			//阈值
		    			pwell.setFlow(transExcelView.getBigValue(row,col[3], true));
		    			pwell.setWater(water);
		    			pwell.setWaterDwon(waterDown);
		    			//状态
		    			pwell.setPowerStatus(transExcelView.getIntValue(row, col[6], 0));
		    			//备注
	    				deinfo.setNote(transExcelView.getValue(row,col[7], true));
		    			pwell.setcSite(csite);
		    			//m默认可见
		    			pwell.setVisible(true);
		    			//降水目的层
		    			try{
		    				deinfo.setPrecipitation(transExcelView.precipitation(row,col[8], true));
		    				
		    			}catch (Exception e){
		    				LOGGER.error(e.getMessage());
		    			}
		    			
		    			deinfo=(DewateringInfo)transExcelView.wellBase(deinfo,9, col, row);
		    			pwell.setPointInfo(deinfo);
		    			dewateringService.save(pwell);
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
	@RequestMapping(value="/water/dewell/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
		/**AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);*/
			
	try {
			String pid=request.getParameter("eid");
			List<String> attributes = new ArrayList<String>();
			attributes.add("cSite.id");
			List<String> fieldValues = new ArrayList<String>();
			fieldValues.add(String.valueOf(Long.parseLong(pid)));
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<Dewatering> list  = dewateringService.getPageListByAttributes(attributes, fieldValues,null,null, orderby);
			
			XSSFWorkbook wb=null;
			if(list!=null && !list.getEntites().isEmpty()){
				String fileName ="疏干井"+request.getParameter("ename");
				//日期加1
				Date edate = (new SimpleDateFormat("yyyy-MM-dd")).parse(endtm);
				Calendar cal = Calendar.getInstance();
				cal.setTime(edate);
				cal.add(Calendar.DATE, 1);
				endtm =(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				for(Dewatering p:list.getEntites()){
					attributes = new ArrayList<String>();
					attributes.add("FLOW");
					attributes.add("WATER");
					attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
					attributes.add("UPDT_ID");
					attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
					attributes.add("FLOWTHRESHOLD");
					attributes.add("WATERTHRESHOLD");
					attributes.add("STATUS");
					
					StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND DEWELL_ID = ").append(p.getId());
					List<Object[]> bbtype= dewateringService.getBySql(attributes, "HDEWELLDATA", include_where.toString());
					String[] title={"流量","水位","观测日期","观测者","修改日期","流量阈值","水位阈值","是否超限"};
					String sheet = p.getName();
					wb =transExcelView.export(sheet, title, bbtype,wb);
				}
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
				//写文件
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.close();
			}  else {
				response.addHeader("Content-Disposition", "attachment;filename=nodata.txt");
				OutputStream os = response.getOutputStream();
				os.write("当前没有测点数据".getBytes());
				os.close();
			}
		
				//resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				//return wb.getBytes();
		} catch (Exception e) {
			LOGGER.debug("fail export");
			response.sendError(404, Constants.FILE_NOT_FOUND);
		}
		//return resp.toJSONString();
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dewell/getProgress.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	public @ResponseBody String getProgress(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		HttpSession session = request.getSession();
		try {
			String progress = session.getAttribute("import").toString();
			if(progress.trim().equalsIgnoreCase("1")){
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				resp.addEntry("progress", progress);
			}
			
		} catch (Exception e) {
			
		}
		
		return resp.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/dewell/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveBanner(@ModelAttribute("csite") Dewatering pwell, Model model,@RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			String delids= request.getParameter("delids");
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(pwell.getAutoStatus()==1){
				if(pwell.getCloseDepp()==null || pwell.getOpenDepp()==null){
					resp.setStatus(-4);
					resp.setStatusMessage("没有设置自动开启的阈值，保存失败");
					return resp.toJSONString();
				}
			}else if(pwell.getAutoStatus()==2){
				if(pwell.getConMin()==null || pwell.getSpaceMin()==null){
					resp.setStatus(-4);
					resp.setStatusMessage("没有设置自动开启的持续时间，保存失败");
					return resp.toJSONString();
				//打开
				}
			}
			if(user!=null){
				pwell.getAuditSection().setModifiedBy(user.getId().toString());
				if(pwell.getId()==null){
					pwell.getAuditSection().setDateCreated(new Date());
				}else{
					pwell.getAuditSection().setDateModified(new Date());
				}
				DewateringInfo deinfo=pwell.getPointInfo();
				//附件处理
				if(uploadfiles!=null && !uploadfiles.isEmpty()){
					List<Wellimages> imgs= new ArrayList<>();
					for(CommonsMultipartFile uploadfile:uploadfiles){
						if (uploadfile!=null && !uploadfile.isEmpty()) {
							Wellimages img = new Wellimages();
							img.setName(RadomSixNumber.getImageName(uploadfile.getOriginalFilename())); 
							img.setDigital(uploadfile.getInputStream());
							//压缩
							img.setJpeg(RadomSixNumber.getImageName(img.getName()));
							InputStream inputStream = uploadfile.getInputStream();
							img.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
							imgs.add(img);
						}
					}
					if(deinfo==null) deinfo =new DewateringInfo();
					deinfo.setImages(imgs);
				}
				if(deinfo!=null){
					deinfo.setPoint(pwell);
				}
				if (pwell.getId()!=null&&pwell.getPowerStatus().intValue()==2) { //更新断电状态
					HashMap<Long, Set<AppUser>> umaps =(HashMap<Long, Set<AppUser>>)webCacheUtils.getFromCache(Constants.USER_WARNING);
					List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
					UnderWater un=null;
					for(UnderWater u:cs){
						if(u.getId().equals(pwell.getcSite().getId())){
							un=u;
							break;
						}
					}
					Set<AppUser> auses=null;
					String pname="";
					if(un!=null){
						auses=umaps.get(un.getPid());
						pname=un.getName();
					}
					pointService.updatePowerstatus(pwell, PointEnumType.DEWATERING,auses,pname);
				}
				dewateringService.saveOrUpdate(pwell,delids);
				this.setAuto(pwell);
				//增加二维码
				//if(pwell.getqCode()==null ){
					this.saveQCode(request, pwell);
				//}
			}
			//
		} catch (ServiceException | IOException | WriterException e) {
			LOGGER.error("Error while save banner ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dewell/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					dewateringService.deleteById(id);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					resplist.add(resp);
				} catch (Exception e) {
					if(e.getMessage().startsWith("Cannot delete or update a parent row")){
						resp.setStatus(9997);
					}else{
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					e.printStackTrace();
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/admin/dewell/print.html", method = RequestMethod.GET)
	public String prints(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ids =request.getParameter("ids");
		if(StringUtils.isNotBlank(ids)){
			try{
				List<Long> dids = new ArrayList<>();
				String [] is = ids.split(",");
				for(String s:is){
					dids.add(Long.parseLong(s));
				}
				List<Dewatering> dewells = dewateringService.getByIds(dids);
				
				for (Dewatering dewewll : dewells) {
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
	
	private void saveQCode(HttpServletRequest request,Dewatering dewell) throws WriterException, IOException, ServiceException{
		String domainUrl = ImageFilePathUtils.buildHttp(request);
		String src =domainUrl+"/water/dedata/list.html?pid="+dewell.getId()+"&ptype=ptype";
		InputStream istram = qCodeUtils.createQrCodeWithLogo(src,1000,"JPEG");
		dewell.setDigital(istram);
		String filename = RadomSixNumber.getImageName(dewell.getId().toString(), Constants.IMAGE_JPG);
		dewell.setqCode(filename);
		dewateringService.saveQcode(dewell);
	}
	
	private List<Dewatering> resetmemory(Dewatering pwell,List<Dewatering> bs,int index) throws ServiceException{
		if(pwell.getAutoStatus().intValue()==index){
			if(bs==null) bs=new ArrayList<>();
			boolean isfound =false;
			for(Dewatering p:bs){
				if(p.getId().equals(pwell.getId())){
					isfound=true;
					break;
				}
			}
			if(!isfound){
				pwell =dewateringService.getByIdWithPointLink(pwell.getId());
				bs.add(pwell);
			}
		}else{
			if(bs!=null){
				for(Dewatering p:bs){
					if(p.getId().equals(pwell.getId())){
						bs.remove(p);
						break;
					}
				}
			}
		}
		
		return bs;
	}
	@SuppressWarnings("unchecked")
	private void setAuto(Dewatering pwell) throws ServiceException{
		List<Dewatering> bs=(List<Dewatering>) webCacheUtils.getFromCache(Constants.AUTODEEPDEWELL);
		bs=this.resetmemory(pwell, bs, 1);
		webCacheUtils.putInCache(Constants.AUTODEEPDEWELL, bs);
		//自动设置内存
		List<Dewatering> pwells=(List<Dewatering>) webCacheUtils.getFromCache(Constants.AUTODEWELL);
		pwells=this.resetmemory(pwell, pwells, 2);
		webCacheUtils.putInCache(Constants.AUTODEWELL, pwells);
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}

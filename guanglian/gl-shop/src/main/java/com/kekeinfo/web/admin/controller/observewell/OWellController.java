package com.kekeinfo.web.admin.controller.observewell;

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
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.pointinfo.model.ObserveInfo;
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
public class OWellController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OWellController.class);
	
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired CSiteService cSiteService;
	@Autowired ObservewellService observewellService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired QCodeUtils qCodeUtils;
	@Autowired ModbusListener modbusListener;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/owell/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
       
		
		setMenu(model,request);
		Observewell oWell = new Observewell();
		model.addAttribute("oWell", oWell);
		request.setAttribute("activeFun", "owell");  //指定当前操作功能
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
				if(csite==null)csite =pnodeUtils.getByCid(Long.parseLong(csiteID));
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
					return "phone-owells";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
			
		}else{
			return "csite-wlist";
		}
		
		return "water-owells";
	}
	
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/owell/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Observewell> dt = new DataTable<Observewell>();
			try {
				
				
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<String> where = new ArrayList<>();
				where.add("cSite.id");
				where.add(csiteID);
				List<String> join =new ArrayList<String>();
				join.add("pointInfo");
				join.add("pointLink");
				Entitites<Observewell> list  = observewellService.getPageListByAttributesLike(null, null, Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,join);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        dt.setAaData(list.getEntites());
		        ObjectMapper mapper = new ObjectMapper();
		       // mapper.getSerializationConfig().addMixInAnnotations(Observewell.class, PointLinkFilter.class);
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
	@RequestMapping(value="/water/owell/active.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String active(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		String astatus =request.getParameter("astatus");
		try {
			Observewell obwell = observewellService.getByIdWithPointLink(Long.parseLong(sUserId));
			if(obwell!=null){
				boolean doOpen=false;
				if(StringUtils.isNotBlank(astatus)){
					if(Integer.parseInt(astatus)==0){
						if(obwell.getPowerStatus().intValue()==1){
							obwell.setPowerStatus(0);
						}else{
							obwell.setPowerStatus(1);
						}
						obwell.setAutoStatus(0);
						doOpen=true;
					}else {
						if(Integer.parseInt(astatus)==1){
							if(obwell.getOpenDepp()==null || obwell.getCloseDepp()==null){
								resp.setStatus(-4);
								resp.setStatusMessage("操作失败，自动开启的阈值没有设定！！！");
								return resp.toJSONString();
							}
							obwell.setAutoStatus(1);
						}else{
							if(obwell.getConMin()==null || obwell.getSpaceMin()==null){
								resp.setStatus(-4);
								resp.setStatusMessage("操作失败，自动开启的时长没有设定！！！");
								return resp.toJSONString();
							}
							obwell.setAutoStatus(2);
							doOpen=true;
							obwell.setPowerStatus(0);
						}
					}
				}else{
					if(obwell.getPowerStatus().intValue()==1) {
						obwell.setPowerStatus(0);
					}else{
						obwell.setPowerStatus(1);
					}
					obwell.setAutoStatus(0);
					doOpen=true;
				}
				//如果没有配置网关信息，则不做自动化远程控制
				@SuppressWarnings("rawtypes")
				BasepointLink link = obwell.getPointLink();
				if (link==null||link.getGateway()==null || link.getChannel3()==null || link.getNode3()==null) {
					observewellService.saveOrUpdate(obwell);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resp.setStatusMessage("操作成功");
				} else {
					//进行自动化控制，对测点进行启停控制 0 成功；-11 失败； -12 没有网关连接； -13 连接失败； -14  没有配置网关信息
					if(doOpen){
						int flag = modbusListener.modifyStart(obwell,PointEnumType.OBSERVE);
						String tmp = "";
						if (flag==0) {
							LOGGER.info("测点远程启停操作成功，测点："+obwell.getName());
							tmp = "测点远程启停操作成功";
							//observewellService.saveOrUpdate(obwell);
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
							LOGGER.info("测点远程启停操作失败，测点："+obwell.getName()+tmp);
							resp.setStatus(flag);
							resp.setStatusMessage(tmp);
						}
					}else{
						observewellService.saveOrUpdate(obwell);
					}
				}
				this.setAuto(obwell);
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
	@RequestMapping(value="/water/owell/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveBanner(@ModelAttribute("csite") Observewell owell, Model model, @RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles,HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			String delids= request.getParameter("delids");
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(owell.getAutoStatus()==1){
				if(owell.getCloseDepp()==null || owell.getOpenDepp()==null){
					resp.setStatus(-4);
					resp.setStatusMessage("没有设置自动开启的阈值，保存失败");
					return resp.toJSONString();
				}
			}else if(owell.getAutoStatus()==2){
				if(owell.getConMin()==null || owell.getSpaceMin()==null){
					resp.setStatus(-4);
					resp.setStatusMessage("没有设置自动开启的持续时间，保存失败");
					return resp.toJSONString();
					//打开
				}
			}
			if(user!=null){
				owell.getAuditSection().setModifiedBy(user.getId().toString());
				if(owell.getId()==null){
					owell.getAuditSection().setDateCreated(new Date());
				}else{
					owell.getAuditSection().setDateModified(new Date());
				}
				ObserveInfo oinfo =owell.getPointInfo();
				//之前的用户如果有修改需要去掉权限
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
					if(oinfo==null) oinfo = new ObserveInfo();
					oinfo.setImages(imgs);
				}
				if(oinfo!=null){
					oinfo.setPoint(owell);
				}
				observewellService.saveOrUpdate(owell,delids);
				this.setAuto(owell);
				//增加二维码
				//if(owell.getqCode()==null ){
					this.saveQCode(request, owell);
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
	@RequestMapping(value="/water/owell/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					observewellService.deleteById(id);
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
	@RequestMapping(value="/water/owell/importCols.shtml", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
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
		    			Observewell owell = new Observewell();
		    			ObserveInfo oinfo = new ObserveInfo();
		    			oinfo.setPoint(owell);
		    			//owell.setPointInfo(oinfo);
		    			//name
		    			owell.setName(transExcelView.getValue(row,col[0], true));
		    			//经度
		    			owell.setLongitude(transExcelView.getBigValue(row,col[1], true));
		    			owell.setLatitude(transExcelView.getBigValue(row,col[2], true));
		    			//水温
		    			owell.setWaterTemperature(transExcelView.getBigValue(row,col[3], true));
		    			//水位
		    			owell.setWaterMeasurement(water);
		    			owell.setWaterDwon(waterDown);
		    			//m默认可见
		    			owell.setVisible(true);
		    			//其他不是必填出错了采用默认值导入
		    			try{
		    				//状态
		    				owell.setPowerStatus(transExcelView.getIntValue(row, col[13], 0));
		    				
		    				try{
		    					//降水目的层
		    					oinfo.setPrecipitation(transExcelView.precipitation(row,col[15], true));
			    				
		    				}catch(Exception e){
		    					LOGGER.error(e.getMessage());
		    				}
		    				oinfo.setNote(transExcelView.getValue(row,col[14], true));
		    				oinfo =(ObserveInfo) transExcelView.wellBase(oinfo,16, col, row);
		    				
		    			}catch (Exception e){
		    				LOGGER.error(e.getMessage());
		    			}
		    			owell.setPointInfo(oinfo);
		    			owell.setcSite(csite);
		    			observewellService.save(owell);
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
			
			}
		}
		
		
		return resp.toJSONString();
	}
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/owell/exportCols.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=UTF-8")
	public @ResponseBody void  exportCols(Model model, @RequestParam(value="ebegindt", required=false) String begindt, @RequestParam(value="eendtm", required=false) String endtm, HttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
			try {
				
				String pid=request.getParameter("eid");
				List<String> attributes = new ArrayList<String>();
				attributes.add("cSite.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(pid)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				Entitites<Observewell> list  = observewellService.getPageListByAttributes(attributes, fieldValues,null,null, orderby);
				
				XSSFWorkbook wb=null;
				if(list!=null && !list.getEntites().isEmpty()){
					String fileName ="观测井"+request.getParameter("ename");
					//日期加1
					Date edate = (new SimpleDateFormat("yyyy-MM-dd")).parse(endtm);
					Calendar cal = Calendar.getInstance();
					cal.setTime(edate);
					cal.add(Calendar.DATE, 1);
					endtm =(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
					for(Observewell p:list.getEntites()){
						attributes = new ArrayList<String>();
						attributes.add("WATER");
						attributes.add("TEMPERATURE");
						attributes.add("date_format(DATE_CREATED, '%Y-%m-%d %H:%i:%s')");
						attributes.add("UPDT_ID");
						attributes.add("date_format(DATE_MODIFIED, '%Y-%m-%d %H:%i:%s')");
						attributes.add("WATERTHRESHOLD");
						attributes.add("WATERDOWN");
						attributes.add("TEMPERATURETHRESHOLD");
						attributes.add("STATUS");
						
						StringBuffer include_where = new StringBuffer().append(" where ( DATE_CREATED BETWEEN '").append(begindt).append("' AND '").append(endtm).append("' ) AND OWELL_ID = ").append(p.getId());
						List<Object[]> bbtype= observewellService.getBySql(attributes, "HOWELLDATA", include_where.toString());
						String[] title={"水位","水温","观测日期","观测者","修改日期","水位阈值上限","水位阈值下限","水温阈值","是否超限"};
						String sheet =p.getName();
						wb =transExcelView.export(sheet, title, bbtype,wb);
					}
				
					fileName = URLEncoder.encode(fileName, "UTF-8");
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".xlsx");
					//写文件
					OutputStream os = response.getOutputStream();
					wb.write(os);
					os.close();
					//resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					//return wb.getBytes();
				} else {
					response.addHeader("Content-Disposition", "attachment;filename=nodata.txt");
					OutputStream os = response.getOutputStream();
					os.write("当前没有测点数据".getBytes());
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.debug("fail export");
				response.sendError(404, Constants.FILE_NOT_FOUND);
			}
		//return resp.toJSONString();
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/admin/owell/print.html", method = RequestMethod.GET)
	public String prints(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ids =request.getParameter("ids");
		if(StringUtils.isNotBlank(ids)){
			try{
				List<Long> dids = new ArrayList<>();
				String [] is = ids.split(",");
				for(String s:is){
					dids.add(Long.parseLong(s));
				}
				List<Observewell> pwells = observewellService.getByIds(dids);
				for (Observewell pewll : pwells) {
					if (pewll.getqCode()==null) {
						this.saveQCode(request, pewll);
					}
					pewll.setqCode(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(pewll.getqCode(), FileContentType.QCODE));
				}
				model.addAttribute("qcodes", pwells);
			}catch(Exception e){
				LOGGER.error(e.getMessage());
			}
			
		}
		return "wells-print";
	}
	
	private void saveQCode(HttpServletRequest request,Observewell owell) throws WriterException, IOException, ServiceException{
		String domainUrl = ImageFilePathUtils.buildHttp(request);
		String src =domainUrl+"/water/odata/list.html?pid="+owell.getId()+"&ptype=ptype";
		InputStream istram = qCodeUtils.createQrCodeWithLogo(src,1000,"JPEG");
		owell.setDigital(istram);
		String filename = RadomSixNumber.getImageName(owell.getId().toString(), Constants.IMAGE_JPG);
		owell.setqCode(filename);
		observewellService.saveQcode(owell);;
	}
	/**
	 * 将观测井转成对应的抽水井
	 * @param wid
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/owell/doswitch.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String doswitch(@RequestParam(required=true) String wid, HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		int result = observewellService.switchWell(Long.valueOf(wid));
		if (result==-1) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage("该测点信息不存在");
		} else if (result == -2) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage("测点类型转换有问题，请联系管理员！");
		}
		String returnString = resp.toJSONString();
		return returnString;
		
	}
	
	private List<Observewell> resetmemory(Observewell pwell,List<Observewell> bs,int index) throws ServiceException{
		if(pwell.getAutoStatus().intValue()==index){
			if(bs==null) bs=new ArrayList<>();
			boolean isfound =false;
			for(Observewell p:bs){
				if(p.getId().equals(pwell.getId())){
					isfound=true;
					break;
				}
			}
			if(!isfound){
				pwell=observewellService.getByIdWithPointLink(pwell.getId());
				bs.add(pwell);
			}
		}else{
			if(bs!=null){
				for(Observewell p:bs){
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
	private void setAuto(Observewell pwell) throws ServiceException{
		List<Observewell> bs=(List<Observewell>) webCacheUtils.getFromCache(Constants.AUTODEEPOWELL);
		bs=this.resetmemory(pwell, bs, 1);
		webCacheUtils.putInCache(Constants.AUTODEEPOWELL, bs);
		//自动设置内存
		List<Observewell> pwells=(List<Observewell>) webCacheUtils.getFromCache(Constants.AUTOOWELL);
		pwells=this.resetmemory(pwell, pwells, 2);
		webCacheUtils.putInCache(Constants.AUTOOWELL, pwells);
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}

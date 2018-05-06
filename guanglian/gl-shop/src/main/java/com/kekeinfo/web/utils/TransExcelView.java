package com.kekeinfo.web.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.web.constants.Constants;




@Component
public class TransExcelView extends AbstractExcelView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransExcelView.class);
	private int impMaxLine = 1000;
	@Autowired BaseDataService baseDataService;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Workbook getBook(InputStream inputStream){
		Workbook book = null;
		
        try {
        	book = WorkbookFactory.create(inputStream);
        	//col= cols.split(",");
        	return book;
        } catch (Exception ex) {
        	ex.printStackTrace();
            try {
            	book = new XSSFWorkbook(inputStream);
				//book = new HSSFWorkbook(inputStream);
            	return book;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				 try {
					 	book = new HSSFWorkbook(inputStream);
						return book;
					} catch (Exception e2) {
						e.printStackTrace();
						LOGGER.error(e.getMessage());
						
						return null;
					}
			} 
        }
	}
	public  Workbook create(InputStream in) throws     
    IOException,InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");

    }

	/**
	 * null 为读取数据是不
	 * List size为0为数据为空
	 * @param inputStream
	 * @return
	 */
	public List<String> getCol(InputStream inputStream) {
		Workbook book = null;
		List<String> cols = new ArrayList<String>();
        try {
        	book = WorkbookFactory.create(inputStream);
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
            try {
            	book = new XSSFWorkbook(inputStream);
				//book = new HSSFWorkbook(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				
				return null;
			}
        }
        try{
        	Sheet sheet = book.getSheetAt(0);
        	int trLength = sheet.getLastRowNum();
        	
        	for(int i=0;i<trLength;i++){
        		Row row = sheet.getRow(i);
        		int tdLength = row.getLastCellNum();
        		if(tdLength>0){
        			for(int j=0;j<tdLength;j++){
        				try{
        					cols.add(row.getCell(j).toString());
        				}catch (Exception e){
        					
        				}
        			}
        			//加空行
        			cols.add("");
        			return cols;
        		}
        	}
        }catch (Exception e){
        	e.printStackTrace();
        	return null;
        }
        
        return cols;
		
	}
	
	
	/**
	 * 获取单元格数字，转换成字符串
	 * @param hssfCell
	 * @param istrim 是否需要将字符串的前后空字符trim
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String getValue(Row row,String num, boolean istrim) {
		if(Integer.parseInt(num)!=-1){
			Cell hssfCell =row.getCell(Integer.parseInt(num));
	    	if (hssfCell == null) return null;
	        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
	            // 返回布尔类型的值
	            return String.valueOf(hssfCell.getBooleanCellValue());
	        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
	            // 返回数值类型的值，取消科学计数法的表达方式，
	        	//如果是数值，先判断是否是日期格式，如果是日期格式，则先进行转换，否则按照字符串方式处理
//	        	String.format("%.0f", row.getCell(0).getNumericCellValue())
	        	if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
	        		return DateUtil.formatDate(hssfCell.getDateCellValue());
	        	} else return String.format("%.0f", hssfCell.getNumericCellValue());
	        } else {
	            // 返回字符串类型的值
	        	if (istrim) {
	        		return String.valueOf(hssfCell.getStringCellValue().trim());
				} else {
					return String.valueOf(hssfCell.getStringCellValue());
				}
	        }
		}
		return null;
		
    }
	
	
	public BigDecimal getBigValue(Row row,String num, boolean istrim) {
		if(Integer.parseInt(num)!=-1){
			Cell hssfCell =row.getCell(Integer.parseInt(num));
			if (hssfCell == null) return null;
	    	try{
	    		hssfCell.setCellType(Cell.CELL_TYPE_STRING);
	    		return new BigDecimal(hssfCell.getStringCellValue());
	    	}catch (Exception e){
	    		
	    		e.printStackTrace();
	    	}
		}
    	
        return null;
    }
	
	public int getIntValue(Row row,String num,int defaut) {
		if(Integer.parseInt(num)!=-1){
			Cell hssfCell =row.getCell(Integer.parseInt(num));
			if (hssfCell == null) return defaut;
	    	try{
	    		hssfCell.setCellType(Cell.CELL_TYPE_STRING);
	    		return Integer.parseInt(hssfCell.getStringCellValue());
	    	}catch (Exception e){
	    		
	    		e.printStackTrace();
	    	}
		}
    	
        return defaut;
    }
	
	public Date getDateValue(Row row,String num, boolean istrim){
		if(Integer.parseInt(num)!=-1){
			Cell hssfCell =row.getCell(Integer.parseInt(num));
			if (hssfCell == null) return null;
			switch (hssfCell.getCellType()){
	        case HSSFCell.CELL_TYPE_NUMERIC:
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
	                Date d = hssfCell.getDateCellValue();
	                String dateString = sdf.format(d);
	                try {
						return sdf.parse(dateString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            return null;
	            
	        case HSSFCell.CELL_TYPE_STRING:
	        	String d = hssfCell.getStringCellValue();
	            if (hssfCell.getStringCellValue().indexOf("/") > 0) {
	                d = hssfCell.getStringCellValue().replace("/", "-");
	            }
	            String temp = d + " 00:00:00";
	            try {
	                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                return df.parse(temp);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return null;
	            }
	       
	        }
		}
		return null;
	}
	
	public XSSFWorkbook  export(String sheetName,String []title,List<Object[]> datas,XSSFWorkbook wb){
		if(wb==null) wb = new XSSFWorkbook();
		
		XSSFSheet sheet = wb.createSheet(sheetName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        XSSFRow row = sheet.createRow(0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        XSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式 
        XSSFCell cell = null; 
      //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);  
            cell.setCellValue(title[i]);  
            cell.setCellStyle(style);  
        }
        int i=1;
      //创建内容
        for(Object[] ob:datas){
        	row = sheet.createRow(i);
        	int j=0;
        	for(Object o:ob){
        		//是否超限,最后一列
        		if(j==title.length-1){
        			try{
        				int status = Integer.parseInt(o.toString());
        				if(status>0){
        					row.createCell(j).setCellValue(true);
        				}else {
        					row.createCell(j).setCellValue(false);
        				}
        			}catch (Exception e){
        				if(o!=null){
        					row.createCell(j).setCellValue(o.toString());
        				}else{
        					row.createCell(j).setCellValue("");
        				}
        			}
        		}else{
        			if(o!=null){
        				row.createCell(j).setCellValue(o.toString());
        			}else{
        				row.createCell(j).setCellValue("");
        			}
        			
        		}
        		j++;
        	}
        	i++;
        }
        return wb;
	}
	
	/**
	 * 降水目的层
	 * @param pre
	 * @return
	 */
	public String precipitation(Row row,String num , boolean istrim){
		if(Integer.parseInt(num)!=-1){
			Cell hssfCell =row.getCell(Integer.parseInt(num));
			if (hssfCell == null) return null;
			String pre =hssfCell.getStringCellValue();
			if(StringUtils.isNotBlank(pre)){
				String[] pres = pre.split(",");
				Entitites<BasedataType> precipitations = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{Constants.BD_PRECIPITATION}, null);
				if(precipitations!=null){
					StringBuffer res=new StringBuffer();
					for(String p:pres){
						for(BasedataType pb:precipitations.getEntites()){
							if(pb.getName().equalsIgnoreCase(p.trim())){
								res.append(pb.getValue()).append(",");
								break;
							}
						}
					}
					res.deleteCharAt(res.lastIndexOf(","));
					return res.toString();
				}
			}
		}
		return null;
	}
	
	public <E> BasepointInfo<E> wellBase(BasepointInfo<E> wellBase,int start,String [] col,Row row){
		try{
			//WellBase wellBase = new WellBase();
			wellBase.setAperture(this.getBigValue(row,col[start], true));
			wellBase.setTubeWell(this.getBigValue(row,col[start+1], true));
			wellBase.setDeepWell(this.getBigValue(row,col[start+2], true));
			wellBase.setfTubleLgn(this.getBigValue(row,col[start+3], true));
			wellBase.setBackfillVol(this.getBigValue(row,col[start+4], true));
			wellBase.setClayBackfill(this.getBigValue(row,col[start+5], true));
			
			String bf = this.getValue(row,col[start+6], true);
			wellBase.setBackFill(this.getIntBoolean(bf));
			try{
				wellBase.setWashWC(this.getValue(row,col[start+7], true));
			}catch(Exception e){
				LOGGER.error(e.getMessage());
			}
			wellBase.setsWellFlow(this.getBigValue(row,col[start+8], true));
			wellBase.setMoveingWater(this.getBigValue(row,col[start+9], true));
			wellBase.setInitialWater(this.getBigValue(row,col[start+10], true));
			wellBase.setPoundSite(this.getValue(row,col[start+11], true));
			
			String ef = this.getValue(row,col[start+12], true);
			wellBase.setException(this.getIntBoolean(ef));
			
			wellBase.setWellTime(this.getDateValue(row,col[start+13], true));
			wellBase.setcPerson(this.getValue(row,col[start+14], true));
			wellBase.setAcceptance(this.getValue(row,col[start+15], true));
			wellBase.setClosure(this.getDateValue(row,col[start+16], true));
			wellBase.setCmeasures(this.getValue(row,col[start+17], true));
			wellBase.setsAcceptance(this.getValue(row,col[start+18], true));
			//return wellBase;
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		return wellBase;
		
	}
	
	public int getIntBoolean(String str){
		int rint=0;
		if(StringUtils.isNotBlank(str)){
			if(str.equalsIgnoreCase("是") || str.equalsIgnoreCase("true")){
				rint=1;
			}
		}
		return rint;
	}
	public int getImpMaxLine() {
		return impMaxLine;
	}

	public void setImpMaxLine(int impMaxLine) {
		this.impMaxLine = impMaxLine;
	}
	
}

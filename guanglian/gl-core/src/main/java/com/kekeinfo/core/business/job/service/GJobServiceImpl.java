package com.kekeinfo.core.business.job.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.AMessageService;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.sign.model.Sign;
import com.kekeinfo.core.business.sign.service.SignService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.utils.MessageTypeEnum;
import com.kekeinfo.core.business.job.dao.GJobDao;

@Service("gjobService")
public class GJobServiceImpl extends KekeinfoEntityServiceImpl<Long, GJob> implements GJobService {
	private GJobDao gjobDao;

	@Autowired UserService userService;
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;
	@Autowired AMessageService aMessageService;
	@Autowired MessageUService messageUService;
	@Autowired SignService signService;
	@Autowired
	public GJobServiceImpl(GJobDao gjobDao) {
		super(gjobDao);
		this.gjobDao = gjobDao;
	}

	@Override
	public List<GJob> getByGidsAndDate(List<Long> ids, Date date,Date edate) throws ServiceException {
		// TODO Auto-generated method stub
		return this.gjobDao.getByGidsAndDate(ids, date,edate);
	}

	@Transactional
	@Override
	public String saveUpdate(GJob gjob) throws ServiceException, ParseException {
		// TODO Auto-generated method stub
		List<Long> id =new ArrayList<>();
		id.add(gjob.getGuard().getId());
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal = Calendar.getInstance();
        cal.setTime(gjob.getEndDate());
        cal.add(Calendar.DATE, +1);
        Date enddata =sdf.parse(sdf.format(cal.getTime()));
		List<GJob> gbs=this.getByGidsAndDate(id, gjob.getStartDate(), enddata);
		if(gbs!=null && gbs.size()>0){
			Set<User> users =gjob.getUsers();
			for(User user:users){
				for(GJob gb:gbs){
					Set<User> gusers =gb.getUsers();
					for(User u:gusers){
						if(u.getId().equals(user.getId())){
							//判断下时间范围
							boolean hasJob =this.hasJob(gjob.getArriveDate(), gjob.getLeaveDate(), gb.getArriveDate(), gb.getLeaveDate());
							if(hasJob){
								return "-1";
							}
						}
					}
				}
				
			}
		}
		//先删除之前的工作安排
		if(gjob.getId()!=null){
			List<Sign> signs = signService.getByJId(gjob.getId());
			if(signs!=null && signs.size()>0){
				Date date =new Date();
				for(Sign s:signs){
					if(s.getShouleBe().getTime()>date.getTime()){
						signService.delete(s);
					}
				}
			}
			
		}
		super.saveOrUpdate(gjob);
		//发送推送消息
		StringBuffer noapp =new StringBuffer();
		Set<User> users = gjob.getUsers();
		AMessage am =new AMessage();
		am.setDateCreated(new Date());
		String message=gjob.getGuard().getName()+"项目 有新的工作安排，请查看！";
		am.setTitle(message);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String detail ="工作时间安排:开始日期-"+dateFormat.format(gjob.getStartDate())+";结束日期- "
		+dateFormat.format(gjob.getEndDate())+";要点时间-"+gjob.getArriveDate()+";销点时间-"+gjob.getLeaveDate();
		am.setMtype(MessageTypeEnum.GJobA);
		am.setMessage(detail);
		aMessageService.save(am);
		//创建签到表
		//先确定起始时间
		List<Date> dateList =new ArrayList<>();
		Date today=gjob.getStartDate();
		while (!today.after(gjob.getEndDate())){
			dateList.add(today);
			cal.setTime(today);  
			cal.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
			today =cal.getTime();
		}
		
		//判断是否跨天，如果结束日期比开始时间早要跨一天
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date dt1 = df.parse(gjob.getArriveDate());//将字符串转换为date类型  
        Date dt2 = df.parse(gjob.getLeaveDate());
        boolean iscorss=false;
        if(dt1.getTime()>dt2.getTime()){
        	iscorss=true;
        	dateList.add(cal.getTime());
        }
		
		/**
		Date now =new Date();
		Date today=sdf.parse(sdf.format(now));
		//开始日期是今天的
		if(today.getTime()==gjob.getStartDate().getTime()){
			Date start =this.getdate(gjob.getStartDate(), gjob.getArriveDate());
			//如果开始是是今天的，而且开始时间大于当前的时间
			if(start.getTime()>now.getTime()){
				dateList.add(today);
			}
		}
		//结束日期不是今天的
		if(today.getTime()<gjob.getEndDate().getTime()){
			cal.setTime(today);  
			cal.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
			today =cal.getTime();
			while (!today.after(gjob.getEndDate())){
				dateList.add(today);
				cal.setTime(today);  
				cal.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
				today =cal.getTime();
			}
		}
		*/
		
		for(User user:users){
			User db =userService.getById(user.getId());
			MessageU mu =new MessageU();
			mu.setMessage(am);
			mu.setUser(db);
			mu.setPids(gjob.getGuard().getId().toString());
			mu.setGjob(gjob);
			messageUService.save(mu);
			//新增签到记录
			for(int i=0;i<dateList.size();i++){
			     Date da=dateList.get(i);
			     if(!(iscorss && i==dateList.size()-1)){
			    	 Sign sign =new Sign();
						sign.setUser(db);
						sign.setGjob(gjob);
						sign.setGuard(gjob.getGuard());
						sign.setShouleBe(this.getdate(da, gjob.getArriveDate()));
						sign.setStation(gjob.getGuard().getStation());
						sign.setAddress(gjob.getGuard().getAddress());
						sign.setpName(gjob.getGuard().getName());
						signService.save(sign);
			     }
				if(!(iscorss && i==0)){
					Sign signout =new Sign();
					signout.setUser(db);
					signout.setGjob(gjob);
					signout.setGuard(gjob.getGuard());
					signout.setShouleBe(this.getdate(da, gjob.getLeaveDate()));
					signout.setStation(gjob.getGuard().getStation());
					signout.setAddress(gjob.getGuard().getAddress());
					signout.setpName(gjob.getGuard().getName());
					signout.setStype(1);
					signService.save(signout);
				}
			}
			
			
			if(StringUtils.isBlank(db.getAtype())){
				noapp.append(db.getFirstName()).append(",");
			}else{
				try{
					if(db.getAtype().equalsIgnoreCase("iOS")){
						iosPushUtils.setPushToken(user.getDevice_token());
						iosPushUtils.setTitle(message);
						iosPushUtils.iPush();
					}else if(db.getAtype().equalsIgnoreCase("android")){
						baiduPushUtitls.setTitle("广联工作安排通知");
						baiduPushUtitls.setChannelID(user.getDevice_token());
						baiduPushUtitls.setDeviceType(3);
						baiduPushUtitls.setMessage(message);
						baiduPushUtitls.pushMessage();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		if(noapp.length()>0){
			return noapp.deleteCharAt(noapp.lastIndexOf(",")).toString();
		}
		return "0";
	}

	private boolean hasJob(String st1, String en1,String st2,String en2) throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date s1 = df.parse("2017-07-17 "+st1);
		 Date e1 = df.parse("2017-07-17 "+en1);
		 Date s2 = df.parse("2017-07-17 "+st2);
		 Date e2 = df.parse("2017-07-17 "+en2);
		 if(s2.getTime()>e1.getTime() || e2.getTime()<s1.getTime()){
			 return false;
		 }
		return true;
	}
	private Date getdate(Date date,String time) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String date1 =dateFormat.format(date);
		date1=date1+" "+time;
		Date start =sdf.parse(date1);
		return start;
	}
	@Override
	public List<GJob> getToday(Date today,Long uid) throws ServiceException {
		// TODO Auto-generated method stub
		return gjobDao.getToday(today, uid);
	}

	@Override
	public List<GJob> getEndDate(Date end) throws ServiceException {
		// TODO Auto-generated method stub
		return gjobDao.getEndDate(end);
	}

	@Override
	public List<GJob> getNoAvaliable(Date end) throws ServiceException {
		// TODO Auto-generated method stub
		return gjobDao.getNoAvaliable(end);
	}

	@Override
	public List<GJob> getEndDate(Date end, Long uid) throws ServiceException {
		// TODO Auto-generated method stub
		return gjobDao.getEndDate(end, uid);
	}

	@Override
	@Transactional
	public void changestatus(GJob gjob) throws ServiceException, ParseException {
		// TODO Auto-generated method stub
		gjob.setRstatus(1);
		super.update(gjob);
		List<Sign> signs = signService.getByJId(gjob.getId());
		if(signs!=null && signs.size()>0){
			//Date date =new Date();
			for(Sign s:signs){
				//if(s.getShouleBe().getTime()>date.getTime()){
					signService.delete(s);
				//}
			}
		}
		
	}
}

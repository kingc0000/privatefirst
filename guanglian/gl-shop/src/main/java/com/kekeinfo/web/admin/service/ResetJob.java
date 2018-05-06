package com.kekeinfo.web.admin.service;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

public class ResetJob {
	public void autoSend() {
        Date date =new Date();
        System.err.println("****:" + date.toString());
    }

    private Scheduler scheduler;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void restJob(String dateTime,String key) {
        TriggerKey triggerKey=new TriggerKey(key, Scheduler.DEFAULT_GROUP);
        Trigger simpleTrigger=null;
        try {
            simpleTrigger=  (Trigger)scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        //simpleTrigger.setRepeatInterval(time);
        
        try {
        	if(simpleTrigger!=null){
        		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key, Scheduler.DEFAULT_GROUP)
                        .startNow().withSchedule(CronScheduleBuilder.cronSchedule(dateTime)).build();
        		scheduler.rescheduleJob(triggerKey,trigger);
        	}
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

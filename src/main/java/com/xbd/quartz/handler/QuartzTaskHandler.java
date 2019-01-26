package com.xbd.quartz.handler;

import java.util.ArrayList;
import java.util.Set;

import com.xbd.quartz.task.NormalTask;
import com.xbd.quartz.task.RssTask;
import com.xbd.quartz.task.Task;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzTaskHandler {
	private Scheduler scheduler;
	
	public QuartzTaskHandler(Scheduler scheduler){
		this.scheduler = scheduler;
	}
	
	/**
	 * add task when it doesn't exist
	 * @param task
	 * @return return false if the task exists
	 * @throws SchedulerException
	 */
    public boolean addTask(Task task) throws SchedulerException {
    	if(this.isExists(task)){
    		return false;
    	}
    	
    	this.createTask(task);
    	
    	return true;
    }
    
    private void createTask(Task task) throws SchedulerException{
    	if(task instanceof RssTask){
    		RssTask quartzTask = (RssTask) task;
    		
    		if(quartzTask.getRssJobSize() > 0){
        		for(int i = 0, size = quartzTask.getRssJobSize(); i < size; i++){
        			String name = String.format("%s_%d", quartzTask.getRssId(), i);
        			
        			this.createTask(name, quartzTask);
        		}
        	}
    		
    	}else if(task instanceof NormalTask){
    		NormalTask quartzTask = (NormalTask) task;
    		
    		this.createTask(quartzTask.getName(), quartzTask);
    	}
    }
    
    private boolean isExists(Task task) throws SchedulerException{
    	String name = null;
    	
    	if(task instanceof RssTask){
    		RssTask quartzTask = (RssTask) task;
    		
    		if(quartzTask.getRssJobSize() > 0){
    			name = String.format("%d_%d", quartzTask.getRssId(), 0);
        	}
    		
    	}else if(task instanceof NormalTask){
    		NormalTask quartzTask = (NormalTask) task;
    		
    		name = quartzTask.getName();
    	}
    	
    	return name != null ? this.scheduler.checkExists(new JobKey(name, task.getGroup())) : false;
    }
    
    private void createTask(String name, Task quartzTask) throws SchedulerException{
    	Class<? extends QuartzJobBean> cls = quartzTask instanceof RssTask ? RssJobBean.class : NormalJobBean.class;
    	
    	JobBuilder jb = JobBuilder
                .newJob(cls)
                .withIdentity(name, quartzTask.getGroup())
                .withDescription(quartzTask.getDescription())
                .storeDurably();
    	
    	if(quartzTask instanceof RssTask){
    		jb.usingJobData(RssJobBean.RSSIDKEY, ((RssTask) quartzTask).getRssId());
    	}

        CronScheduleBuilder cronScheduleBuilder = this.initCronScheduleBuilder(quartzTask);

        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(name, quartzTask.getGroup()))
                .withSchedule(cronScheduleBuilder);

        if (quartzTask.getStartAt() != null) {
            triggerBuilder.startAt(quartzTask.getStartAt());
        }

        if (quartzTask.isStartNow()) {
            triggerBuilder.startNow();
        }

        if (quartzTask.getEndAt() != null) {
            triggerBuilder.endAt(quartzTask.getEndAt());
        }

        CronTrigger cronTrigger = triggerBuilder.build();

        scheduler.scheduleJob(jb.build(), cronTrigger);
    }
    
    /**
     * update task. if the task exists, it will be deleted first, then added.
     * @param quartzTask
     * @throws SchedulerException
     */
    public void updateTask(Task quartzTask) throws SchedulerException {
    	this.deleteTask(quartzTask);
    	this.createTask(quartzTask);
    }
    
    public void pauseTask(Task quartzTask) throws SchedulerException {
        scheduler.pauseJobs(GroupMatcher.groupEquals(quartzTask.getGroup()));
    }
    
    public void pauseAllTask() throws SchedulerException {
        scheduler.pauseAll();
    }
    
    public void resumeTask(Task quartzTask) throws SchedulerException {
        scheduler.resumeJobs(GroupMatcher.groupEquals(quartzTask.getGroup()));
    }
    
    public void deleteTask(Task quartzTask) throws SchedulerException {
    	Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.groupEquals(quartzTask.getGroup()));
    	
    	if(CollectionUtils.isNotEmpty(keys)){
    		scheduler.deleteJobs(new ArrayList<>(keys)); 
    	}
    }
    
    public void fireOnce(Task quartzTask) throws SchedulerException{
    	Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.groupEquals(quartzTask.getGroup()));
    	if(CollectionUtils.isNotEmpty(keys)){
    		for(JobKey key : keys){
    			scheduler.triggerJob(key);
    		}
    	}
    }
    
    private CronScheduleBuilder initCronScheduleBuilder(Task quartzTask) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTask.getCronExpression());
        
        switch(quartzTask.getMisfireInstruction()){
	        case MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
	        	cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
	        	break;
	        	
	        case MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
	        	cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
	        	break;
	        	
	        case MISFIRE_INSTRUCTION_DO_NOTHING:
	        	cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
	        	break;
	        	
        	default:
        		break;
        }
        
        return cronScheduleBuilder;
    }
}

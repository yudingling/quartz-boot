package com.xbd.quartz.task;

import java.io.Serializable;
import java.util.Date;

public abstract class Task implements Serializable {
	private static final long serialVersionUID = 1488196450884232116L;
	
	private String description;

    private Date startAt;

    private boolean startNow;

    private Date endAt;

    private String cronExpression;
    
    private MisfireInstruction misfireInstruction = MisfireInstruction.MISFIRE_INSTRUCTION_DO_NOTHING;
	
    public abstract String getGroup();

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public boolean isStartNow() {
        return startNow;
    }

    public void setStartNow(boolean startNow) {
        this.startNow = startNow;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public MisfireInstruction getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(MisfireInstruction misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

	public enum MisfireInstruction{
    	/**
    	 * Instructs the Scheduler that upon a mis-fire situation, the CronTrigger wants to be fired now by Scheduler. 
    	 */
    	MISFIRE_INSTRUCTION_FIRE_ONCE_NOW,
    	
    	/**
    	 * Instructs the Scheduler that the Trigger will never be evaluated for a misfire situation, 
    	 * and that the scheduler will simply try to fire it as soon as it can, 
    	 * and then update the Trigger as if it had fired at the proper time. 
    	 */
    	MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY,
    	
    	/**
    	 * Instructs the Scheduler that upon a mis-fire situation, the CronTrigger wants to have it's next-fire-time updated 
    	 * to the next time in the schedule after the current time (taking into account any associated Calendar, but it does not want to be fired now.)
    	 */
    	MISFIRE_INSTRUCTION_DO_NOTHING,
    	
    	/**
    	 * smart policy
    	 */
    	MISFIRE_INSTRUCTION_SMART_POLICY
    }
}

package com.xbd.quartz.task;

public class RssTask extends Task {
	private static final long serialVersionUID = -7200605810737859329L;

	private Long rssId;
    
    private int rssJobSize;

    public Long getRssId() {
		return rssId;
	}

	public void setRssId(Long rssId) {
		this.rssId = rssId;
	}
	
	/**
	 * get the group of task. (rssId)
	 * @return
	 */
	@Override
	public String getGroup() {
        return this.rssId + "";
    }
    
    public int getRssJobSize() {
		return rssJobSize;
	}

	public void setRssJobSize(int rssJobSize) {
		this.rssJobSize = rssJobSize;
	}
}

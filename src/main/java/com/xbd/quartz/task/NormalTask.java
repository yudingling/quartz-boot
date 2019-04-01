package com.xbd.quartz.task;

public class NormalTask extends Task {
	private static final long serialVersionUID = 6900012254006253925L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getGroup() {
		return this.name;
	}
}

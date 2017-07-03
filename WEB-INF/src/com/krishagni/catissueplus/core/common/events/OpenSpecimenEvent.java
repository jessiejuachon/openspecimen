package com.krishagni.catissueplus.core.common.events;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.ApplicationEvent;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class OpenSpecimenEvent extends ApplicationEvent {
	private String eventName;
	
	private Object source;
	
	private User user;
	
	private Date time;
	
	public OpenSpecimenEvent(Object source, String eventName) {
		super(source);
		this.source = source;
		this.eventName = eventName;
		this.time = Calendar.getInstance().getTime();
		this.user = AuthUtil.getCurrentUser();
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object object) {
		this.source = object;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setTime(Date time) {
		this.time = time;
	}	
}

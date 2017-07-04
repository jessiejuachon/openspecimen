package com.krishagni.catissueplus.core.common.events;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

@Configurable
public class EventPublisher implements ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher publisher;
	
	private static EventPublisher instance = new EventPublisher();
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public static EventPublisher getInstance() {
		return instance;
	}
	
	public <T> void publish(OpenSpecimenEvent<T> event) {
		publisher.publishEvent(event);
	}
}

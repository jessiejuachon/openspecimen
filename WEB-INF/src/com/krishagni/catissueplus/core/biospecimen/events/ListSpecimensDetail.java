package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;


public class ListSpecimensDetail {
	private List<SpecimenInfo> specimens = new ArrayList<>();
	
	private Integer actualCount;

	public List<SpecimenInfo> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenInfo> specimens) {
		this.specimens = specimens;
	}

	public Integer getActualCount() {
		return actualCount;
	}

	public void setActualCount(Integer actualCount) {
		this.actualCount = actualCount;
	}
	
	public static ListSpecimensDetail from(Integer actualCount) {
		return from(Collections.emptyList(), actualCount);
	}
	
	public static ListSpecimensDetail from(Collection<Specimen> specimens, Integer actualCount) {
		ListSpecimensDetail detail = new ListSpecimensDetail();
		detail.setSpecimens(SpecimenInfo.from(new ArrayList<>(specimens)));
		detail.setActualCount(actualCount);
		return detail;
	}
}

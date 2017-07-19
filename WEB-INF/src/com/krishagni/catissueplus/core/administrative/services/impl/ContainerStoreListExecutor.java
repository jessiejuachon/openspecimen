package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ContainerStoreList;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

@Configurable
public class ContainerStoreListExecutor implements ScheduledTask {

	@Autowired
	private DaoFactory daoFactory;

	@Override
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		int maxResults = 15;

		boolean hasPendingLists = true;
		while (hasPendingLists) {
			List<ContainerStoreList> storeLists = getPendingStoreList(maxResults);
			storeLists.forEach(ContainerStoreList::process);
			if (storeLists.size() < maxResults) {
				hasPendingLists = false;
			}
		}
	}

	@PlusTransactional
	public List<ContainerStoreList> getPendingStoreList(int maxResults) {
		List<ContainerStoreList> storeLists = daoFactory.getContainerStoreListDao().getPendingStoreLists(0, maxResults);
		for (ContainerStoreList list : storeLists) {
			Hibernate.initialize(list.getContainer());
			Hibernate.initialize(list.getContainer().getAutoFreezerProvider());
		}

		return storeLists;
	}
}

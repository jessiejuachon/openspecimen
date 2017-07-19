package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.PrintRule;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.PrintRuleFactory;
import com.krishagni.catissueplus.core.biospecimen.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.PrintRuleService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PrintRuleServiceImpl implements PrintRuleService {
	private DaoFactory daoFactory;

	private PrintRuleFactory printRuleFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setPrintRuleFactory(PrintRuleFactory printRuleFactory) {
		this.printRuleFactory = printRuleFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<PrintRuleDetail> createPrintRule(RequestEvent<PrintRuleDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			PrintRuleDetail detail = req.getPayload();

			PrintRule rule = printRuleFactory.createPrintRule(detail);
			daoFactory.getPrintRuleDao().saveOrUpdate(rule);

			return ResponseEvent.response(PrintRuleDetail.from(rule));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}

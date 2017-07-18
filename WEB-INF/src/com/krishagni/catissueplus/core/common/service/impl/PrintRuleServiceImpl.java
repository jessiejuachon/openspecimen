package com.krishagni.catissueplus.core.common.service.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.PrintRule;
import com.krishagni.catissueplus.core.common.domain.factory.PrintRuleFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.PrintRuleService;

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
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

			PrintRule printRule = printRuleFactory.createPrintRule(detail);
			ose.checkAndThrow();

			daoFactory.getPrintRuleDao().saveOrUpdate(printRule);
			return ResponseEvent.response(PrintRuleDetail.from(printRule));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}

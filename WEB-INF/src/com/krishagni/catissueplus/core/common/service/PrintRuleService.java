package com.krishagni.catissueplus.core.common.service;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.repository.PrintRuleCritetia;

public interface PrintRuleService {
	ResponseEvent<List<PrintRuleDetail>> getPrintRules(RequestEvent<PrintRuleCritetia> req);

	ResponseEvent<PrintRuleDetail> createPrintRule(RequestEvent<PrintRuleDetail> req);
}

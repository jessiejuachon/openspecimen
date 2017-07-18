package com.krishagni.catissueplus.core.common.service;

import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface PrintRuleService {
	ResponseEvent<PrintRuleDetail> createPrintRule(RequestEvent<PrintRuleDetail> req);
}

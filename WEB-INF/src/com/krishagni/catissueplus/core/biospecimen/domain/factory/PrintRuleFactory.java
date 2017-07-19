package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.PrintRule;
import com.krishagni.catissueplus.core.biospecimen.events.PrintRuleDetail;

public interface PrintRuleFactory {
	PrintRule createPrintRule(PrintRuleDetail detail);
}

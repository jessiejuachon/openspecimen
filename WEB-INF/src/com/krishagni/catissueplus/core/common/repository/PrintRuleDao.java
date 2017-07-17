package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.PrintRule;

public interface PrintRuleDao extends Dao<PrintRule> {
	List<PrintRule> getPrintRules(PrintRuleCritetia crit);
}

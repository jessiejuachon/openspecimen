package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import org.hibernate.Criteria;

import com.krishagni.catissueplus.core.common.domain.PrintRule;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.PrintRuleCritetia;
import com.krishagni.catissueplus.core.common.repository.PrintRuleDao;

public class PrintRuleDaoImpl extends AbstractDao<PrintRule> implements PrintRuleDao {
	@Override
	public Class<?> getType() {
		return PrintRule.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrintRule> getPrintRules(PrintRuleCritetia crit) {
		return getPrintRulesCrit(crit)
				.setMaxResults(crit.maxResults())
				.list();
	}

	public Criteria getPrintRulesCrit(PrintRuleCritetia crit) {
		return sessionFactory.getCurrentSession()
				.createCriteria(PrintRule.class);
	}
}

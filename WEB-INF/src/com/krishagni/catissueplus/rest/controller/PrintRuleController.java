package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.repository.PrintRuleCritetia;
import com.krishagni.catissueplus.core.common.service.PrintRuleService;

@Controller
@RequestMapping("/print-rules")
public class PrintRuleController {
	@Autowired
	private PrintRuleService printRuleSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<PrintRuleDetail> getPrintRules(
			@RequestParam(value = "maxResults", required = false, defaultValue = "100")
			int maxResults) {
		PrintRuleCritetia crit = new PrintRuleCritetia()
				.maxResults(maxResults);

		RequestEvent<PrintRuleCritetia> req = new RequestEvent<PrintRuleCritetia>(crit);
		ResponseEvent<List<PrintRuleDetail>> resp = printRuleSvc.getPrintRules(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PrintRuleDetail createPrintRule(@RequestBody PrintRuleDetail detail) {
		RequestEvent<PrintRuleDetail> req = new RequestEvent<PrintRuleDetail>(detail);
		ResponseEvent<PrintRuleDetail> resp = printRuleSvc.createPrintRule(req);
		resp.throwErrorIfUnsuccessful();

		return resp.getPayload();
	}
}

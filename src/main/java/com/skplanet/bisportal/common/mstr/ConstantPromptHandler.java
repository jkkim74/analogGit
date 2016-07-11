package com.skplanet.bisportal.common.mstr;

import lombok.extern.slf4j.Slf4j;

import com.microstrategy.web.objects.WebConstantPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.skplanet.bisportal.model.mstr.MstrRequest;

/**
 * The ConstantPromptHandler class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
public class ConstantPromptHandler implements PromptHandler {
	@Override
	public String handlePrompt(WebPrompt prompt, MstrRequest mstrRequest) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		WebConstantPrompt constantPrompt = (WebConstantPrompt) prompt;
		String description = constantPrompt.getDescription().toUpperCase();
		String trClass = "drop-down-item";
		String addClass = "mstr-text";
		if (description.indexOf("YYYY") != -1 || description.indexOf("MM") != -1 || description.indexOf("DD") != -1) {
			trClass = "hidden";
			addClass = "mstr-date date-" + (description.indexOf("START") != -1 ? "start" : "end");
			if (description.indexOf("WEEK") != -1) {
				if (description.indexOf("WEEK2") != -1)
					addClass += " week2";
				else if (description.indexOf("WEEK3") != -1)
					addClass += " week3";
				else
					addClass += " week1";
			}
		}
		promptStr.append("<tr class='" + trClass + "'><th scope='row'>항목검색</th><td>");
		promptStr.append("<div class='select-box'>");
		promptStr.append("<ul class='list-inline constant-prompt " + addClass + "'>");
		promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + constantPrompt.getID() + "'>"
				+ constantPrompt.getTitle() + "</label>");
		promptStr.append("<li><input type='text' id='" + mstrRequest.getMenuCode() + "_" + constantPrompt.getID() + "' "
				+ "name='" + mstrRequest.getMenuCode() + "_" + constantPrompt.getID() + "' "
				+ "data-answer='<pa pt=\"3\" pin=\"0\" did=\"" + constantPrompt.getID() + "\" "
				+ "tp=\"" + constantPrompt.getType() + "\">#constant-answer#</pa>' "
				+ "value=''/></li>");
		promptStr.append("</ul></div></td></tr>");
		return promptStr.toString();
	}
}

package com.skplanet.bisportal.common.mstr;

import lombok.extern.slf4j.Slf4j;

import com.microstrategy.web.objects.WebElementSource;
import com.microstrategy.web.objects.WebElements;
import com.microstrategy.web.objects.WebElementsPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import org.apache.commons.lang.StringUtils;

/**
 * The ConstantPromptHandler class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
public class ElementsPromptHandler implements PromptHandler {
	@Override
	public String handlePrompt(WebPrompt prompt, MstrRequest mstrRequest) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		WebElementsPrompt elePrompt = (WebElementsPrompt) prompt;
		WebElementSource eltSrc = elePrompt.getOrigin().getElementSource();
		WebElements elements = elePrompt.getSuggestedAnswers();
		WebElements eleAnswer = elePrompt.getAnswer();
		if (elements == null) {
			elements = eltSrc.getElements();
		}
		String description = elePrompt.getDescription().toUpperCase();
		int elementsSize = elements.size();
		int eleAnswerSize = eleAnswer.size();
		if (description.indexOf("ONE_DOWN") != -1) { //SELECT
			promptStr.append("<tr class='drop-down-item'><th scope='row'>항목검색</th><td>");
			promptStr.append("<div class='select-box'>");
			promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + elePrompt.getID() + "' "
					+ "class='text-check'>" + elePrompt.getTitle() + "&nbsp;</label>");
			promptStr.append("<select id='" + mstrRequest.getMenuCode() + "_" + elePrompt.getID() + "' "
					+ "class='select elements-prompt mstr-one'>");
			for (int i = 0; i < elementsSize; i++) {
				String isSelected = StringUtils.EMPTY;
				for (int j = 0; j < eleAnswerSize; j++) {
					if (StringUtils.equals(elements.get(i).getElementID(), eleAnswer.get(j).getElementID())) {
						isSelected = "selected='selected'";
					}
				}
				promptStr.append("<option value='<pa pt=\"7\" pin=\"0\" did=\"" + elePrompt.getID() + "\" "
						+ "tp=\"" + elePrompt.getType() + "\"><mi><es>"
						+ "<at did=\"" + elePrompt.getOrigin().getID() + "\" tp=\"12\"/>"
						+ "<e emt=\"1\" ei=\"" + elements.get(i).getElementID() + "\" art=\"1\"/></es></mi></pa>' "
						+ isSelected + ">"
						+ elements.get(i).getDisplayName() + "</option>");
			}
			promptStr.append("</select></div></td></tr>");
		} else if (description.indexOf("ONE") != -1) { //RADIO
			promptStr.append("<tr><th scope='row'>" + elePrompt.getTitle() + "</th><td>"
					+ "<ul class='list-inline elements-prompt mstr-one mstr-radio'>");
			for (int i = 0; i < elementsSize; i++) {
				String isChecked = StringUtils.EMPTY;
				for (int j = 0; j < eleAnswerSize; j++) {
					if (StringUtils.equals(elements.get(i).getElementID(), eleAnswer.get(j).getElementID())) {
						isChecked = "checked='checked'";
					}
				}
				promptStr.append("<li class='radio'>");
				promptStr.append("<input type='radio' id='" + mstrRequest.getMenuCode() + "_" + elements.get(i).getID() + "' "
						+ "name='" + mstrRequest.getMenuCode() + "_" + elePrompt.getID() + "' "
						+ "value='<pa pt=\"7\" pin=\"0\" did=\"" + elePrompt.getID() + "\" "
						+ "tp=\"" + elePrompt.getType() + "\"><mi><es>"
						+ "<at did=\"" + elePrompt.getOrigin().getID() + "\" tp=\"12\"/>"
						+ "<e emt=\"1\" ei=\"" + elements.get(i).getElementID() + "\" art=\"1\"/></es></mi></pa>' "
						+ isChecked + "/>");
				promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + elements.get(i).getID() + "'>"
						+ elements.get(i).getDisplayName() + "</label>");
				promptStr.append("</li>");

			}
			promptStr.append("</ul></td></tr>");
//		} else if (description.indexOf("MULTI_DOWN") != -1) { //CHECKBOX(DROPDOWN)
		} else if (description.indexOf("MULTI") != -1) { //CHECKBOX
			promptStr.append("<tr><th scope='row'>" + elePrompt.getTitle() + "</th><td>"
					+ "<ul class='list-inline elements-prompt mstr-multi' "
					+ "data-prefix='<pa pt=\"7\" pin=\"0\" did=\"" + elePrompt.getID() + "\" "
					+ "tp=\"" + elePrompt.getType() + "\"><mi><es>"
					+ "<at did=\"" + elePrompt.getOrigin().getID() + "\" tp=\"12\"/>' data-suffix='</es></mi></pa>'>");
			for (int i = 0; i < elementsSize; i++) {
				String isChecked = StringUtils.EMPTY;
				for (int j = 0; j < eleAnswerSize; j++) {
					if (StringUtils.equals(elements.get(i).getElementID(), eleAnswer.get(j).getElementID())) {
						isChecked = "checked='checked'";
					}
				}
				promptStr.append("<li class='checkbox'>"
						+ "<input type='checkbox' id='" + mstrRequest.getMenuCode() + "_" + elements.get(i).getID() + "' "
						+ "name='" + mstrRequest.getMenuCode() + "_" + elePrompt.getID() + "' "
						+ "value='<e emt=\"1\" ei=\"" + elements.get(i).getElementID() + "\" art=\"1\"/>' "
						+ isChecked + ">"
						+ "<label for='" + mstrRequest.getMenuCode() + "_" + elements.get(i).getID() + "'>"
						+ elements.get(i).getDisplayName() + "</label>"
						+ "</li>");
			}
			promptStr.append("</ul></td></tr>");
		}
		return promptStr.toString();
	}
}

package com.skplanet.bisportal.common.mstr;

import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectsPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Enumeration;

/**
 * The ConstantPromptHandler class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
public class ObjectsPromptHandler implements PromptHandler {
	@Override
	@SuppressWarnings("rawtypes")
	public String handlePrompt(WebPrompt prompt, MstrRequest mstrRequest) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		WebObjectsPrompt objPrompt = (WebObjectsPrompt) prompt;
		WebFolder fdrSug = objPrompt.getSuggestedAnswers();
		WebFolder fdrAns = objPrompt.getAnswer();
		WebObjectInfo oInfoSug;
		WebObjectInfo oInfoAns;
		String description = objPrompt.getDescription().toUpperCase();
		if (description.indexOf("ONE_DOWN") != -1) { //SELECT
			promptStr.append("<tr class='drop-down-item'><th scope='row'>항목검색</th><td>");
			promptStr.append("<div class='select-box'>");
			promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + objPrompt.getID() + "' "
					+ "class='text-check'>" + objPrompt.getTitle() + "&nbsp;</label>");
			promptStr.append("<select id='" + mstrRequest.getMenuCode() + "_" + objPrompt.getID() + "' "
					+ "class='select objects-prompt mstr-one'>");
			for (Enumeration e = fdrSug.elements(); e.hasMoreElements(); ) {
				String isSelected = StringUtils.EMPTY;
				oInfoSug = (WebObjectInfo) e.nextElement();
				for (Enumeration i = fdrAns.elements(); i.hasMoreElements(); ) {
					oInfoAns = (WebObjectInfo) i.nextElement();
					if (StringUtils.equals(oInfoSug.getID(), oInfoAns.getID())) {
						isSelected = "selected='selected'";
					}
				}
				promptStr.append("<option value='<pa pt=\"6\" pin=\"0\" did=\"" + objPrompt.getID() + "\" "
						+ "tp=\"" + objPrompt.getType() + "\">"
						+ "<mi><fct qsr=\"0\" fcn=\"0\" cc=\"1\" sto=\"1\" pfc=\"0\" pcc=\"1\">"
						+ "<" + (oInfoSug.getType() == 4 ? "mt" : "at") + " did=\"" + oInfoSug.getID() + "\" "
						+ "tp=\"" + oInfoSug.getType() + "\"/>"
						+ "</fct></mi></pa>' "
						+ isSelected + ">"
						+ oInfoSug.getDisplayName() + "</option>");
			}
			promptStr.append("</select></div></td></tr>");
		} else if (description.indexOf("ONE") != -1) { //RADIO
			promptStr.append("<tr><th scope='row'>" + objPrompt.getTitle() + "</th><td>");
			promptStr.append("<ul class='list-inline objects-prompt mstr-one mstr-radio' data-pa-did='" + objPrompt.getID()
					+ "' data-type='" + objPrompt.getType() + "'>");
			for (Enumeration e = fdrSug.elements(); e.hasMoreElements(); ) {
				String isChecked = StringUtils.EMPTY;
				oInfoSug = (WebObjectInfo) e.nextElement();
				for (java.util.Enumeration i = fdrAns.elements(); i.hasMoreElements(); ) {
					oInfoAns = (WebObjectInfo) i.nextElement();
					if (StringUtils.equals(oInfoSug.getID(), oInfoAns.getID())) {
						isChecked = "checked='checked'";
					}
				}
				promptStr.append("<li class='radio'>");
				promptStr.append("<input type='radio' id='" + mstrRequest.getMenuCode() + "_" + oInfoSug.getID() + "' "
						+ "name='" + mstrRequest.getMenuCode() + "_" + objPrompt.getID()  + "' "
						+ "value='<pa pt=\"6\" pin=\"0\" did=\"" + objPrompt.getID() + "\" "
						+ "tp=\"" + objPrompt.getType() + "\">"
						+ "<mi><fct qsr=\"0\" fcn=\"0\" cc=\"1\" sto=\"1\" pfc=\"0\" pcc=\"1\">"
						+ "<" + (oInfoSug.getType() == 4 ? "mt" : "at") + " did=\"" + oInfoSug.getID() + "\" "
						+ "tp=\"" + oInfoSug.getType() + "\"/>"
						+ "</fct></mi></pa>' "
						+ isChecked + "/>");
				promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + oInfoSug.getID() + "'>" + oInfoSug.getDisplayName() + "</label>");
				promptStr.append("</li>");
			}
			promptStr.append("</ul>");
			promptStr.append("</td></tr>");
//		} else if (description.indexOf("MULTI_DOWN") != -1) { //CHECKBOX(DROPDOWN)
		} else if (description.indexOf("MULTI") != -1) { //CHECKBOX
			promptStr.append("<tr><th scope='row'>" + objPrompt.getTitle() + "</th><td>");
			promptStr.append("<ul class='list-inline objects-prompt mstr-multi' "
					+ "data-prefix='<pa pt=\"6\" pin=\"0\" did=\"" + objPrompt.getID() + "\" "
					+ "tp=\"" + objPrompt.getType() + "\"><mi>"
					+ "<fct qsr=\"0\" fcn=\"0\" cc=\"#objects-answerCount#\" sto=\"1\" pfc=\"0\" pcc=\"#objects-answerCount#\">' "
					+ "data-suffix='</fct></mi></pa>'>");
			for (Enumeration e = fdrSug.elements(); e.hasMoreElements(); ) {
				String isChecked = StringUtils.EMPTY;
				oInfoSug = (WebObjectInfo) e.nextElement();
				for (java.util.Enumeration i = fdrAns.elements(); i.hasMoreElements(); ) {
					oInfoAns = (WebObjectInfo) i.nextElement();
					if (StringUtils.equals(oInfoSug.getID(), oInfoAns.getID())) {
						isChecked = "checked='checked'";
					}
				}
				promptStr.append("<li class='checkbox'>");
				promptStr.append("<input type='checkbox' id='" + mstrRequest.getMenuCode() + "_" + oInfoSug.getID() + "' "
						+ "name='" + mstrRequest.getMenuCode() + "_" + objPrompt.getID() + "' "
						+ "value='<" + (oInfoSug.getType() == 4 ? "mt" : "at") + " did=\"" + oInfoSug.getID() + "\" "
						+ "tp=\"" + oInfoSug.getType() + "\"/>' " + isChecked + "/>");
				promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + oInfoSug.getID() + "'>"
						+ oInfoSug.getDisplayName() + "</label>");
				promptStr.append("</li>");
			}
			promptStr.append("</ul>");
			promptStr.append("</td></tr>");
		}

		return promptStr.toString();
	}
}

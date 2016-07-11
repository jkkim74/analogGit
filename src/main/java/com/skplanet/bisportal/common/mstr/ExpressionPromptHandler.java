package com.skplanet.bisportal.common.mstr;

import com.skplanet.bisportal.common.consts.Constants;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.microstrategy.web.objects.WebDimension;
import com.microstrategy.web.objects.WebDisplayUnit;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebExpressionPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.microstrategy.webapi.EnumDSSXMLExpressionType;
import com.skplanet.bisportal.common.utils.PropertiesUtil;
import com.skplanet.bisportal.model.mstr.MstrRequest;

/**
 * The ConstantPromptHandler class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
public class ExpressionPromptHandler implements PromptHandler {
	private String EXP_FORM_ID_OBJECT;

	public ExpressionPromptHandler() {
		EXP_FORM_ID_OBJECT = PropertiesUtil.getProperty("mstr.exp.form.id.object");
	}

	@Override
	public String handlePrompt(WebPrompt prompt, MstrRequest mstrRequest) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		WebExpressionPrompt expPrompt = (WebExpressionPrompt) prompt;
		WebDisplayUnits webDisplayUnits = expPrompt.getDisplayHelper().getAvailableDisplayUnits();
		int webDisplayUnitsSize = webDisplayUnits.size();
		String description = expPrompt.getDescription().toUpperCase();
		if (expPrompt.getExpressionType() == EnumDSSXMLExpressionType.DssXmlFilterAllAttributeQual) {
			String trClass = StringUtils.EMPTY;
			String addClass = StringUtils.EMPTY;
			if (description.indexOf("DAY") != -1 || description.indexOf("WEEK") != -1 || description.indexOf("MONTH") != -1) {
				trClass = "hidden";
				addClass = "mstr-date";
				if (description.indexOf("WEEK") != -1) {
					if (description.indexOf("WEEK2") != -1)
						addClass += " week2";
					else if (description.indexOf("WEEK3") != -1)
						addClass += " week3";
					else
						addClass += " week1";
				}
			}
			promptStr.append("<tr class='" + trClass + "'><th scope='row'>" + expPrompt.getTitle() + "</th><td>");
			promptStr.append("<ul class='list-inline expression-prompt " + addClass + "'>");
			for (int i = 0; i < webDisplayUnitsSize; i++) {
				WebDimension webDimension = (WebDimension) webDisplayUnits.get(i);
				int topLevelAttrSize = webDimension.getTopLevelAttributes().size();

				for (int j = 0; j < topLevelAttrSize; j++) {
					WebDisplayUnit unit = webDimension.getTopLevelAttributes().get(j);
					String unitId;
					String unitName = StringUtils.EMPTY;
					String unitClass = StringUtils.EMPTY;
					if (addClass.indexOf("mstr-date") != -1) {
						unitId = unit.getDisplayName().indexOf("월") != -1 ? Constants.DATE_MONTH :
								unit.getDisplayName().indexOf("주") != -1 ? Constants.DATE_WEEK : Constants.DATE_DAY;
						unitName = unit.getDisplayName().indexOf("월") != -1 ? "월" :
								unit.getDisplayName().indexOf("주") != -1 ? "주" : "일";
						unitClass = "date-" + unitId;
					}
					promptStr.append("<li class='radio'>");
					promptStr.append("<input type='radio' id='" + mstrRequest.getMenuCode() + "_" + unit.getID() + "' "
							+ "name='" + mstrRequest.getMenuCode() + "_" + expPrompt.getID() + "' " + "class='" + unitClass + "'"
							+ "value='<pa pt=\"8\" pin=\"0\" did=\"" + expPrompt.getID() + "\" "
							+ "tp=\"" + expPrompt.getType() + "\"><exp><nd et=\"2\" nt=\"4\" dmt=\"1\" ddt=\"-1\">"
							+ "<nd et=\"1\" nt=\"1\" dmt=\"1\" ddt=\"-1\" lcl=\"1042\">" + "<at did=\"" + unit.getID() + "\" tp=\"12\"/>"
							+ "<fm did=\"" + EXP_FORM_ID_OBJECT + "\" tp=\"21\"/></nd>"
							+ "<nd et=\"1\" nt=\"3\" dmt=\"1\" ddt=\"8\"><cst ddt=\"8\">#expression-date-answer1#</cst></nd>"
							+ "<nd et=\"1\" nt=\"3\" dmt=\"1\" ddt=\"8\"><cst ddt=\"8\">#expression-date-answer2#</cst></nd>"
							+ "<op fnt=\"17\" fg=\"0\" apply_order=\"0\"/></nd></exp></pa>'/>");
					promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + unit.getID() + "'>" + unitName + "</label>");
					promptStr.append("</li>");
				}
			}
			promptStr.append("</ul></td></tr>");
		} else if (expPrompt.getExpressionType() == EnumDSSXMLExpressionType.DssXmlFilterAttributeIDQual) {
			String trClass = StringUtils.EMPTY;
			String addClass = StringUtils.EMPTY;
			if (description.indexOf("DAY") != -1 || description.indexOf("WEEK") != -1 || description.indexOf("MONTH") != -1) {
				trClass = "hidden";
				addClass = "mstr-date";
				if (description.indexOf("WEEK") != -1) {
					if (description.indexOf("WEEK2") != -1)
						addClass += " week2";
					else if (description.indexOf("WEEK3") != -1)
						addClass += " week3";
					else
						addClass += " week1";
				}
			}
			promptStr.append("<tr class='" + trClass + "'><th scope='row'>" + expPrompt.getTitle() + "</th><td>");
			promptStr.append("<ul class='list-inline expression-prompt " + addClass + "'>");
			for (int i = 0; i < webDisplayUnitsSize; i++) {
				WebDisplayUnit unit = webDisplayUnits.get(i);
				String unitId;
				String unitName = StringUtils.EMPTY;
				String unitClass = StringUtils.EMPTY;
				if (addClass.indexOf("mstr-date") != -1) {
					unitId = unit.getDisplayName().indexOf("월") != -1 ? Constants.DATE_MONTH :
							unit.getDisplayName().indexOf("주") != -1 ? Constants.DATE_WEEK : Constants.DATE_DAY;
					unitName = unit.getDisplayName().indexOf("월") != -1 ? "월" :
							unit.getDisplayName().indexOf("주") != -1 ? "주" : "일";
					unitClass = "date-" + unitId;
				}
				promptStr.append("<li class='radio'>");
				promptStr.append("<input type='radio' id='" + mstrRequest.getMenuCode() + "_" + unit.getID() + "' "
						+ "name='" + mstrRequest.getMenuCode() + "_" + expPrompt.getID() + "' " + "class='" + unitClass + "'"
						+ "value='<pa pt=\"8\" pin=\"0\" did=\"" + expPrompt.getID() + "\" "
						+ "tp=\"" + expPrompt.getType() + "\"><exp><nd et=\"2\" nt=\"4\" dmt=\"1\" ddt=\"-1\">"
						+ "<nd et=\"1\" nt=\"1\" dmt=\"1\" ddt=\"-1\" lcl=\"1042\">" + "<at did=\"" + unit.getID() + "\" tp=\"12\"/>"
						+ "<fm did=\"" + EXP_FORM_ID_OBJECT + "\" tp=\"21\"/></nd>"
						+ "<nd et=\"1\" nt=\"3\" dmt=\"1\" ddt=\"8\"><cst ddt=\"8\">#expression-date-answer1#</cst></nd>"
						+ "<nd et=\"1\" nt=\"3\" dmt=\"1\" ddt=\"8\"><cst ddt=\"8\">#expression-date-answer2#</cst></nd>"
						+ "<op fnt=\"17\" fg=\"0\" apply_order=\"0\"/></nd></exp></pa>'/>");
				promptStr.append("<label for='" + mstrRequest.getMenuCode() + "_" + unit.getID() + "'>" + unitName
						+ "</label>");
				promptStr.append("</li>");
			}
		} else {
			log.warn("invalid ExpressionType!");
		}
		return promptStr.toString();
	}

	public String getDateTypes(WebPrompt prompt) throws Exception {
		StringBuffer promptStr = new StringBuffer(256);
		WebExpressionPrompt expPrompt = (WebExpressionPrompt) prompt;
		String description = expPrompt.getDescription().toUpperCase();
		if (description.indexOf("DAY") != -1) {
			promptStr.append((StringUtils.isNotEmpty(promptStr.toString()) ? "," : StringUtils.EMPTY) + Constants.DATE_DAY);
		}
		if (description.indexOf("WEEK") != -1) {
			promptStr.append((StringUtils.isNotEmpty(promptStr.toString()) ? "," : StringUtils.EMPTY) + Constants.DATE_WEEK);
		}
		if (description.indexOf("MONTH") != -1) {
			promptStr.append((StringUtils.isNotEmpty(promptStr.toString()) ? "," : StringUtils.EMPTY) + Constants.DATE_MONTH);
		}
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		log.debug("description : {}", description);
		log.debug("returns : {}", promptStr.toString());
		log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++");
		return promptStr.toString();
	}
}

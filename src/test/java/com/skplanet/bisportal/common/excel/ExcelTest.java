package com.skplanet.bisportal.common.excel;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pepsi on 2014. 6. 19..
 */
public class ExcelTest {
	private final Logger logger = LoggerFactory.getLogger(ExcelTest.class);
	@Test
	public void testJsoupExcel() throws Exception {
		// When
		Document docs = Jsoup.connect("http://www.wiseeco.com/pivot.html").get();
		int dataNumcols = Integer.parseInt(StringUtils.defaultIfEmpty(docs.select("table.pvtTable")
				.attr("data-numcols"), "0"));
		int headerColSpan = Integer.parseInt(StringUtils.defaultIfEmpty(docs.select("table tr.head-group th").first()
				.attr("colspan"), "0"));
		int dataNumrows = Integer.parseInt(StringUtils.defaultIfEmpty(docs.select("table.pvtTable")
				.attr("data-numrows"), "0"));
		int headLength = dataNumcols + headerColSpan;
		logger.debug("dataNumcols={}, headerColSpan={}, headLength={}, dataNumrows={}", dataNumcols, headerColSpan,
				headLength, dataNumrows);
		int merge1Row = 0, merge2Row = 0;
		for (int i = 2; i < dataNumrows + 2; i++) {
			int childrenSize = docs.select("table.pvtTable tr").get(i).children().size();
			logger.debug("childrenSize={}", childrenSize);
			Elements pvtRowLabels = docs.select("table.pvtTable tr").get(i).getElementsByTag("th");
			for (Element pvtRowLabel : pvtRowLabels) {
				logger.debug("pvtRowLabel={}", pvtRowLabel.toString());
			}
			Elements pvtVals = docs.select("table.pvtTable tr").get(i).getElementsByTag("td");
			for (Element pvtVal : pvtVals) {
				logger.debug("pvtVal={}", pvtVal.toString());
			}
		}
	}
}

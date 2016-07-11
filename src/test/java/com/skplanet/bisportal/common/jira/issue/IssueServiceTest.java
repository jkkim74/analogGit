package com.skplanet.bisportal.common.jira.issue;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueServiceTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void getIssue() throws IOException, ConfigurationException {
		String issueKey = "VOY-52";

		IssueService issueService = new IssueService();
		Issue issue = issueService.getIssue(issueKey);

		logger.info(issue.toPrettyJsonString());

		// attachment info
		java.util.List<Attachment> attachs = issue.getFields().getAttachment();
		for (Attachment a : attachs)
			logger.info(a.toPrettyJsonString());

		IssueFields fields = issue.getFields();
		// 프로젝트키
		String prjKey = fields.getProject().getKey();
		// 이슈 타입
		IssueType issueType = fields.getIssuetype();
		// 이슈 상세내역
		String desc = fields.getDescription();
	}

//	@Test
//	public void createIssue() throws IOException, ConfigurationException {
//
//		Issue issue = new Issue();
//
//		IssueFields fields = new IssueFields();
//
//		fields.setProjectKey("VOYOP");
//		fields.setIssueTypeId("10718");
//		fields.setSummary("JIRA client test");
//		fields.setDescription("voyager 계정으로 테스트를 하고 있습니다...)");
//
//		// fields.setComponents(Arrays.asList(new Component[]{new Component("Component-1"), new
//		// Component("Component-2")}));
//		fields.setComponents(Arrays.asList(new Component[] { new Component("Hoppin") }));
////		fields.setComponents(Arrays.asList(new Component[] { new Component("T map") }));
//
//		fields.setDuedate("2015-02-01");
//
//		fields.addAttachment("/Users/cookatrice/github/apple.txt");
//		fields.addAttachment("/Users/cookatrice/github/banana.txt");
//
//		HashMap<String, String> f_10701 = new HashMap<>();
//		f_10701.put("name", "pp39095");
//		fields.setCustomfield("customfield_10701", f_10701);
//
//		HashMap<String, String> f_11639 = new HashMap<>();
//		f_11639.put("name", "1000714");
//		fields.setCustomfield("customfield_11639", f_11639);
//
//		ArrayList<HashMap<String, String>> f_10921 = new ArrayList<HashMap<String, String>>();
//		// HashMap<String, String> tmpSet;
//		// tmpSet = new HashMap<>();
//		// tmpSet.put("name","PP39095");
//		f_10921.add(new HashMap<String, String>() {
//			{
//				put("name", "PP39095");
//			}
//		});
//		// tmpSet = new HashMap<>();
//		// tmpSet.put("name","PP44651");
//		f_10921.add(new HashMap<String, String>() {
//			{
//				put("name", "PP44651");
//			}
//		});
//		// tmpSet = new HashMap<>();
//		// tmpSet.put("name","mimul");
//		f_10921.add(new HashMap<String, String>() {
//			{
//				put("name", "mimul");
//			}
//		});
//
//		fields.setCustomfield("customfield_10921", f_10921);
//
//		issue.setFields(fields);
//		logger.info(issue.toPrettyJsonString());
//
//		IssueService issueService = new IssueService();
//
//		Issue genIssue = issueService.createIssue(issue);
//
//		// Print Generated issue
//		logger.info(genIssue.toPrettyJsonString());
//	}

	// @Test
	// public void uploadAttachments() throws IOException, ConfigurationException {
	// Issue issue = new Issue();
	//
	// issue.setKey("VOYOP-48");
	//
	// issue.addAttachment("/Users/cookatrice/github/apple.txt");
	// issue.addAttachment("/Users/cookatrice/github/banana.txt");
	//
	// IssueService issueService = new IssueService();
	// issueService.postAttachment(issue);
	// }

	// @Test
	// public void getAllIssueType() throws IOException, ConfigurationException {
	//
	// IssueService issueService = new IssueService();
	// List<IssueType> issueTypes = issueService.getAllIssueTypes();
	//
	// logger.info(issueTypes.toString());
	// }
	//
	// @Test
	// public void getAllPriorities() throws IOException, ConfigurationException {
	//
	// IssueService issueService = new IssueService();
	// List<Priority> priority = issueService.getAllPriorities();
	//
	// logger.info(priority.toString());
	// }

	// @Test
	// public void getCustomeFields() throws IOException, ConfigurationException {
	// IssueService issueService = new IssueService();
	// Issue issue = issueService.getIssue("VOYOP-42");
	//
	// logger.info(issue.getFields().getCustomfield().toString());
	// }
}

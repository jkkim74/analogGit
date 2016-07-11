package com.skplanet.bisportal.common.controller;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microstrategy.utils.StringUtils;
import com.skplanet.bisportal.common.jira.issue.Component;
import com.skplanet.bisportal.common.jira.issue.Issue;
import com.skplanet.bisportal.common.jira.issue.IssueFields;
import com.skplanet.bisportal.common.jira.issue.IssueService;
import com.skplanet.bisportal.common.model.JiraApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

//import com.atlassian.jira.rest.client.JiraRestClient;
//import com.atlassian.jira.rest.client.NullProgressMonitor;
//import com.atlassian.jira.rest.client.domain.SearchResult;
//import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

//import com.atlassian.jira.rest.client.domain.Issue;
//import com.atlassian.jira.rest.client.domain.IssueFieldId;

/**
 * Created by cookatrice on 2014. 11. 24..
 */
@Slf4j
@Controller
@RequestMapping("/jiraApi")
public class JiraApiController {
	private PropertiesConfiguration config = null;
	private final static String CONFIG_FILE = "jira-rest-client.properties";

	public JiraApiController() throws ConfigurationException {
		File f = null;
		if (System.getProperty("jira.client.property") != null)
			f = new File(System.getProperty("jira.client.property"));

		if (f != null && f.exists()) {
			config = new PropertiesConfiguration(f);
		} else {
			f = new File(new File("."), CONFIG_FILE);
			if (f.exists()) {
				config = new PropertiesConfiguration(f);
			} else {
				config = new PropertiesConfiguration(CONFIG_FILE);
			}
		}

		if (config.getString("jira.user.id") != null && config.getString("jira.user.pwd") != null) {
			log.info("JIRA ID       : {}", config.getString("jira.user.id"));
			log.info("JIRA password : {}", config.getString("jira.user.pwd"));
			log.info("JIRA URL      : {}", config.getString("jira.server.url"));
			log.info("JIRA URL      : {}", config.getString("jira.server.basic.url"));
		}
	}

	/**
	 * Get Jira list from API
	 *
	 * @param jiraApiRequest
	 */
	@RequestMapping(value = "/getJiraListFromApi", method = RequestMethod.GET)
	@ResponseBody
	public String getJiraListFromApi(JiraApiRequest jiraApiRequest) {
		log.debug("getJiraListFromApi - jiraApiRequest {}", jiraApiRequest);
		JsonArray jiraResult = new JsonArray();

		try {

			final URI jiraServerUri = URI.create(config.getString("jira.server.basic.url"));
			final com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			final com.atlassian.jira.rest.client.api.JiraRestClient restClient = factory
					.createWithBasicHttpAuthentication(jiraServerUri, config.getString("jira.user.id"), config.getString("jira.user.pwd"));

			String tmpJQL = "project = VOYOP";
			if (StringUtils.isNotEmpty(jiraApiRequest.getSummary())) {
                //\u002a is wildcard. (\u002a = *)
				tmpJQL += " AND summary~ " + jiraApiRequest.getSummary()+"\\u002a";
//				tmpJQL += " AND summary~ " + jiraApiRequest.getSummary();
			}
//			if (!NullStrCheck(jiraApiRequest.getApplicant())) {
//				tmpJQL += " AND cf[10701] = " + jiraApiRequest.getApplicant();
//			}
//			if (!NullStrCheck(jiraApiRequest.getApproval())) {
//				tmpJQL += " AND cf[11639] = " + jiraApiRequest.getApproval();
//			}
			// if(!NullStrCheck(jiraApiRequest.getStatus())){
			// tmpJQL += " AND status = " + jiraApiRequest.getStatus();
			// }
//			if (!NullSelctCheck(jiraApiRequest.getComponent())) {
//				tmpJQL += " AND component = " + jiraApiRequest.getComponent();
//			}
			if (!Boolean.parseBoolean(jiraApiRequest.getCreateDateTotal())) {
				tmpJQL += " AND created >= " + jiraApiRequest.getCreateStartDate().replace(".", "-");
				tmpJQL += " AND created <= " + jiraApiRequest.getCreateEndDate().replace(".", "-");
			}
			if (!Boolean.parseBoolean(jiraApiRequest.getDueDateTotal())) {
				tmpJQL += " AND due >= " + jiraApiRequest.getDueStartDate().replace(".", "-");
				tmpJQL += " AND due <= " + jiraApiRequest.getDueEndDate().replace(".", "-");
			}

			tmpJQL += " ORDER BY created DESC";
			log.debug("JQL : {}", tmpJQL);

//			Set<String> fields = new HashSet<String>();
//			fields.add("id");
//			fields.add("key");
//			fields.add("summary");
//			fields.add("duedate");
//			fields.add("created");
//			fields.add("customfield_10701");
//			fields.add("customfield_11639");
//			fields.add("status");
//			fields.add("components");

			com.atlassian.jira.rest.client.api.domain.SearchResult results = restClient.getSearchClient()
					.searchJql(tmpJQL, 50, 0, null).get();
			int index = 0;

			for (BasicIssue i : results.getIssues()) {
				String key = i.getKey();
				com.atlassian.jira.rest.client.api.domain.Issue issue = restClient.getIssueClient().getIssue(key)
						.claim();

				// make issuefield to json. this is custom fields
				JsonArray fieldList = new JsonArray();
				Iterable<IssueField> issueFields = issue.getFields();
				for (IssueField issueField : issueFields) {
					JsonObject tmpField = new JsonObject();
					tmpField.addProperty("id", String.valueOf(issueField.getId()));
					tmpField.addProperty("name", String.valueOf(issueField.getName()));
					tmpField.addProperty("type", String.valueOf(issueField.getType()));
					tmpField.addProperty("value", String.valueOf(issueField.getValue()));
					fieldList.add(tmpField);
				}

				// make components to json.
				JsonArray componentList = new JsonArray();
				Iterable<BasicComponent> basicComponents = issue.getComponents();
				for (BasicComponent basicComponent : basicComponents) {
					JsonObject tmpComponent = new JsonObject();
					tmpComponent.addProperty("id", String.valueOf(basicComponent.getId()));
					tmpComponent.addProperty("self", String.valueOf(basicComponent.getSelf()));
					tmpComponent.addProperty("name", String.valueOf(basicComponent.getName()));
					tmpComponent.addProperty("description", String.valueOf(basicComponent.getDescription()));
					componentList.add(tmpComponent);
				}

				JsonObject tmpObj = new JsonObject();
				tmpObj.addProperty("id", String.valueOf(issue.getId()));
				tmpObj.addProperty("key", String.valueOf(issue.getKey()));
				tmpObj.addProperty("summary", String.valueOf(issue.getSummary()));
				tmpObj.addProperty("project", String.valueOf(issue.getProject()));
//				tmpObj.addProperty("status", String.valueOf(issue.getStatus()));
				tmpObj.addProperty("status", String.valueOf(issue.getStatus().getName()));
				tmpObj.addProperty("description", String.valueOf(issue.getDescription()));
				tmpObj.addProperty("resolution", String.valueOf(issue.getResolution()));
				tmpObj.addProperty("expandos", String.valueOf(issue.getExpandos()));
				tmpObj.addProperty("comments", String.valueOf(issue.getComments()));
				tmpObj.addProperty("attachments", String.valueOf(issue.getAttachments()));
				tmpObj.add("issueFields", fieldList);
//				tmpObj.addProperty("issueType", String.valueOf(issue.getIssueType()));
				tmpObj.addProperty("issueType", String.valueOf(issue.getIssueType().getName()));
//				tmpObj.addProperty("reporter", String.valueOf(issue.getReporter()));
				tmpObj.addProperty("reporter", String.valueOf(issue.getReporter().getDisplayName()));
//				tmpObj.addProperty("assignee", String.valueOf(issue.getAssignee()));
				tmpObj.addProperty("assignee", String.valueOf(issue.getAssignee().getDisplayName()));
				tmpObj.addProperty("creationDate", String.valueOf(issue.getCreationDate()));
				tmpObj.addProperty("updateDate", String.valueOf(issue.getUpdateDate()));
				tmpObj.addProperty("dueDate", String.valueOf(issue.getDueDate()));
				tmpObj.addProperty("transitionsUri", String.valueOf(issue.getTransitionsUri()));
				tmpObj.addProperty("issueLinks", String.valueOf(issue.getIssueLinks()));
				tmpObj.addProperty("votes", String.valueOf(issue.getVotes()));
				tmpObj.addProperty("worklogs", String.valueOf(issue.getWorklogs()));
				tmpObj.addProperty("watchers", String.valueOf(issue.getWatchers()));
				tmpObj.addProperty("fixVersions", String.valueOf(issue.getFixVersions()));
				tmpObj.addProperty("affectedVersions", String.valueOf(issue.getAffectedVersions()));
				tmpObj.add("component", componentList);
				tmpObj.addProperty("priority", String.valueOf(issue.getPriority()));
				tmpObj.addProperty("timeTracking", String.valueOf(issue.getTimeTracking()));
				tmpObj.addProperty("subtasks", String.valueOf(issue.getSubtasks()));
				tmpObj.addProperty("changelog", String.valueOf(issue.getChangelog()));
				tmpObj.addProperty("labels", String.valueOf(issue.getLabels()));

				jiraResult.add(tmpObj);

				log.debug("##### INDEX : {}", (++index));
				log.debug("{}", tmpObj.toString());
			}
            restClient.close();
		} catch (RuntimeException e) {
			log.error("RuntimeException...wiseeco");
			log.error("{}", e.toString());
		} catch (Exception ee) {
			log.error("Exception...wiseeco");
			log.error("{}", ee.toString());
		}

		return jiraResult.toString();
	}

	// /**
	// * Get Jira list
	// *
	// * @param jiraApiRequest
	// */
	// @RequestMapping(value = "/getJiraList", method = RequestMethod.GET)
	// @ResponseBody
	// public SearchResult getJiraList(JiraApiRequest jiraApiRequest) {
	// log.debug("getJiraList() - jiraApiRequest {}", jiraApiRequest);
	//
	// System.out.println("01 : " + jiraApiRequest.getSummary());
	// System.out.println("02 : " + jiraApiRequest.getApplicant());
	// System.out.println("03 : " + jiraApiRequest.getApproval());
	// System.out.println("04 : " + jiraApiRequest.getStatus());
	// System.out.println("05 : " + jiraApiRequest.getComponent());
	// System.out.println("06 : " + jiraApiRequest.getCreateDateTotal());
	// System.out.println("07 : " + jiraApiRequest.getCreateStartDate());
	// System.out.println("08 : " + jiraApiRequest.getCreateEndDate());
	// System.out.println("09 : " + jiraApiRequest.getDueDateTotal());
	// System.out.println("10 : " + jiraApiRequest.getDueStartDate());
	// System.out.println("11 : " + jiraApiRequest.getDueEndDate());
	//
	// try {
	// final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
	// final URI jiraServerUri = new URI(config.getString("jira.server.basic.url"));
	// final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri,
	// config.getString("jira.user.id"), config.getString("jira.user.pwd"));
	// final NullProgressMonitor pm = new NullProgressMonitor();
	// final com.atlassian.jira.rest.client.domain.Issue issue = restClient.getIssueClient().getIssue("VOYOP-148", pm);
	//
	// System.out.println(issue);
	//
	// String tmpJQL = "project = VOYOP";
	// if (!NullStrCheck(jiraApiRequest.getSummary())) {
	// tmpJQL += " AND summary~" + jiraApiRequest.getSummary();
	// }
	// if (!NullStrCheck(jiraApiRequest.getApplicant())) {
	// tmpJQL += " AND cf[10701] = " + jiraApiRequest.getApplicant();
	// }
	// if (!NullStrCheck(jiraApiRequest.getApproval())) {
	// tmpJQL += " AND cf[11639] = " + jiraApiRequest.getApproval();
	// }
	// // if(!NullStrCheck(jiraApiRequest.getStatus())){
	// // tmpJQL += " AND status = " + jiraApiRequest.getStatus();
	// // }
	// if (!NullSelctCheck(jiraApiRequest.getComponent())) {
	// tmpJQL += " AND component = " + jiraApiRequest.getComponent();
	// }
	// if (!Boolean.parseBoolean(jiraApiRequest.getCreateDateTotal())) {
	// tmpJQL += " AND created >= " + jiraApiRequest.getCreateStartDate().replace(".", "-");
	// tmpJQL += " AND created <= " + jiraApiRequest.getCreateEndDate().replace(".", "-");
	// }
	// if (!Boolean.parseBoolean(jiraApiRequest.getDueDateTotal())) {
	// tmpJQL += " AND due >= " + jiraApiRequest.getDueStartDate().replace(".", "-");
	// tmpJQL += " AND due <= " + jiraApiRequest.getDueEndDate().replace(".", "-");
	// }
	//
	// System.out.println("JQL : " + tmpJQL);
	//
	// final SearchResult search = restClient.getSearchClient().searchJql(tmpJQL, new NullProgressMonitor());
	// // final SearchResult search = restClient.getSearchClient().searchJqlWithFullIssues(tmpJQL, 500, 0, new
	// NullProgressMonitor());
	// System.out.println(search);
	// return search;
	//
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	//
	// }

	/**
	 * upload file delete after use jira
	 */
	@RequestMapping(value = "/deleteWas", method = RequestMethod.POST)
	@ResponseBody
	public void deleteUploadFiles(@RequestBody JiraApiRequest jiraApiRequest) throws ConfigurationException,
			IOException {

		log.debug("deleteUploadFiles() - jiraApiRequest {}", jiraApiRequest);
		//log.debug("files : {}", jiraApiRequest.getAttachFiles()); // 첨부파일

		// attach files
		String[] tmpFileList = jiraApiRequest.getAttachFiles();
		int tmpFileLength = 0;
		if (tmpFileList != null) {
			tmpFileLength = tmpFileList.length;
		}
		File tmpFile;
		for (int i = 0; i < tmpFileLength; i++) {
			// tmpFile = new File("/data/helpdesk/"+jiraApiRequest.getApplicant()+"_"+tmpFileList[i]); //for server
			tmpFile = new File("/Users/cookatrice/jira_test/helpdesk/" + jiraApiRequest.getApplicant() + "_"
					+ tmpFileList[i]); // for dev
			if (!tmpFile.delete()) {
				log.debug("file {}", tmpFile.toString());
			}
		}
	}

	/**
	 * file upload to was to use jira.
	 */
	@RequestMapping(value = "/saveWas", method = RequestMethod.POST)
	@ResponseBody
	public String saveAttachFiles(MultipartHttpServletRequest request, @RequestParam(value = "userId") String userId) {
		log.debug("saveAttachFiles()");
		log.debug("request: {}", request);

		Iterator<String> itr = request.getFileNames();

		try {
			if (itr.hasNext()) {
				MultipartFile mpf = request.getFile(itr.next());
				log.debug("{}", mpf.getOriginalFilename());

				String fileName = null;
				if (!mpf.isEmpty()) {
					try {
						fileName = mpf.getOriginalFilename();
						byte[] bytes = mpf.getBytes();
						BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(
						// "/data/helpdesk/" + userId + "_" + fileName))); //for server
								"/Users/cookatrice/jira_test/helpdesk/" + userId + "_" + fileName))); // for dev
						buffStream.write(bytes);
						buffStream.close();
						return "You have successfully uploaded " + fileName;
					} catch (Exception e) {
						return "You failed to upload " + fileName + ": " + e.getMessage();
					}
				} else {
					return "Unable to upload. File is empty.";
				}
			}
		} catch (Exception e) {
			log.error("File save error", e);
		}

		return "just return";
	}

	/**
	 * JIRA create API 호출
	 *
	 * @param jiraApiRequest
	 * @return
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Issue callJiraApi(@RequestBody JiraApiRequest jiraApiRequest) throws ConfigurationException, IOException {
		log.debug("sendWorkRequestForm() - jiraApiRequest {}", jiraApiRequest);

		Issue issue = new Issue();

		IssueFields fields = new IssueFields();

		// project Key
		fields.setProjectKey("VOYOP");

		// issue type
		fields.setIssueTypeId(jiraApiRequest.getIssueType());

		// summary
		fields.setSummary(jiraApiRequest.getSummary());

		// description
		fields.setDescription(jiraApiRequest.getDescription());

		// component
		fields.setComponents(Arrays.asList(new Component[] { new Component(jiraApiRequest.getComponent()) }));
		// fields.setComponents(Arrays.asList(new Component[] { new Component("14221", "Hoppin") }));
		// fields.setComponents(Arrays.asList(new Component[]{new Component("Component-1"), new
		// Component("Component-2")}));

		// due-date
		fields.setDuedate(jiraApiRequest.getDueDate());

		// attach files
		String[] tmpFileList = jiraApiRequest.getAttachFiles();
		int tmpFileLength = 0;
		if (tmpFileList != null) {
			tmpFileLength = tmpFileList.length;
		}
		for (int i = 0; i < tmpFileLength; i++) {
			log.debug("index : {} --> {}", i, tmpFileList[i]);
			String fileName = new String(tmpFileList[i].getBytes("UTF-8"), "UTF-8");
			log.debug("index : {} --> {}", i, fileName);

			// fields.addAttachment("/data/helpdesk/"+jiraApiRequest.getApplicant()+"_"+tmpFileList[i]); //for server
			fields.addAttachment("/Users/cookatrice/jira_test/helpdesk/" + jiraApiRequest.getApplicant() + "_"
					+ tmpFileList[i]); // for dev
			// fields.addAttachment("/Users/cookatrice/jira_test/helpdesk/"+jiraApiRequest.getApplicant()+"_"+fileName);
			// //for dev (encoding)
		}

		// applicant
		HashMap<String, String> f_10701 = Maps.newHashMap();
		f_10701.put("name", jiraApiRequest.getApplicant());
		fields.setCustomfield("customfield_10701", f_10701);

		// approval
		HashMap<String, String> f_11639 = Maps.newHashMap();
		f_11639.put("name", jiraApiRequest.getApproval());
		fields.setCustomfield("customfield_11639", f_11639);

		// reference
		ArrayList<HashMap<String, String>> f_10921 = Lists.newArrayList();
		String[] tmpList = jiraApiRequest.getReference();
		int tmpLength = 0;
		if (tmpList != null) {
			tmpLength = tmpList.length;
		}
		for (int i = 0; i < tmpLength; i++) {
			log.debug("index : {} --> {}", i, tmpList[i]);
			final String nameValue = tmpList[i];
			f_10921.add(new HashMap<String, String>() {
				{
					put("name", nameValue);
				}
			});
		}
		fields.setCustomfield("customfield_10921", f_10921);
		issue.setFields(fields);
		log.info("{}", issue.toPrettyJsonString());

		IssueService issueService = new IssueService();
		Issue genIssue = issueService.createIssue(issue);
		// Print Generated issue
		log.info("{}", genIssue.toPrettyJsonString());

		return genIssue;
	}
}

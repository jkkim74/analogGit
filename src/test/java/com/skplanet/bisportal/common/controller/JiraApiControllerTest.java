package com.skplanet.bisportal.common.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JiraApiControllerTest extends TestCase {

	public void testName() throws Exception {

	}

	@Test
	public void testGetJiraListFromApi() throws Exception {

		final com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final com.atlassian.jira.rest.client.api.JiraRestClient restClient = factory.createWithBasicHttpAuthentication(
				URI.create("http://jira.skplanet.com/"), "voyager", "voyops");

		String tmpJQL = "project = VOYOP ORDER BY created DESC";
		// String tmpJQL = "project = VOYOP AND cf[10701] = 이승우 ORDER BY created DESC";
		// String tmpJQL = "project = VOYOP AND created > 2015-01-25 and created <= 2015-01-28 ORDER BY created DESC";

		System.out.println("JQL : " + tmpJQL);

		Set<String> fields = new HashSet<String>();
		fields.add("id");
		fields.add("key");
		fields.add("summary");
		fields.add("duedate");
		fields.add("created");
		// fields.add("customfield_10701");
		// fields.add("customfield_11639");
		fields.add("status");
		fields.add("components");

		// com.atlassian.jira.rest.client.api.domain.SearchResult search =
		// restClient.getSearchClient().searchJql(tmpJQL);

		// com.atlassian.util.concurrent.Promise<com.atlassian.jira.rest.client.api.domain.SearchResult> search =
		// restClient.getSearchClient().searchJql(tmpJQL, 500, 0, fields);
		// // restClient.getSearchClient().searchJql(tmpJQL);
		// com.atlassian.jira.rest.client.api.domain.SearchResult search_2 =
		// restClient.getSearchClient().searchJql(tmpJQL).claim();
		//
		// // com.atlassian.jira.rest.client.api.domain.Issue issue = restClient.getIssueClient().getIssue("VOYOP-151")
		// // .claim();
		// // System.out.println(issue);
		//
		// System.out.println("===start search=====================================================");
		// // System.out.println(search.toString());
		// System.out.println("===end search=======================================================");
		//
		// System.out.println("===start search_2===================================================");
		// // System.out.println(search_2.toString());
		// System.out.println("===end search_2=====================================================");
		// // return search;

		// com.atlassian.jira.rest.client.api.domain.SearchResult results =
		// restClient.getSearchClient().searchJql(tmpJQL).get();
		com.atlassian.jira.rest.client.api.domain.SearchResult results = restClient.getSearchClient()
				.searchJql(tmpJQL, 50, 0, null).get();
		int index = 0;
		JsonArray jiraResult = new JsonArray();
		for (BasicIssue i : results.getIssues()) {

			String key = i.getKey();
			Issue issue = restClient.getIssueClient().getIssue(key).claim();
			String curInfo = "";

			curInfo += "- Summary..... " + issue.getSummary() + "\n";
			curInfo += "- Key..... " + issue.getKey() + "\n";
			curInfo += "- Id..... " + issue.getId() + "\n";
			curInfo += "- Description..... " + issue.getDescription() + "\n";
			curInfo += "- Status..... " + issue.getStatus() + "\n";
			curInfo += "- DueDate..... " + issue.getDueDate() + "\n";
			curInfo += "- CreateDate....." + issue.getCreationDate() + "\n";
			curInfo += "- CustomFields....." + issue.getFields() + "\n";

			Iterable<IssueField> issueFields2 = issue.getFields();
			// issue.getFields().iterator().next().;

			for (IssueField issueField : issueFields2) {
				System.out.print("#####");
				IssueField field = issueField;
				if (field.getId().equals("customfield_10701")) {
					System.out.print(field.getName() + "///" + field.getValue());
				}
				System.out.print("#####");
				System.out.print("");
			}

			curInfo += "- Component....." + issue.getComponents() + "\n";
			curInfo += "- Assignee..... " + issue.getAssignee() + "\n";

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

            issue.getAssignee().getDisplayName();

			JsonObject tmpObj = new JsonObject();
			tmpObj.addProperty("id", String.valueOf(issue.getId()));
            tmpObj.addProperty("key", String.valueOf(issue.getKey()));
            tmpObj.addProperty("summary", String.valueOf(issue.getSummary()));
            tmpObj.addProperty("project", String.valueOf(issue.getProject()));
//            tmpObj.addProperty("status", String.valueOf(issue.getStatus()));
            tmpObj.addProperty("status", String.valueOf(issue.getStatus().getName()));
            tmpObj.addProperty("description", String.valueOf(issue.getDescription()));
            tmpObj.addProperty("resolution", String.valueOf(issue.getResolution()));
            tmpObj.addProperty("expandos", String.valueOf(issue.getExpandos()));
            tmpObj.addProperty("comments", String.valueOf(issue.getComments()));
            tmpObj.addProperty("attachments", String.valueOf(issue.getAttachments()));
            tmpObj.add("issueFields", fieldList);
//            tmpObj.addProperty("issueType", String.valueOf(issue.getIssueType()));
            tmpObj.addProperty("issueType", String.valueOf(issue.getIssueType().getName()));
//            tmpObj.addProperty("reporter", String.valueOf(issue.getReporter()));
            tmpObj.addProperty("reporter", String.valueOf(issue.getReporter().getDisplayName()));
//            tmpObj.addProperty("assignee", String.valueOf(issue.getAssignee()));
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

			System.out.println("INDEX : " + (++index));
			// System.out.println(curInfo);
			System.out.println(tmpObj.toString());
			// System.out.println("==============================================");
			// System.out.println(issue.toString());

			// Object a = issue.getFieldByName("AosCustom1").getValue();
			// System.out.println(key + ": " + a);
		}

		System.out.println(jiraResult.toString());

	}
}

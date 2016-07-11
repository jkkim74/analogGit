package com.skplanet.bisportal.api.jenkins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.api.jenkins.model.Base;
import com.skplanet.bisportal.api.jenkins.model.Job;
import com.skplanet.bisportal.api.jenkins.model.JobDetail;
import com.skplanet.bisportal.common.exception.RemoteAccessException;
import com.skplanet.bisportal.common.utils.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Jenkins API Helper 클래스.
 *
 * @author HO-JIN, HA(mimul@wiseeco.com)
 * @see <a href="https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API">Jenkins Remote access API<a>
 * jarvis에서 빌드후 이력이 남아있어 디스크풀 시 빌드안되는 현상이 있어 주기적으로 과거 빌드이력 삭제해주어야 함.
 */
@Slf4j
public class JenkinsHelper {
	private final ObjectMapper mapper = ObjectMapperFactory.get();
	private PreemptiveAuthenticationRestTemplate restTemplate;
	private String context;
	private URI serverUri;

	/**
	 * Jenkins 로그인 기반 JenkinsHelper 생성자
	 *
	 * @param serverUri       jenkins 서버 주(예. http://localhost:8080/)
	 * @param username        Jenkins 사용자 계정
	 * @param passwordOrToken 패스워드나 인증 토큰
	 */
	public JenkinsHelper(URI serverUri, String username, String passwordOrToken) {
		this.context = serverUri.getPath();
		if (!context.endsWith("/")) {
			context += "/";
		}
		this.serverUri = serverUri;
		restTemplate = new PreemptiveAuthenticationRestTemplate();
		HttpHost host = new HttpHost(serverUri.getHost(), 8080, "http");
		restTemplate.setCredentials(host, PreemptiveAuthenticationScheme.BASIC_AUTHENTICATION, username,
				passwordOrToken);
	}

	/**
	 * Jenkins Job 리스트 조회(List 방식을 Map 방식으로 전환해 키 방식으로 조회)
	 *
	 * @return Map 등록된 Job Collection
	 * @throws RemoteAccessException
	 */
	public Map<String, Job> getJobs() {
		List<Job> jobs;
		Map<String, Job> jobMaps;
		String content = restTemplate.getForObject(getUrl("/"), String.class);
		try {
			jobs = mapper.readValue(content, Base.class).getJobs();
			jobMaps = Maps.uniqueIndex(jobs, new Function<Job, String>() {
				@Override public String apply(Job job) {
					return job.getName().toLowerCase();
				}
			});
		} catch (IOException e) {
			log.error("getJobs {}", e.getMessage());
			throw new RemoteAccessException("io exception generated.", e);
		}

		return jobMaps;
	}

	/**
	 * Jenkins에 등록된 특정 이름으로 된 Job 상세 정보 조회
	 *
	 * @param jobName 잡이름
	 * @return JobDetails Job 상세 정보
	 * @throws RemoteAccessException
	 */
	public JobDetail getJobByName(String jobName) {
		JobDetail jobDetail;
		try {
			String content = restTemplate.getForObject(getUrl("/job/" + jobName), String.class);
			jobDetail = mapper.readValue(content, JobDetail.class);
		} catch (IOException e) {
			log.error("getJobByName {}", e.getMessage());
			throw new RemoteAccessException("io exception generated.", e);
		}
		return jobDetail;
	}

	/**
	 * 특정 Job의 특정 Build 이력에 대한 상세 정보 조회
	 *
	 * @param jobName 잡이름
	 * @param buildNumber  빌드 번호
	 * @return BuildDetails 빌드 상세 정보
	 * @throws RemoteAccessException
	 */
//	public BuildDetail getBuildByName(String jobName, int buildNumber) {
//		BuildDetail buildDetail;
//		try {
//			String content = restTemplate.getForObject(getUrl("/job/" + jobName + "/" + buildNumber), String.class);
//			buildDetail = mapper.readValue(content, BuildDetail.class);
//		} catch (IOException e) {
//			log.error("getBuildByName {}", e.getMessage());
//			throw new RemoteAccessException("io exception generated.", e);
//		}
//
//		return buildDetail;
//	}

	/**
	 * 빌드 이력 삭제
	 *
	 * @param jobName 잡 이름
	 * @param buildNumber 빌드 번호
	 * @return HTTP Status
	 */
	public int removeBuildHistory(String jobName, int buildNumber) {
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(getUrl("/job/" + jobName + "/" + buildNumber + "/doDelete"),
				getHttpEntity(MediaType.APPLICATION_FORM_URLENCODED), String.class);
		return responseEntity != null ? responseEntity.getStatusCode().value() : 500;
	}

//	public String lastSuccessfulBuild(String jobName) {
//		return restTemplate.getForObject(getUrl("/job/" + jobName + "/lastSuccessfulBuild"), String.class);
//	}

//	public String lastBuild(String jobName) {
//		return restTemplate.getForObject(getUrl("/job/" + jobName + "/lastBuild"), String.class);
//	}

//	public BuildDetail lastStableBuild(String jobName) {
//		BuildDetail lastStableBuild;
//		try {
//			String content = restTemplate.getForObject(getUrl("/job/" + jobName + "/lastStableBuild"), String.class);
//			lastStableBuild = mapper.readValue(content, BuildDetail.class);
//		} catch (IOException e) {
//			log.error("lastStableBuild {}", e.getMessage());
//			throw new RemoteAccessException("io exception generated.", e);
//		}
//
//		return lastStableBuild;
//	}

//	public BuildDetail findLastBuildDetails(String jobName) {
//		BuildDetail lastBuildDetail;
//		try {
//			String content = restTemplate.getForObject(getUrl("/job/" + jobName + "/lastBuild"), String.class);
//			lastBuildDetail = mapper.readValue(content, BuildDetail.class);
//		} catch (IOException e) {
//			log.error("findLastBuildDetails {}", e.getMessage());
//			throw new RemoteAccessException("io exception generated.", e);
//		}
//		return lastBuildDetail;
//	}

	private String urlJoin(String path1, String path2) {
		if (!path1.endsWith("/")) {
			path1 += "/";
		}
		if (path2.startsWith("/")) {
			path2 = path2.substring(1);
		}
		return path1 + path2;
	}

	private URI getUrl(String path) {
		if (!path.toLowerCase().matches("https?://.*")) {
			path = urlJoin(this.context, path);
		}
		if (!path.contains("?"))
			path = urlJoin(path, "api/json");
		else {
			String[] components = path.split("\\?", 2);
			path = urlJoin(components[0], "api/json") + "?" + components[1];
		}
		return serverUri.resolve("/").resolve(path);
	}

	private HttpEntity<?> getHttpEntity(MediaType mediaType){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		return new HttpEntity<Object>(headers);
	}

	public static void main(String[] args) {
		try {
			JenkinsHelper jenkins = new JenkinsHelper(new URI("http://172.19.103.102:8080"), "mimul", "Jhh1027!@#");
			JobDetail jobDetail = jenkins.getJobByName("Voyager_Portal");
			log.info("jobDetail {}", jobDetail);
			jenkins.removeBuildHistory("Voyager_Portal", jobDetail.getFirstBuild().getNumber());
		} catch (Exception e) {
			log.error("jenkins call error {}", e);
		}
	}
}

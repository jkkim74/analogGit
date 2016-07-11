package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.FileUtil;
import com.skplanet.bisportal.common.utils.PropertiesUtil;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.bip.FileMeta;
import com.skplanet.bisportal.repository.bip.FileMetaRepository;
import com.skplanet.bisportal.service.bip.FileMetaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by seoseungho on 2014. 9. 23..
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {
	public static final String RESULT_CODE_SUCCESS = "0000";
	public static final String RESULT_CODE_INTERNAL_ERROR = "1000";
	public static final String RESULT_CODE_FILE_NOT_EXIST = "2000";

	@Autowired
	private FileMetaRepository fileMetaRepository;

	@Autowired
	private FileMetaService fileMetaServiceImpl;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadFile(MultipartHttpServletRequest request,
			@CookieValue(Constants.COOKIE_VOYAGER_MASTER) String cookieValue,
			@RequestParam(value = "containerName", defaultValue = "container") String containerName,
			@RequestParam(value = "contentId") long contentId,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "deleteFileMetaId", required = false) Long deleteFileMetaId) {
		BipUser bipUser = CookieUtils.getBipUserFromEncryptedString(cookieValue);
		Map<String, String> resultMap = Maps.newHashMap();
		String uuid = UUID.randomUUID().toString();
		Iterator<String> itr = request.getFileNames();

		try {
			if (itr.hasNext()) {
				MultipartFile mpf = request.getFile(itr.next());
				// fileMeta 테이블에 저장하기 위해 객체 생성
				FileMeta fileMeta = new FileMeta();
				fileMeta.setContainerName(containerName);
				fileMeta.setContentId(contentId);
				fileMeta.setUuid(uuid);
				fileMeta.setCategory(category);
				fileMeta.setFileName(mpf.getOriginalFilename());
				fileMeta.setFileSize(mpf.getBytes().length);
				fileMeta.setCreateId(bipUser.getUsername());

				Map<String, Object> uploadResultMap = FileUtil.uploadObject(containerName, uuid, mpf);
				if (MapUtils.isEmpty(uploadResultMap)) {
					resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
					return resultMap;
				}
				// 파일 업로드가 성공한 경우, file_meta에 정보를 남긴다.
				if (uploadResultMap.get(Constants.STATUS).equals("201")) {
					int fileMetaResult = fileMetaRepository.addFileMeta(fileMeta);
					if (fileMetaResult > 0) {
						resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
						resultMap.put("uuid", uuid);
						resultMap.put("fileMetaId", String.valueOf(fileMeta.getId()));
					} else {
						// fileMeta 테이블 저장이 실패하면 해당 요청은 실패로 처리한다.
						resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
					}
				} else if (uploadResultMap.get(Constants.STATUS).equals("404")) {
					FileUtil.createContainer(containerName);
					uploadResultMap = FileUtil.uploadObject(containerName, uuid, mpf);
					if (uploadResultMap.get(Constants.STATUS).equals("201")) {
						int fileMetaResult = fileMetaRepository.addFileMeta(fileMeta);
						if (fileMetaResult > 0) {
							resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
							resultMap.put("uuid", uuid);
							resultMap.put("fileMetaId", String.valueOf(fileMeta.getId()));
						} else {
							// fileMeta 테이블 저장이 실패하면 해당 요청은 실패로 처리한다.
							resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
						}
					}
				} else {
					resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
				}
			} else {
				resultMap.put(Constants.CODE, RESULT_CODE_FILE_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("File Upload error", e);
			resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		}
		return resultMap;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> downloadFile(
			@RequestParam(value = "containerName", defaultValue = "container") String containerName,
			@RequestParam(value = "uuid") String uuid, @RequestParam(value = "saveFileName") String saveFileName,
			HttpServletResponse res, HttpServletRequest req, @RequestHeader(value = "User-Agent") String userAgent)
					throws Exception {
		Map<String, Object> resultMap = Maps.newHashMap();
		String encoding = PropertiesUtil.getProperty("planet.space.encoding");
		String baseUrl = PropertiesUtil.getProperty("planet.space.baseurl");
		String apiVersion = PropertiesUtil.getProperty("planet.space.api.version");
		String serviceName = PropertiesUtil.getProperty("planet.space.service.name");

		String dateString = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
				.format(new Date(System.currentTimeMillis()));
		String userName = PropertiesUtil.getProperty("planet.space.user.name");
		String secretKey = PropertiesUtil.getProperty("planet.space.user.secretkey");

		URL url = new URL(baseUrl + "/" + apiVersion + "/" + URLEncoder.encode(serviceName, encoding) + "/"
				+ URLEncoder.encode(userName, encoding) + "/" + URLEncoder.encode(containerName, encoding) + "/"
				+ URLEncoder.encode(uuid, encoding).replaceAll("%2F", "/"));

		String method = "GET";
		String uri = URLDecoder.decode(url.getPath(), encoding);
		String signature = FileUtil.makeSignature(method, uri, dateString, userName, secretKey);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpGet request = new HttpGet(url.toString());
			// Request Headers
			request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			request.setHeader("Date", dateString);
			request.setHeader("X-PS-User", userName);
			request.setHeader("X-PS-Signature", signature);

			// execute
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case 200:
			case 206:
				if (response.getStatusLine().getStatusCode() == 200
						|| response.getStatusLine().getStatusCode() == 206) {
					for (Header header : response.getAllHeaders()) {
						switch (header.getName()) {
						case "Content-Length":
							res.setContentLength(Integer.parseInt(header.getValue()));
							break;
						case "Content-Type":
							res.setContentType(header.getValue());
							break;
						default:
							break;
						}
					}

					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = getDisposition(saveFileName, userAgent);
					res.setHeader(headerKey, headerValue);
				}

				if (!FileUtil.isLocal(req.getRequestURL().toString())) {
					// 문서보안 적용 ------ start
					saveFileName = FileUtil.renameAddSuffix(saveFileName);
					FileUtil.planetSpaceToServer(response, saveFileName);
					saveFileName = FileUtil.encoding(saveFileName);
					FileUtil.download(res, saveFileName);
					// 문서보안 적용 ------ end
				} else {
					// 문서보안 미적용 ------ start
					// Response body : download file
					HttpEntity httpEntity = response.getEntity();
					httpEntity.writeTo((res.getOutputStream()));
					// 문서보안 미적용 ------ start
				}
				break;

			default:
				String responseBody = (response.getEntity() != null) ? EntityUtils.toString(response.getEntity())
						: null;
				if (responseBody != null) {
					Type stringMap = new TypeToken<Map<String, String>>() {
					}.getType();
					Map<String, String> map = new Gson().fromJson(responseBody, stringMap);
					resultMap.put(Constants.MESSAGE, map.get(Constants.MESSAGE));
				}
				break;
			}

			return resultMap;
		} catch (Exception e) {
			log.error("File Download Error {}", e);
			return null;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	/**
	 * 게시물에 해당하는 파일 목록 가지고 오기
	 */
	@RequestMapping(value = "/getMetaFileLists", method = RequestMethod.GET)
	@ResponseBody
	public List<FileMeta> getMetaFileLists(
			@RequestParam(value = "containerName", defaultValue = "container") String containerName,
			@RequestParam(value = "contentId") long contentId) {
		FileMeta params = new FileMeta();
		params.setContainerName(containerName);
		params.setContentId(contentId);
		return fileMetaServiceImpl.getFileMetaLists(params);
	}

	/**
	 * 파일삭제 - (FILE_META table, DELETE_YN = 'Y' update)
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public int deleteFile(@RequestBody List<Long> fileMetaIdList, @CookieValue(Constants.COOKIE_VOYAGER_MASTER) String cookieValue) {
		BipUser deleteUser = CookieUtils.getBipUserFromEncryptedString(cookieValue);
		return fileMetaServiceImpl.deleteFile(fileMetaIdList, deleteUser.getUsername());
	}

	private String getBrowser(String userAgent) {
		if (userAgent.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (userAgent.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (userAgent.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	private String getDisposition(String filename, String userAgent) throws Exception {
		String browser = getBrowser(userAgent);
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, CharEncoding.UTF_8).replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes(CharEncoding.UTF_8), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes(CharEncoding.UTF_8), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			int filenameLength = filename.length();
			for (int i = 0; i < filenameLength; i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode(StringUtils.EMPTY + c, CharEncoding.UTF_8));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			throw new RuntimeException("Not supported browser");
		}
		return dispositionPrefix + encodedFilename;
	}
}

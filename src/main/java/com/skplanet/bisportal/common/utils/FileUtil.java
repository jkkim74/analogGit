package com.skplanet.bisportal.common.utils;

import SCSL.SLDsFile;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skplanet.bisportal.common.consts.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by seoseungho on 2014. 9. 22..
 */
@Slf4j
public class FileUtil {
    //public static final DateFormat DATE_HEADER_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String PLANET_SPACE_ENCODING = PropertiesUtil.getProperty("planet.space.encoding");
    private static final String PLANET_SPACE_BASEURL = PropertiesUtil.getProperty("planet.space.baseurl");
    private static final String PLANET_SPACE_API_VER = PropertiesUtil.getProperty("planet.space.api.version");
    private static final String PLANET_SPACE_SERVICE_NM = PropertiesUtil.getProperty("planet.space.service.name");
    private static final String PLANET_SPACE_USER_NM = PropertiesUtil.getProperty("planet.space.user.name");
    private static final String PLANET_SPACE_SECRETKEY = PropertiesUtil.getProperty("planet.space.user.secretkey");
    private static final int PLANET_SPACE_SUCCESS_CODE = 201;

    private static final String SAVE_FILE_PATH = "/data/security/";
    private static final String SAVE_FILE_SUFFIX = "_original";

    public static Map<String, Object> uploadObject(String containerName, String objectName, MultipartFile mpf) throws SignatureException, IOException {
        CloseableHttpClient httpClient = null;
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            URL url = new URL(PLANET_SPACE_BASEURL
                    + "/" + PLANET_SPACE_API_VER
                    + "/" + URLEncoder.encode(PLANET_SPACE_SERVICE_NM, PLANET_SPACE_ENCODING) + "/"  + URLEncoder.encode(PLANET_SPACE_USER_NM, PLANET_SPACE_ENCODING)
                    + "/" + URLEncoder.encode(containerName, PLANET_SPACE_ENCODING)
                    + "/" + URLEncoder.encode(objectName, PLANET_SPACE_ENCODING).replaceAll("%2F", "/"));
            String uri = URLDecoder.decode(url.getPath(), PLANET_SPACE_ENCODING);
            String dateStr = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(
                    new Date(System.currentTimeMillis()));
            String signature = makeSignature("PUT", uri, dateStr, PLANET_SPACE_USER_NM, PLANET_SPACE_SECRETKEY);
            httpClient = HttpClientBuilder.create().build();
            File uploadFile = convert(mpf);
            if (uploadFile == null) {
                return returnMap;
            }
            HttpPut request = new HttpPut(url.toString());
            // Request Headers
            request.setHeader("Date", dateStr);
            request.setHeader("X-PS-User", PLANET_SPACE_USER_NM);
            request.setHeader("X-PS-Signature", signature);
            String contentType = mpf.getContentType();
            if (StringUtils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
            request.setHeader("Content-Type", contentType);
            FileEntity entity = new FileEntity(uploadFile);
            request.setEntity(entity);

            // execute
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            returnMap.put(Constants.STATUS, String.valueOf(response.getStatusLine().getStatusCode()));
            String responseBody = (response.getEntity() != null) ? EntityUtils.toString(response.getEntity()) : null;
            switch (statusCode) {
            case PLANET_SPACE_SUCCESS_CODE:
                if (responseBody != null) {
                    Map<String, String> objectInfoMap = Maps.newHashMap();
                    objectInfoMap.put(Constants.STATUS, String.valueOf(statusCode));
                    objectInfoMap.put("bytes", response.getHeaders("Content-Length")[0].getValue());
                    objectInfoMap.put("content_type", response.getHeaders("Content-Type")[0].getValue());
                    objectInfoMap.put("hash", response.getHeaders("ETag")[0].getValue());
                    returnMap.put("object", objectInfoMap);
                }
                break;

            default:
                if (responseBody != null) {
                    Type stringMap = new TypeToken<Map<String, String>>() {
                    }.getType();
                    returnMap = new Gson().fromJson(responseBody, stringMap);
                }
                break;
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
        return returnMap;
    }

    public static Map<String, Object> createContainer(String containerName) throws SignatureException, IOException {
        CloseableHttpClient httpClient = null;
        Map<String, Object> returnMap = Maps.newHashMap();

        try {
            URL url = new URL(PLANET_SPACE_BASEURL
                    + "/" + PLANET_SPACE_API_VER + "/" + URLEncoder.encode(PLANET_SPACE_SERVICE_NM, PLANET_SPACE_ENCODING)
                    + "/"  + URLEncoder.encode(PLANET_SPACE_USER_NM, PLANET_SPACE_ENCODING)
                    + "/" + URLEncoder.encode(containerName, PLANET_SPACE_ENCODING));
            String uri = URLDecoder.decode(url.getPath(), PLANET_SPACE_ENCODING);
            String dateStr = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(
                    new Date(System.currentTimeMillis()));
            String signature = makeSignature("PUT", uri, dateStr, PLANET_SPACE_USER_NM, PLANET_SPACE_SECRETKEY);
            httpClient = HttpClientBuilder.create().build();
            HttpPut request = new HttpPut(url.toString());
            // Request Headers
            request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.setHeader("Date", dateStr);
            request.setHeader("X-PS-User", PLANET_SPACE_USER_NM);
            request.setHeader("X-PS-Signature", signature);

            // execute
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            returnMap.put(Constants.STATUS, String.valueOf(response.getStatusLine().getStatusCode()));
            String responseBody = (response.getEntity() != null) ? EntityUtils.toString(response.getEntity()) : null;
            switch (statusCode) {
            case PLANET_SPACE_SUCCESS_CODE:
                break;
            default:
                if (responseBody != null) {
                    Type stringMap = new TypeToken<Map<String, String>>() {
                    }.getType();
                    returnMap = new Gson().fromJson(responseBody, stringMap);
                }
                break;
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
        return returnMap;
    }

    /**
     * MultiPart File 을 File 로 변환
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static File convert(MultipartFile file) throws IOException {
        File convFile = null;
        FileOutputStream fos = null;
        try {
            convFile = new File(file.getOriginalFilename());
            if (!convFile.createNewFile()) {
                log.error("convFile ERROR {}", convFile.toString());
                return null;
            }
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.flush();
        } finally {
            try {
               if (fos != null)
                   fos.close();
            } catch (IOException e) {
                log.error("convert {}", e);
            }
        }
        return convFile;
    }

    private static String calculateRFC2104HMAC(String strToSign, String key) throws SignatureException {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(CharEncoding.UTF_8), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(strToSign.getBytes(CharEncoding.UTF_8));

            // base64-encode the hmac
            result = Base64.encodeBase64String(rawHmac);
            // result = result.replace("\r", "").replace("\n", "");
        } catch (Exception e) {
            log.error("calculateRFC2104HMAC {}", e);
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    public static String makeSignature(String method, String uri, String date, String user, String secretKey) throws UnsupportedEncodingException,
            SignatureException {
        StringBuffer buf = new StringBuffer();
        buf.append(method + "\n");
        buf.append(uri + "\n");
        buf.append(date + "\n");
        buf.append(user + "\n");
        String strToSign = buf.toString();
        String signature = calculateRFC2104HMAC(URLEncoder.encode(strToSign, CharEncoding.UTF_8), secretKey);
        return signature;
    }

    /**
     * 기존 파일에 특정 접미사를 포함하도록 이름을 변경
     * @param saveFileName
     * @return
     * @throws Exception
     */
	public static String renameAddSuffix(String saveFileName) throws Exception {
		String body;
		String ext;
		int dot = saveFileName.lastIndexOf(".");
		if (dot != -1) {
			body = saveFileName.substring(0, dot);
			ext = saveFileName.substring(dot); // includes "."
		} else {
			body = saveFileName;
			ext = StringUtils.EMPTY;
		}
		return body + SAVE_FILE_SUFFIX + ext;
	}

    /**
     * planet.space에 있는 파일을 서버 storage로 다운로드
     * @param response
     * @param saveFileName
     * @throws IOException
     */
    public static void planetSpaceToServer(HttpResponse response, String saveFileName) throws IOException {
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = response.getEntity().getContent();
            File directory = new File(SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator);
            if (!directory.isDirectory()) {
                if (!directory.mkdirs()) {
                    log.error("mkdir error {}", directory.toString());
                }
            }
            File outputFile = new File(SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator + saveFileName);
            outStream = new FileOutputStream(outputFile);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = inStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }
            outStream.flush();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                log.error("planetSpaceToServer {}", e);
            }
        }
    }

    /**
     * pivot 데이터를 excel로 변환하여 서버 storage로 다운로드
     * @param workbook
     * @param saveFileName
     * @throws IOException
     */
    public static void makeExcelToServer(Workbook workbook, String saveFileName) throws IOException {
        FileOutputStream fos = null;
        try {
            File directory = new File(SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator);
            if (!directory.isDirectory()) {
                if (!directory.mkdirs()) {
                    log.error("makeExcelToServer mkdir error {}", directory.toString());
                }
            }

            fos = new FileOutputStream(SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator + saveFileName);
            workbook.write(fos);
            fos.flush();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("makeExcelToServer {}", e);
            }
        }
    }

    /**
     * softcamp의 API를 이용하여 파일을 암호화
     * @param saveFileName
     * @return
     */
    public static String encoding(String saveFileName) {
        SLDsFile sFile = new SLDsFile();
        sFile.SettingPathForProperty(Constants.SCSL_PROPERTY);
        sFile.SLDsInitDAC();
        sFile.SLDsAddUserDAC("SECURITYDOMAIN", "111001100", 0, 0, 0);
        sFile.SLDsEncFileDACV2(Constants.SCSL_ENC, "system",
                (SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator + saveFileName),
                (SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator+ saveFileName.replaceAll(SAVE_FILE_SUFFIX, "")), 1);
        return saveFileName.replaceAll(SAVE_FILE_SUFFIX, StringUtils.EMPTY);
    }

    /**
     * 서버 storage에 있는 파일을 client로 다운로드
     * @param response
     * @param saveFileName
     * @throws Exception
     */
    public static void download(HttpServletResponse response, String saveFileName) throws Exception {
        FileInputStream inStream = null;
        OutputStream outStream = null;
        try {
            File downloadFile = new File(SAVE_FILE_PATH + DateUtil.getCurrentYmd() + File.separator + saveFileName);
            inStream = new FileInputStream(downloadFile);
            response.setContentType("application/octet-stream");
            response.setContentLength((int) downloadFile.length());
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=\"%s\"", URLEncoder.encode(downloadFile.getName(), CharEncoding.UTF_8)));
            outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                log.error("download {}", e);
            }
        }
    }

    /**
     * 요청 URL이 로컬인지 여부 체크
     * @param inUrl
     * @return boolean
     * @throws Exception
     */
    public static boolean isLocal(String inUrl) {
        return inUrl.toLowerCase().indexOf("localhost") != -1 ? true : false;
    }
}




package com.skplanet.bisportal.controller.mstr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microstrategy.web.objects.WebObjectsException;
import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.model.bip.ComRoleUser;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import com.skplanet.bisportal.model.mstr.MstrResponse;
import com.skplanet.bisportal.service.mstr.MstrService;

/**
 * The MstrController class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
@Controller
@RequestMapping("/mstr")
public class MstrController extends ReportController {
	@Autowired
	private MstrService mstrServiceImpl;

	/**
	 * MSTR 세션 체크
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getCheckSession", method = RequestMethod.GET)
	@ResponseBody
	public MstrResponse getCheckSession(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) {
		MstrResponse mstrResponse = null;
		try {
			mstrResponse = mstrServiceImpl.getCheckSession(request, response, mstrRequest);
		} catch (WebObjectsException e) {
			log.error("WebObjectsException {}", e);
		}
		return mstrResponse;
	}

	/**
	 * MSTR 프롬프트 정보 생성
	 *
	 * @param request
	 * @param response
	 * @param mstrRequest
	 * @return
	 */
	@RequestMapping(value = "/getPrompts", method = RequestMethod.GET)
	@ResponseBody
	public MstrResponse getPrompts(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) {
		MstrResponse mstrResponse = null;
		try {
			mstrResponse = mstrServiceImpl.getPrompts(request, response, mstrRequest);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return mstrResponse;
	}

	/**
	 * MSTR 권한 정보
	 *
	 * @param comRoleUser
	 * @return
	 */
	@RequestMapping(value = "/getMstrRole", method = RequestMethod.GET)
	@ResponseBody
	public ComRoleUser getMstrRole(ComRoleUser comRoleUser) {
		ComRoleUser comRoleUserResponse = null;
		try {
			comRoleUserResponse = mstrServiceImpl.getMstrRole(comRoleUser);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return comRoleUserResponse;
	}

	/**
	 * MSTR 리포트 날짜 조건 정보
	 *
	 * @param request
	 * @param response
	 * @param mstrRequest
	 * @return
	 */
	@RequestMapping(value = "/getDateTypes", method = RequestMethod.GET)
	@ResponseBody
	public MstrResponse getDateTypes(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) {
		MstrResponse mstrResponse = null;
		try {
			mstrResponse = mstrServiceImpl.getDateTypes(request, response, mstrRequest);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return mstrResponse;
	}

	/**
	 * 기준일에 대한 주차 정보
	 *
	 * @param mstrRequest
	 * @return
	 */
	@RequestMapping(value = "/getWeeks", method = RequestMethod.GET)
	@ResponseBody
	public MstrResponse getWeeks(MstrRequest mstrRequest) {
		MstrResponse mstrResponse = null;
		try {
			mstrResponse = mstrServiceImpl.getWeeks(mstrRequest);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return mstrResponse;
	}
}

package com.skplanet.bisportal.controller.bip;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.bip.Dss;
import com.skplanet.bisportal.model.bip.DssRequest;
import com.skplanet.bisportal.model.bip.DssValidator;
import com.skplanet.bisportal.model.bip.TreeMenu;
import com.skplanet.bisportal.service.bip.DssService;

/**
 * Created by seoseungho on 2014. 9. 15..
 */
@Controller
@RequestMapping("/dss")
public class DssController {
	@Autowired
	private DssService dssService;

	/**
	 * BM 리스트 조회
	 */
	@RequestMapping(value = "/bmList", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeMenu> getBmList() {
		return dssService.getBmList();
	}

	/**
	 * 보고서 목록 조회
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Dss> getDssList(@ModelAttribute DssRequest dssRequest) {
		if (StringUtils.isNotEmpty(dssRequest.getBms())) {
			dssRequest.setBmList(Arrays.asList(dssRequest.getBms().split("\\s*,\\s*")));
		}
		return dssService.getDssList(dssRequest);
	}

	/**
	 * 보고서 상세 조회
	 */
	@RequestMapping(value = "/{dssId}", method = RequestMethod.GET)
	public ResponseEntity<Dss> getDss(@PathVariable Long dssId) {
		Dss dss = dssService.getDss(dssId.longValue());
		if (dss == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(dss, HttpStatus.OK);
	}

	/**
	 * voyager main index page 노출용 최신 dss 조회
	 */
	@RequestMapping(value = "/getLatestDss", method = RequestMethod.GET)
	@ResponseBody
	public Dss getLatestDss() {
		return dssService.getLatestDss();
	}

	/**
	 * 보고서 삭제
	 */
	@RequestMapping(value = "/{dssId}", method = RequestMethod.DELETE)
	public ResponseEntity<Dss> deleteDss(BipUser deleteUser, @PathVariable long dssId) {
		Dss dss = dssService.getDss(dssId);
		if (dss != null) {
			// 권한 체크
			if (dss.getCreateUser().getUsername().equals(deleteUser.getUsername())) {
				int returnValue = dssService.deleteDss(dssId, deleteUser.getUsername());
				if (returnValue > 0) {
					return new ResponseEntity<>(dss, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(dss, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<>(dss, HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * 보고서 등록
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Dss> addDss(@RequestBody Dss dss, BipUser addUser, BindingResult result,
			UriComponentsBuilder builder) {
		new DssValidator().validate(dss, result);
		// validation 에러시 에러 처리한다.
		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		dss.setCreateId(addUser.getUsername());
		// dss 를 저장한다.
		if (dssService.addDss(dss) <= 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/dss/{id}").buildAndExpand(dss.getId()).toUri());
		return new ResponseEntity<>(dss, headers, HttpStatus.CREATED);
	}

	/**
	 * 보고서 수정
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Dss> updateDss(@RequestBody Dss dss, BipUser updateUser, BindingResult result,
			UriComponentsBuilder builder) {
		new DssValidator().validate(dss, result);

		// validation 에러시 에러 처리한다.
		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		dss.setUpdateId(updateUser.getUsername());
		// dss 를 저장한다.
		if (dssService.updateDss(dss) <= 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/dss/{id}").buildAndExpand(dss.getId()).toUri());
		return new ResponseEntity<>(dss, headers, HttpStatus.CREATED);
	}
}

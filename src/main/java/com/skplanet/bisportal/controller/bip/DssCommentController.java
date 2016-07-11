package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.bip.DssComment;
import com.skplanet.bisportal.repository.bip.DssCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seoseungho on 2014. 10. 2..
 */
@Controller
@RequestMapping(value = "/dssComment")
public class DssCommentController {
	public static final String RESULT_CODE_SUCCESS = "0000";
	public static final String RESULT_CODE_INTERNAL_ERROR = "1000";

	@Autowired
	private DssCommentRepository dssCommentRepository;

	/**
	 * 보고서에 댓글을 등록한다.
	 *
	 * @param dssComment
	 *            dssComment 객체
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addDssComment(BipUser addUser, @RequestBody DssComment dssComment) {
		Map<String, Object> resultMap = Maps.newHashMap();
		dssComment.setCreateId(addUser.getUsername());
		int addDssCommentResult = dssCommentRepository.addDssComment(dssComment);
		if (addDssCommentResult > 0) {
			resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
			resultMap.put(Constants.MESSAGE, Constants.SUCCESS);
		} else {
			resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
			resultMap.put(Constants.MESSAGE, "internal error to add DssComment");
		}
		return resultMap;
	}

	/**
	 * 보고서에 달린 댓글을 조회한다.
	 * 
	 * @param dssId
	 *            보고서 ID
	 * @return
	 */
	@RequestMapping(value = "/{dssId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addDssComment(@PathVariable long dssId) {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
		resultMap.put(Constants.MESSAGE, Constants.SUCCESS);
		resultMap.put("data", dssCommentRepository.getDssComments(dssId));
		return resultMap;
	}

	/**
	 * 리포트 댓글 삭제
	 */
	@RequestMapping(value = "/{dssCommentId}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> deleteDss(BipUser deleteUser, @PathVariable long dssCommentId) {
		Map<String, Object> resultMap = Maps.newHashMap();
		int returnValue = dssCommentRepository.deleteDssComment(dssCommentId, deleteUser.getUsername());
		if (returnValue > 0) {
			resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
			resultMap.put(Constants.MESSAGE, Constants.SUCCESS);
		} else {
			// returnValue 가 0보다 크지 않으면 어떤 이유로 작업이 실패한 것이다
			resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
			resultMap.put(Constants.MESSAGE, "delete error for DssComment(" + dssCommentId + ")");
		}
		return resultMap;
	}

	/**
	 * 리포트 수정
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateDssComment(BipUser updateUser, @RequestBody DssComment dssComment) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dssComment.setUpdateId(updateUser.getUsername());
		if (dssCommentRepository.updateDssComment(dssComment) > 0) {
			resultMap.put(Constants.CODE, RESULT_CODE_SUCCESS);
			resultMap.put(Constants.MESSAGE, Constants.SUCCESS);
		} else {
			resultMap.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
			resultMap.put(Constants.MESSAGE, "fail update DssComment( " + dssComment.getId() + ")");
		}
		return resultMap;
	}
}

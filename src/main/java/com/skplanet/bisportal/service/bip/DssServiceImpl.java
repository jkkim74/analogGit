package com.skplanet.bisportal.service.bip;

import com.google.gson.Gson;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.acl.MenuRepository;
import com.skplanet.bisportal.repository.bip.DssBmListRepository;
import com.skplanet.bisportal.repository.bip.DssRepository;
import com.skplanet.bisportal.repository.bip.FileMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by seoseungho on 2014. 9. 15..
 */
@Service
public class DssServiceImpl implements DssService {
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private DssRepository dssRepository;
	@Autowired
	private FileMetaRepository fileMetaRepository;
	@Autowired
	private DssBmListRepository dssBmListRepository;
	@Autowired
	private FileMetaService fileMetaService;

	/**
	 * BM 리스트를 가져온다.
	 * 
	 * @return
	 */
	@Override
	public List<TreeMenu> getBmList() {
		Menu menuParam = new Menu();
		menuParam.setCommonCodeName(CommonCode.MenuCommonCodeName.BM.name());
		menuParam.setParentId(74l);
		return menuRepository.getTreeMenus(menuParam);
	}

	/**
	 * dssID를 가진 보고서 정보를 가져온다. 보고서 이전, 이후 보고서도 같이 불러온다.
	 * 
	 * @param dssId
	 * @return
	 */
	@Override
	@Transactional
	public Dss getDss(long dssId) {
		Dss dss = dssRepository.getDss(dssId);
		if (dss == null) {
			return dss;
		}

		// 이전 보고서, 다음 보고서를 조회
		dss.setPreviousDss(dssRepository.getPreviousDss(dssId));
		dss.setNextDss(dssRepository.getNextDss(dssId));

		// file 정보를 받아온다.
		FileMeta fileMetaRequest = new FileMeta();
		fileMetaRequest.setContainerName(Constants.PLANET_SPACE_DSS_CONTAINER_NAME);
		fileMetaRequest.setContentId(dss.getId());
		List<FileMeta> flieMetaList = fileMetaService.getFileMetaLists(fileMetaRequest);
		dss.setFileMetaList(flieMetaList);

		// 조회수 1 증가한다.
		dssRepository.increaseViewCount(dssId);
		return dss;
	}

	/**
	 * voyager main index page 노출용 최신 dss를 불러온다.
	 *
	 * @return
	 */
	@Override
	public Dss getLatestDss() {
		return dssRepository.getLatestDss();
	}

	/**
	 * 보고서를 등록한다.
	 * 
	 * @param dss
	 * @return
	 */
	@Override
	@Transactional
	public int addDss(Dss dss) {
		// ISOData -> YYYYMMDD 로 변환 (날짜만 필요한 정보)
		dss.setAnalysisStartYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getAnalysisStart(),
				Constants.DATE_YMD_FORMAT));
		dss.setAnalysisEndYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getAnalysisEnd(), Constants.DATE_YMD_FORMAT));
		dss.setDataStartYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getDataStart(), Constants.DATE_YMD_FORMAT));
		dss.setDataEndYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getDataEnd(), Constants.DATE_YMD_FORMAT));

		// variables 값을 json으로 저장. 복수개의 variable 값이 들어올 수 있다.)
		if (dss.getVariables() != null) {
			dss.setVariablesJSON(new Gson().toJson(dss.getVariables()));
		}

		int addDssResult = dssRepository.addDss(dss);
		long dssId = dss.getId();

		// dss_bm_list 등록
		for (Long bmId : dss.getBmIdList()) {
			DssBmList dssBmList = new DssBmList();
			dssBmList.setDssId(dssId);
			dssBmList.setBmId(bmId);
			dssBmList.setCreateId(dss.getCreateId());
			dssBmListRepository.addDssBmList(dssBmList);
		}
		return addDssResult;
	}

	/**
	 * 보고서를 수정한다.
	 * 
	 * @param dss
	 * @return
	 */
	@Override
	@Transactional
	public int updateDss(Dss dss) {
		// ISOData -> YYYYMMDD 로 변환 (날짜만 필요한 정보)
		dss.setAnalysisStartYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getAnalysisStart(),
				Constants.DATE_YMD_FORMAT));
		dss.setAnalysisEndYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getAnalysisEnd(), Constants.DATE_YMD_FORMAT));
		dss.setDataStartYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getDataStart(), Constants.DATE_YMD_FORMAT));
		dss.setDataEndYYYYMMDD(DateUtil.convertFormatFromISODate(dss.getDataEnd(), Constants.DATE_YMD_FORMAT));

		// variables 값을 json으로 저장. 복수개의 variable 값이 들어올 수 있다.)
		if (dss.getVariables() != null) {
			dss.setVariablesJSON(new Gson().toJson(dss.getVariables()));
		}
		// log.info("dss: {}", dss);
		int updateDssResult = dssRepository.updateDss(dss);

		long dssId = dss.getId();
		// 기 등록된 dss_bm_list를 삭제
		dssBmListRepository.deleteDssBmListByDssId(dssId, dss.getUpdateId());
		// 새로 dss_bm_list 등록
		for (Long bmId : dss.getBmIdList()) {
			DssBmList dssBmList = new DssBmList();
			dssBmList.setDssId(dssId);
			dssBmList.setBmId(bmId);
			dssBmList.setCreateId(dss.getUpdateId());
			// log.info("dssBmList: {}", dssBmList);
			dssBmListRepository.addDssBmList(dssBmList);
		}
		return updateDssResult;
	}

	/**
	 * dssId 이후 보고서를 조회한다.
	 * 
	 * @param dssId
	 * @return
	 */
	@Override
	public Dss getNextDss(long dssId) {
		return dssRepository.getNextDss(dssId);
	}

	/**
	 * dssId 이전 보고서를 조회한다.
	 * 
	 * @param dssId
	 * @return
	 */
	@Override
	public Dss getPreviousDss(long dssId) {
		return dssRepository.getPreviousDss(dssId);
	}

	@Override
	@Transactional
	public int deleteDss(long dssId, String deleteId) {
		// 첨부한 파일을 삭제한다.
		fileMetaRepository.deleteFileByContentId(dssId, deleteId);
		return dssRepository.deleteDss(dssId, deleteId);
	}

	@Override
	public List<Dss> getDssList(DssRequest dssRequest) {
		if (dssRequest.getSearchStart() != null) {
			dssRequest.setSearchStart(DateUtil.convertFormatFromISODate(dssRequest.getSearchStart(),
					Constants.DATE_YMD_FORMAT) + "000000");
			dssRequest.setSearchEnd(DateUtil.convertFormatFromISODate(dssRequest.getSearchEnd(),
					Constants.DATE_YMD_FORMAT) + "235959");
		}
		return dssRepository.getDssList(dssRequest);
	}
}

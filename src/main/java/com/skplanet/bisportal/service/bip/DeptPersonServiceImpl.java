package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.model.bip.ComDept;
import com.skplanet.bisportal.model.bip.ComPerson;
import com.skplanet.bisportal.model.bip.ComRoleUser;
import com.skplanet.bisportal.repository.acl.UserRepository;
import com.skplanet.bisportal.repository.bip.DeptPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 경영 실적 Admin 서비스 구현 클래스.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Slf4j
@Service
public class DeptPersonServiceImpl implements DeptPersonService {
	@Autowired
	private DeptPersonRepository deptPersonRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<ComDept> getDeptInfo(String sendDt) throws Exception {
		return deptPersonRepository.getDeptInfo(sendDt);
	}

	@Override
	public List<ComPerson> getPersonInfo(String sendDt) throws Exception {
		return deptPersonRepository.getPersonInfo(sendDt);
	}

	@Override
	@Transactional
	public void createComDeptAndHst(ComDept comDept) throws Exception {
		ComDept existsComDept = deptPersonRepository.getComDept(comDept.getOrgCd());
		if (existsComDept != null) {
			deptPersonRepository.createComDeptHst(comDept.getOrgCd());
		}
		deptPersonRepository.createComDept(comDept);
	}

	@Override
	@Transactional
	public void createComPersonAndHst(ComPerson comPerson) throws Exception {
		ComPerson existsComPerson = deptPersonRepository.getComPerson(comPerson.getLoginId());
		if (existsComPerson != null) {
			deptPersonRepository.createComPersonHst(comPerson.getLoginId());
			deptPersonRepository.updateComPerson(comPerson);
		} else {
			deptPersonRepository.createComPerson(comPerson);
		}
		// 전체 오픈 권한 추가.
		try {
			ComRoleUser comRoleUser = new ComRoleUser();
			comRoleUser.setRoleId(0);
			comRoleUser.setLoginId(comPerson.getLoginId());
			comRoleUser.setAuditId("BATCH");
			if (StringUtils.equals(Constants.NO, comPerson.getActvnYn())) {
				ComRoleUser existComRoleUser = userRepository.getComRoleUser(comRoleUser);
				if (existComRoleUser != null)
					userRepository.deleteComRoleUser(comRoleUser);
			} else {
				userRepository.createComRoleUser(comRoleUser);
			}
		} catch (Exception e) {
			log.error("createComPersonAndHst {}", e);
		}
	}
}

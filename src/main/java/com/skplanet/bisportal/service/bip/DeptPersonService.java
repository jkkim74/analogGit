package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.model.bip.ComDept;
import com.skplanet.bisportal.model.bip.ComPerson;

import java.util.List;

/**
 * DeptPersonService
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
public interface DeptPersonService {
	List<ComDept> getDeptInfo(String sendDt) throws Exception;

	List<ComPerson> getPersonInfo(String sendDt) throws Exception;

	void createComDeptAndHst(ComDept comDept) throws Exception;

	void createComPersonAndHst(ComPerson comPerson) throws Exception;
}

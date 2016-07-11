package com.skplanet.bisportal.schedule;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.ComDept;
import com.skplanet.bisportal.model.bip.ComPerson;
import com.skplanet.bisportal.service.bip.DeptPersonService;

/**
 * Cron(조직도, 직원 정보 동기화 배치.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Slf4j
@Component
public class TaskScheduler {
	@Autowired
	private DeptPersonService deptPersonServiceImpl;

	/**
	 * 매일 새벽 5시에 배치가 실행된다. 조직도/직원 동기화.
	 */
	@Scheduled(cron = "0 0 5 * * *")
	public void doComDeptPerson() {
		// 조직도 수신 처리
		try {
			String hostName = Utils.getHostName();
			if (StringUtils.isNotEmpty(hostName) && StringUtils.equals(hostName, Constants.HOST_WAS02)) {
				String today = DateUtil.getCurrentDate();
				log.info("doComDept({}) started.", today);
				doComDept(today);
				log.info("doComDept({}) completed.", today);
				log.info("doComPerson({}) started.", today);
				doComPerson(today);
				log.info("doComPerson({}) completed.", today);
			} else {
				log.info("hostName({}) can not started.", hostName);
			}
		} catch (Exception e) {
			log.error("doComDeptPerson {}", e);
		}
	}

	private void doComDept(String today) {
		try {
			List<ComDept> comDepts = deptPersonServiceImpl.getDeptInfo(today);
			for (ComDept comDept : comDepts) {
				try {
					log.info("부서 {}", comDept.getOrgCd());
					deptPersonServiceImpl.createComDeptAndHst(comDept);
				} catch (Exception e) {
					log.error("createComDeptAndHst {}", comDept.getOrgCd());
					log.error("createComDeptAndHst {}", e);
				}
			}
		} catch (Exception e) {
			log.error("doComDept {}", e);
		}
	}

	private void doComPerson(String today) {
		try {
			List<ComPerson> comPersons = deptPersonServiceImpl.getPersonInfo(today);
			for (ComPerson comPerson : comPersons) {
				try {
					log.info("직원 {}", comPerson.getLoginId());
					deptPersonServiceImpl.createComPersonAndHst(comPerson);
				} catch (Exception e) {
					log.error("createComPersonAndHst {}", comPerson.getLoginId());
					log.error("createComPersonAndHst {}", e);
				}
			}
		} catch (Exception e) {
			log.error("doComPerson {}", e);
		}
	}
}

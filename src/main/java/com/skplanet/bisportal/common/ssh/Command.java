package com.skplanet.bisportal.common.ssh;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.skplanet.bisportal.common.consts.Constants;

public abstract class Command {
	protected RuntimeExecutor executor;

	public void setRuntimeExecutor(RuntimeExecutor executor) {
		this.executor = executor;
	}

	/**
	 * 커맨드 실행 결과(성공/실패) 판별.
	 * 
	 * @param cmdResult
	 *            커맨드 실행 결과 로그.
	 * @return Success/Fail.
	 */
	protected String getResult(List<String> cmdResult) {
		if (cmdResult != null
				&& StringUtils.indexOf(cmdResult.get(cmdResult.size() - 1), Constants.SPRING_BATCH_COMPLETED) > 0) {
			return Constants.SUCCESS;
		} else {
			return Constants.FAIL;
		}
	}

	public abstract String execute(Map<String, String> cmd);
}

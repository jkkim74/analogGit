package com.skplanet.bisportal.common.ssh;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.consts.Constants;

@Slf4j
public class BatchCommand extends Command {
	@Override
	public String execute(Map<String, String> cmd) {
		String result = Constants.FAIL;
		try {
			List<String> cmds = Lists.newArrayList();
			cmds.add(Constants.RUN_SCRIPT);
			cmds.add(cmd.get("job")); // job.xml
			cmds.add(cmd.get("filename")); // Sam File
			super.setRuntimeExecutor(new RuntimeExecutor(cmds));
			result = getResult(super.executor.runAndOutput());
		} catch (Exception e) {
			log.error("execute {}", e);
			throw new RuntimeException("createNode exception generated.", e);
		} finally {
			if (super.executor != null) {
				super.executor.destroy();
			}
		}
		return result;
	}
}

package com.skplanet.bisportal.common.ssh;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rumtime.exec 실행 Wrapper 클래스.
 * 
 * @author HO-JIN, HA (mimul@wiseeco.com)
 */
@Slf4j
public final class RuntimeExecutor {
	private final List<String> args;
	private final boolean permitNonZeroExitStatus;
	private volatile Process process;
	private volatile boolean destroyed;

	public RuntimeExecutor(List<String> args) {
		this.args = new ArrayList<>(args);
		this.permitNonZeroExitStatus = false;
	}

	/**
	 * 커맨드 실행.
	 * 
	 * @return void
	 * @throws IOException
	 */
	public void run() throws IOException {
		if (isStarted()) {
			throw new RuntimeException("Already started!");
		}
		ProcessBuilder processBuilder = new ProcessBuilder().command(args).redirectErrorStream(true);
		process = processBuilder.start();
	}

	/**
	 * 커맨드 실행 결과값 출력.
	 * 
	 * @return 출력 결과값 리스트.
	 * @throws IOException
	 *             , InterruptedException
	 */
	public List<String> getOutputLog() throws IOException, InterruptedException {
		List<String> outputLogs = gatherOutputLog();
		int exitValue = process.waitFor();
		destroyed = true;
		if (exitValue != 0 && !permitNonZeroExitStatus) {
			throw new RuntimeExecutorException(args, outputLogs);
		}

		return outputLogs;
	}

	/**
	 * 커맨드 실행 및 결과 출력.
	 * 
	 * @return 출력 결과값 리스트.
	 * @throws RuntimeException
	 */
	public List<String> runAndOutput() {
		try {
			this.run();
			return this.getOutputLog();
		} catch (IOException e) {
			log.error("runAndOutput {}", e);
			throw new RuntimeException("io exception generated." + args, e);
		} catch (InterruptedException e) {
			log.error("runAndOutput {}", e);
			throw new RuntimeException("interrupted exception generated." + args, e);
		}
	}

	/**
	 * 실행 프로세스 자원 회수.(Too many open files 발생 예방)
	 * 
	 * @return void
	 * @throws RuntimeException
	 */
	public void destroy() {
		Process process = this.process;
		if (process == null) {
			throw new RuntimeException("illegal state exception");
		}
		if (destroyed) {
			return;
		}

		try {
			destroyed = true;
			process.destroy();
			process.waitFor();
			int exitValue = process.exitValue();
			log.debug("received exit value {} from destroyed command {}", exitValue, this);
		} catch (IllegalThreadStateException e) {
			log.error("destroy {}", e);
			throw new RuntimeException("illegalthread exception generated.", e);
		} catch (InterruptedException e) {
			log.error("destroy {}", e);
			throw new RuntimeException("interrupted exception generated.", e);
		}
	}

	/**
	 * 커맨드 실행 여부 확인.
	 * 
	 * @return true or false
	 * @throws
	 */
	public boolean isStarted() {
		return process != null;
	}

	/**
	 * 커맨드 실행 후 로킹 정보 추출.
	 * 
	 * @return 로깅 정보.
	 * @throws
	 */
	private List<String> gatherOutputLog() throws IOException {
		if (!isStarted()) {
			throw new IllegalStateException("Not started!");
		}
		List<String> outputLogs = Lists.newArrayList();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(process.getInputStream(), CharEncoding.UTF_8));
			String outputLine;
			while ((outputLine = in.readLine()) != null) {
				outputLogs.add(outputLine);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("gatherOutputLog {}", e);
			throw e;
		} catch (IOException e) {
			log.error("gatherOutputLog {}", e);
			throw e;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				log.error("IOException {}", e);
			}
		}
		return outputLogs;
	}
}

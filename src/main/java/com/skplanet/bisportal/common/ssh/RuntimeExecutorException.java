package com.skplanet.bisportal.common.ssh;

import java.util.List;

public class RuntimeExecutorException extends RuntimeException {
	private static final long serialVersionUID = -1244067629893677728L;

	private final List<String> args;
	private final List<String> outputLines;

	public RuntimeExecutorException(List<String> args, List<String> outputLines) {
		super(formatMessage(args, outputLines));
		this.args = args;
		this.outputLines = outputLines;
	}

	public static String formatMessage(List<String> args, List<String> outputLines) {
		StringBuilder result = new StringBuilder();
		result.append("Command failed:");
		for (String arg : args) {
			result.append(" ").append(arg);
		}
		for (String outputLine : outputLines) {
			result.append("\n  ").append(outputLine);
		}
		return result.toString();
	}

	public List<String> getArgs() {
		return args;
	}

//	public List<String> getOutputLines() {
//		return outputLines;
//	}

}

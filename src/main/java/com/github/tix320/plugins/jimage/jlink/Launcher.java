package com.github.tix320.plugins.jimage.jlink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.tix320.plugins.jimage.common.ValidationException;

/**
 * @author : Tigran Sargsyan
 * @since : 09.03.2021
 **/
public class Launcher {

	private String command;

	private String mainModule;

	private String mainClass;

	private Map<String, String> vmOptions = new HashMap<>();

	public Launcher() {
	}

	public String getCommand() {
		return command;
	}

	public String getMainModule() {
		return mainModule;
	}

	public String getMainClass() {
		return mainClass;
	}

	public Set<String> getVmOptions() {
		return new HashSet<>(vmOptions.values());
	}

	public void validate() throws ValidationException {
		if (command == null) {
			throw new ValidationException("Launcher's `command` not specified.");
		}

		if (mainModule == null) {
			throw new ValidationException("Launcher's `main-module` not specified.");
		}

		if (mainClass == null) {
			throw new ValidationException("Launcher's `main-class` not specified.");
		}
	}

	@Override
	public String toString() {
		return "Launcher{"
			   + "command='"
			   + command
			   + '\''
			   + ", mainModule='"
			   + mainModule
			   + '\''
			   + ", mainClass='"
			   + mainClass
			   + '\''
			   + ", vmOptions="
			   + vmOptions
			   + '}';
	}
}

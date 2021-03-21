package com.github.tix320.plugins.jimage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : Tigran Sargsyan
 * @since : 09.03.2021
 **/
public class Launcher {

	private String command;

	private String mainModule;

	private String mainClass;

	private Set<String> vmOptions = new HashSet<>();

	public Launcher() {
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setMainModule(String mainModule) {
		this.mainModule = mainModule;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public void setVmOptions(Set<String> vmOptions) {
		this.vmOptions = new HashSet<>();
		if (vmOptions != null) {
			this.vmOptions.addAll(vmOptions);
		}
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
		return vmOptions;
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

	public void fillFrom(Launcher launcher) {
		if (command == null) {
			command = launcher.command;
		}

		if (mainModule == null) {
			mainModule = launcher.mainModule;
		}

		if (mainClass == null) {
			mainClass = launcher.mainClass;
		}

		vmOptions.addAll(launcher.vmOptions);
	}
}

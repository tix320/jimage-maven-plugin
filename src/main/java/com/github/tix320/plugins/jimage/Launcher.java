package com.github.tix320.plugins.jimage;

/**
 * @author : Tigran Sargsyan
 * @since : 09.03.2021
 **/
public class Launcher {

	private String command;

	private String mainModule;

	private String mainClass;

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

	public String getCommand() {
		return command;
	}

	public String getMainModule() {
		return mainModule;
	}

	public String getMainClass() {
		return mainClass;
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
}

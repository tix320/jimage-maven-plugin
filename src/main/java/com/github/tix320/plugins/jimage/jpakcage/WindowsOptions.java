package com.github.tix320.plugins.jimage.jpakcage;

import java.util.ArrayList;
import java.util.List;

import com.github.tix320.plugins.jimage.common.Options;
import com.github.tix320.plugins.jimage.common.ValidationException;

/**
 * @author : Tigran Sargsyan
 * @since : 02.04.2021
 **/
public class WindowsOptions implements Options {

	private boolean enableDirChooser;

	private boolean includeInSystemMenu;

	private String startMenuGroupName;

	private boolean perUserInstall;

	private boolean createDesktopShortcut;

	private boolean withConsole;

	private String runtimeImagePath;

	@Override
	public List<String> toArgs() throws ValidationException {
		if (runtimeImagePath == null) {
			throw new ValidationException("Runtime image is required");
		}

		final List<String> args = new ArrayList<>();

		args.add("--runtime-image");
		args.add(runtimeImagePath);

		if (enableDirChooser) {
			args.add("--win-dir-chooser");
		}

		if (includeInSystemMenu) {
			args.add("--win-menu");
		}

		if (startMenuGroupName != null) {
			args.add("--win-menu-group");
			args.add(startMenuGroupName);
		}

		if (perUserInstall) {
			args.add("--win-per-user-install");
		}

		if (createDesktopShortcut) {
			args.add("--win-shortcut");
		}

		if (withConsole) {
			args.add("--win-console");
		}

		return args;
	}
}

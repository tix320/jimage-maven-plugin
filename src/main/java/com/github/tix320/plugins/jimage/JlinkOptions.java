package com.github.tix320.plugins.jimage;

import java.io.File;
import java.util.*;

/**
 * @author : Tigran Sargsyan
 * @since : 10.03.2021
 **/
public class JlinkOptions {

	private boolean verbose;

	private Map<String, String> modulePaths = new HashMap<>();

	private Map<String, String> rootModules = new HashMap<>();

	private Map<String, String> options = new HashMap<>();

	private int compression = 0;

	private boolean noHeaderFiles;

	private boolean noManPages;

	private boolean stripDebug;

	private Launcher launcher;

	private String outputPath;

	public Launcher getLauncher() {
		return launcher;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public List<String> toArgs() throws ValidationException {
		List<String> args = new ArrayList<>();

		if (verbose) {
			args.add("--verbose");
		}

		args.add("--compress");
		args.add(String.valueOf(compression));

		if (noHeaderFiles) {
			args.add("--no-header-files");
		}

		if (noManPages) {
			args.add("--no-man-pages");
		}

		if (stripDebug) {
			args.add("--strip-debug");
		}

		if (modulePaths.isEmpty()) {
			throw new ValidationException("At least one module path must be specified.");
		} else {
			if (modulePaths.containsValue(null)) {
				throw new ValidationException("`null` value in modulePaths: " + modulePaths);
			}

			String modulePaths = String.join(File.pathSeparator, new HashSet<>(this.modulePaths.values()));
			args.add("--module-path");
			args.add(modulePaths);
		}

		if (rootModules.isEmpty()) {
			throw new ValidationException("At least one root module must be specified.");
		} else {
			String modules = String.join(File.pathSeparator, new HashSet<>(this.rootModules.values()));
			args.add("--add-modules");
			args.add(modules);
		}

		if (!options.isEmpty()) {
			String options = String.join(" ", new HashSet<>(this.options.values()));

			args.add("--add-options");
			args.add(options);
		}

		if (launcher != null) {
			launcher.validate();

			String launcherArg = launcher.getCommand() + "=" + launcher.getMainModule() + "/" + launcher.getMainClass();

			args.add("--launcher");
			args.add(launcherArg);
		}

		if (outputPath == null) {
			throw new ValidationException("Output path not specified");
		} else {
			args.add("--output");
			args.add(outputPath);
		}

		return args;
	}

	@Override
	public String toString() {
		return "JlinkOptions{"
			   + "verbose="
			   + verbose
			   + ", modulePaths="
			   + modulePaths
			   + ", rootModules="
			   + rootModules
			   + ", options="
			   + options
			   + ", compression="
			   + compression
			   + ", noHeaderFiles="
			   + noHeaderFiles
			   + ", noManPages="
			   + noManPages
			   + ", stripDebug="
			   + stripDebug
			   + ", launcher="
			   + launcher
			   + ", outputPath='"
			   + outputPath
			   + '\''
			   + '}';
	}
}

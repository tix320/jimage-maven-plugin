package com.github.tix320.plugins.jimage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Tigran Sargsyan
 * @since : 10.03.2021
 **/
public class JlinkOptions {

	private Boolean verbose;

	private Set<String> modulePaths = new HashSet<>();

	private Set<String> rootModules = new HashSet<>();

	private Set<String> options = new HashSet<>();

	private Integer compression;

	private Boolean noHeaderFiles;

	private Boolean noManPages;

	private Boolean stripDebug;

	private Launcher launcher;

	private String outputPath;

	public Boolean getVerbose() {
		return verbose;
	}

	public void setVerbose(Boolean verbose) {
		this.verbose = verbose;
	}

	public Set<String> getModulePaths() {
		return modulePaths;
	}

	public void setModulePaths(Set<String> modulePaths) {
		this.modulePaths = new HashSet<>();
		if (modulePaths != null) {
			this.modulePaths.addAll(modulePaths);
		}
	}

	public Set<String> getRootModules() {
		return rootModules;
	}

	public void setRootModules(Set<String> rootModules) {
		this.rootModules = new HashSet<>();
		if (rootModules != null) {
			this.rootModules.addAll(rootModules);
		}
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = new HashSet<>();
		if (options != null) {
			this.options.addAll(options);
		}
	}

	public Integer getCompression() {
		return compression;
	}

	public void setCompression(Integer compression) {
		this.compression = compression;
	}

	public Boolean getNoHeaderFiles() {
		return noHeaderFiles;
	}

	public void setNoHeaderFiles(Boolean noHeaderFiles) {
		this.noHeaderFiles = noHeaderFiles;
	}

	public Boolean getNoManPages() {
		return noManPages;
	}

	public void setNoManPages(Boolean noManPages) {
		this.noManPages = noManPages;
	}

	public Boolean getStripDebug() {
		return stripDebug;
	}

	public void setStripDebug(Boolean stripDebug) {
		this.stripDebug = stripDebug;
	}

	public Launcher getLauncher() {
		return launcher;
	}

	public void setLauncher(Launcher launcher) {
		this.launcher = launcher;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void fillFrom(JlinkOptions jlinkOptions) {
		if (verbose == null) {
			verbose = jlinkOptions.verbose;
		}

		modulePaths.addAll(jlinkOptions.modulePaths);
		rootModules.addAll(jlinkOptions.rootModules);
		options.addAll(jlinkOptions.options);

		if (compression == null) {
			compression = jlinkOptions.compression;
		}

		if (noHeaderFiles == null) {
			noHeaderFiles = jlinkOptions.noHeaderFiles;
		}

		if (noManPages == null) {
			noManPages = jlinkOptions.noManPages;
		}

		if (stripDebug == null) {
			stripDebug = jlinkOptions.stripDebug;
		}

		if (launcher == null) {
			launcher = jlinkOptions.launcher;
		} else {
			launcher.fillFrom(jlinkOptions.launcher);
		}

		if (outputPath == null) {
			outputPath = jlinkOptions.outputPath;
		}
	}

	public List<String> toArgs() throws ValidationException {
		List<String> args = new ArrayList<>();

		if (verbose != null && verbose) {
			args.add("--verbose");
		}

		args.add("--compress");
		args.add(String.valueOf(compression));

		if (noHeaderFiles != null && noHeaderFiles) {
			args.add("--no-header-files");
		}

		if (noManPages != null && noManPages) {
			args.add("--no-man-pages");
		}

		if (stripDebug != null && stripDebug) {
			args.add("--strip-debug");
		}

		if (modulePaths.isEmpty()) {
			throw new ValidationException("At least one module path must be specified.");
		} else {
			if (modulePaths.contains(null)) {
				throw new ValidationException("`null` value in modulePaths");
			}

			String modulePaths = String.join(File.pathSeparator, this.modulePaths);
			args.add("--module-path");
			args.add(modulePaths);
		}

		if (rootModules.isEmpty()) {
			throw new ValidationException("At least one root module must be specified.");
		} else {
			String modules = String.join(File.pathSeparator, this.rootModules);
			args.add("--add-modules");
			args.add(modules);
		}

		if (!options.isEmpty()) {
			String options = String.join(" ", this.options);

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

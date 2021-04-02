package com.github.tix320.plugins.jimage.jpakcage;

import java.util.ArrayList;
import java.util.List;

import com.github.tix320.plugins.jimage.common.Options;
import com.github.tix320.plugins.jimage.common.ValidationException;

/**
 * @author : Tigran Sargsyan
 * @since : 02.04.2021
 **/
public class CommonOptions implements Options {

	private boolean verbose;

	private String name;

	private String type;

	private String appVersion;

	private String copyright;

	private String description;

	private String vendor;

	private String icon;

	private String outputPath;

	private String mainModule;

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	@Override
	public List<String> toArgs() throws ValidationException {
		if (name == null) {
			throw new ValidationException("Package name is required");
		}

		List<String> args = new ArrayList<>();

		if (verbose) {
			args.add("--verbose");
		}

		args.add("--name");
		args.add(name);

		if (type != null) {
			args.add("--type");
			args.add(type);
		}

		if (appVersion != null) {
			args.add("--app-version");
			args.add(appVersion);
		}

		if (copyright != null) {
			args.add("--copyright");
			args.add(copyright);
		}

		if (description != null) {
			args.add("--description");
			args.add(description);
		}

		if (vendor != null) {
			args.add("--vendor");
			args.add(vendor);
		}

		if (icon != null) {
			args.add("--icon");
			args.add(icon);
		}

		args.add("--dest");
		args.add(outputPath);

		if (mainModule != null) {
			args.add("--module");
			args.add(mainModule);
		}

		return args;
	}
}

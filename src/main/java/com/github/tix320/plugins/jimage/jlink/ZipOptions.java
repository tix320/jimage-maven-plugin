package com.github.tix320.plugins.jimage.jlink;

public class ZipOptions {

	private boolean enable = true;

	private boolean preserveRootFolder;

	public boolean isEnable() {
		return enable;
	}

	public boolean preserveRootFolder() {
		return preserveRootFolder;
	}

	@Override
	public String toString() {
		return "ZipOptions{" + "enable=" + enable + ", preserveRootFolder=" + preserveRootFolder + '}';
	}
}

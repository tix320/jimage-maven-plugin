package com.github.tix320.plugins.jimage;

public class ZipOptions {

	private boolean enable = true;

	private boolean preserveRootFolder;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean preserveRootFolder() {
		return preserveRootFolder;
	}

	public void setPreserveRootFolder(boolean preserveRootFolder) {
		this.preserveRootFolder = preserveRootFolder;
	}
}

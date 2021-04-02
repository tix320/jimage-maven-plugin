package com.github.tix320.plugins.jimage.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.codehaus.plexus.util.FileUtils;

public class FilesUtils {

	public static void replaceInFile(Path path, String target, String replacement) throws IOException {
		final byte[] bytes = Files.readAllBytes(path);
		final String content = new String(bytes);
		final String result = content.replace(target, replacement);
		Files.writeString(path, result, StandardOpenOption.WRITE);
	}

	public static void preserveDirectory(Path directory) throws IOException {
		if (Files.exists(directory) && !Files.isDirectory(directory)) {
			throw new NotDirectoryException(directory.toString());
		} else {
			FileUtils.forceDelete(directory.toFile());
		}
	}
}

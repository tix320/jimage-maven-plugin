package com.github.tix320.plugins.jimage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FilesUtils {

	public static void replaceInFile(Path path, String target, String replacement) throws IOException {
		final byte[] bytes = Files.readAllBytes(path);
		final String content = new String(bytes);
		final String result = content.replace(target, replacement);
		Files.writeString(path, result, StandardOpenOption.WRITE);
	}
}

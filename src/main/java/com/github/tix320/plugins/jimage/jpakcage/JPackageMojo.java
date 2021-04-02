package com.github.tix320.plugins.jimage.jpakcage;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;
import java.util.spi.ToolProvider;

import com.github.tix320.deft.api.OS;
import com.github.tix320.plugins.jimage.common.FilesUtils;
import com.github.tix320.plugins.jimage.common.Options;
import com.github.tix320.plugins.jimage.common.ToolUtils;
import com.github.tix320.plugins.jimage.common.ValidationException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author : Tigran Sargsyan
 * @since : 02.04.2021
 **/
@Mojo(name = "jpackage")
public class JPackageMojo extends AbstractMojo {

	@Parameter
	private CommonOptions commonOptions;

	@Parameter
	private WindowsOptions windowsOptions;

	@Parameter
	private LinuxOptions linuxOptions;

	@Parameter
	private MacOptions macOptions;

	@Parameter(readonly = true, defaultValue = "${project}")
	private MavenProject project;

	private final ToolProvider jpackage;

	public JPackageMojo() {
		jpackage = ToolProvider.findFirst("jpackage").orElse(null);
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (jpackage == null) {
			throw new MojoExecutionException("Jpackage tool not found in current JDK.");
		}

		final OS currentOs = OS.CURRENT;

		Options osSpecificOptions;
		switch (currentOs) {
			case WINDOWS:
				osSpecificOptions = windowsOptions;
				break;
			case LINUX:
				osSpecificOptions = linuxOptions;
				break;
			case MAC:
				osSpecificOptions = macOptions;
				break;
			case UNKNOWN:
			default:
				throw new MojoFailureException("Current OS is unknown");
		}

		if (osSpecificOptions == null) {
			getLog().info("Config not found for current OS: " + OS.CURRENT);
			return;
		}

		if (commonOptions.getOutputPath() == null) {
			String defaultOutputDirectory = preserveDefaultOutputDirectory();
			commonOptions.setOutputPath(defaultOutputDirectory);
		}

		createImage(commonOptions, osSpecificOptions);
	}

	private void createImage(CommonOptions commonOptions, Options additionalOptions) throws MojoFailureException {
		List<String> args;
		try {
			args = commonOptions.toArgs();
			args.addAll(additionalOptions.toArgs());
		} catch (ValidationException e) {
			throw new MojoFailureException(e.getMessage());
		}

		getLog().info("Executing JPackage with arguments: " + args);

		boolean success = ToolUtils.executeTool(jpackage, args.toArray(String[]::new),
				line -> getLog().info("JPACKAGE --- " + line), line -> getLog().error("JPACKAGE --- " + line));

		if (!success) {
			throw new MojoFailureException(
					"Jpackage command failed with arguments: " + args + ". See Jpackage output for details.");
		}
	}

	private String preserveDefaultOutputDirectory() throws MojoFailureException {
		String targetDirectory = project.getModel().getBuild().getDirectory();
		final Path outputPath = Path.of(targetDirectory, "jpackage", "default");

		try {
			FilesUtils.preserveDirectory(outputPath);
		} catch (NotDirectoryException e) {
			throw new MojoFailureException("Output is not a directory: " + outputPath);
		} catch (IOException e) {
			throw new MojoFailureException("Unable to delete output directory: " + outputPath, e);
		}

		return outputPath.toString();
	}
}

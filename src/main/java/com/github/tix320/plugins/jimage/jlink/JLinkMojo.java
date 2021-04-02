package com.github.tix320.plugins.jimage.jlink;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.spi.ToolProvider;

import com.github.tix320.plugins.jimage.common.FilesUtils;
import com.github.tix320.plugins.jimage.common.ToolUtils;
import com.github.tix320.plugins.jimage.common.ValidationException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.zeroturnaround.zip.ZipUtil;

/**
 * @author : Tigran Sargsyan
 * @since : 09.03.2021
 **/
@Mojo(name = "jlink")
public class JLinkMojo extends AbstractMojo {

	@Parameter
	private JlinkOptions jlinkOptions;

	@Parameter
	private ZipOptions zipOptions;

	@Parameter(readonly = true, defaultValue = "${project}")
	private MavenProject project;

	private final ToolProvider jlink;

	public JLinkMojo() {
		jlink = ToolProvider.findFirst("jlink").orElse(null);
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (jlink == null) {
			throw new MojoExecutionException("JLink tool not found in current JDK.");
		}

		if (jlinkOptions.getOutputPath() == null) {
			String defaultOutputDirectory = preserveDefaultOutputDirectory();
			jlinkOptions.setOutputPath(defaultOutputDirectory);
		}

		createJlinkImage(jlinkOptions);
	}

	private void createJlinkImage(JlinkOptions jlinkOptions) throws MojoFailureException {
		List<String> args;
		try {
			args = jlinkOptions.toArgs();
		} catch (ValidationException e) {
			throw new MojoFailureException(e.getMessage());
		}

		getLog().info("Executing Jlink with arguments: " + args);

		boolean success = ToolUtils.executeTool(jlink, args.toArray(String[]::new),
				line -> getLog().info("JLINK --- " + line), line -> getLog().error("JLINK --- " + line));

		if (!success) {
			throw new MojoFailureException(
					"Jlink command failed with arguments: " + args + ". See Jlink output for details.");
		}

		File outputFile = new File(jlinkOptions.getOutputPath());


		final Launcher launcher = jlinkOptions.getLauncher();
		if (launcher != null && !launcher.getVmOptions().isEmpty()) {
			final Set<String> vmOptions = new HashSet<>(launcher.getVmOptions());
			getLog().info("Injecting launcher VM options: " + vmOptions);
			final String command = launcher.getCommand();
			final Path launcherPath = outputFile.toPath().resolve("bin").resolve(command);
			final String vmOptionsString = String.join(" ", vmOptions);
			try {
				final String target = "JLINK_VM_OPTIONS=";
				final String replacement = target + vmOptionsString;
				FilesUtils.replaceInFile(launcherPath, target, replacement);

				final Path batLauncher = Path.of(launcherPath.toString() + ".bat");
				if (Files.exists(batLauncher)) {
					FilesUtils.replaceInFile(batLauncher, target, replacement);
				}

			} catch (IOException e) {
				throw new MojoFailureException("Failed to inject VM options to launcher", e);
			}
		}

		if (zipOptions != null && zipOptions.isEnable()) {
			String parentDirectory = outputFile.getParent();
			String outputFileName = outputFile.getName();
			String zipPath = parentDirectory + File.separator + outputFileName + ".zip";

			getLog().info(String.format("Creating zip of image in: %s", zipPath));

			ZipUtil.pack(outputFile, new File(zipPath), zipOptions.preserveRootFolder());
		}
	}

	private String preserveDefaultOutputDirectory() throws MojoFailureException {
		String targetDirectory = project.getModel().getBuild().getDirectory();
		final Path outputPath = Path.of(targetDirectory, "jlink", "default");

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

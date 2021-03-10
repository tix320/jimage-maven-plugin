package com.github.tix320.plugins.jimage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.spi.ToolProvider;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

/**
 * @author : Tigran Sargsyan
 * @since : 09.03.2021
 **/
@Mojo(name = "jlink", defaultPhase = LifecyclePhase.PACKAGE)
public class JLinkMojo extends AbstractMojo {

	@Parameter
	private JlinkOptions jlinkOptions;

	@Parameter
	private Map<String, JlinkOptions> images;

	@Parameter
	private boolean createZip;

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

		String defaultOutputDirectory = preserveDefaultOutputDirectory();

		if (images == null || images.isEmpty()) {
			if (jlinkOptions.getOutputPath() == null) {
				jlinkOptions.setOutputPath(defaultOutputDirectory);
			}

			createJlinkImage(jlinkOptions);
		} else {
			for (Entry<String, JlinkOptions> entry : images.entrySet()) {
				String name = entry.getKey();
				JlinkOptions jlinkOptions = entry.getValue();

				if (jlinkOptions == null) {
					throw new MojoFailureException(String.format("Image config with name `%s` is invalid.", name));
				}

				if (jlinkOptions.getOutputPath() == null) {
					jlinkOptions.setOutputPath(defaultOutputDirectory + File.separator + name);
				}

				if (this.jlinkOptions != null) {
					jlinkOptions.fillFrom(this.jlinkOptions);
				}

				getLog().info(String.format("Creating image: `%s`", name));

				createJlinkImage(jlinkOptions);
			}
		}
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

		if (createZip) {
			File outputFile = new File(jlinkOptions.getOutputPath());
			String parentDirectory = outputFile.getParent();
			String outputFileName = outputFile.getName();
			String zipPath = parentDirectory + File.separator + outputFileName + ".zip";

			getLog().info(String.format("Creating zip of image in: %s", zipPath));

			ZipUtil.pack(outputFile, new File(zipPath), true);
		}
	}

	private String preserveDefaultOutputDirectory() throws MojoFailureException {
		String targetDirectory = project.getModel().getBuild().getDirectory();
		String output = targetDirectory + File.separator + "jlink";

		File outputFile = new File(output);
		if (outputFile.exists() && !outputFile.isDirectory()) {
			throw new MojoFailureException("Output is not a directory: " + output);
		} else {
			try {
				FileUtils.forceDelete(outputFile);
			} catch (IOException e) {
				throw new MojoFailureException("Unable to delete output directory: " + output);
			}
		}

		return output;
	}
}
package com.github.tix320.plugins.jimage.common;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;
import java.util.function.Consumer;
import java.util.spi.ToolProvider;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author : Tigran Sargsyan
 * @since : 10.03.2021
 **/
public class ToolUtils {

	public static boolean executeTool(ToolProvider toolProvider, String[] args, Consumer<String> outConsumer,
									  Consumer<String> errConsumer) throws MojoFailureException {
		PipedOutputStream outputStream = new PipedOutputStream();
		PipedOutputStream errorStream = new PipedOutputStream();

		Runnable outCloser = logStreamAsync(outputStream, outConsumer);
		Runnable errCLoser = logStreamAsync(errorStream, errConsumer);

		int status = toolProvider.run(new PrintStream(outputStream), new PrintStream(errorStream), args);

		try {
			outputStream.flush();
			errorStream.flush();
		} catch (IOException ignored) {

		}

		outCloser.run();
		errCLoser.run();

		return status == 0;
	}

	private static Runnable logStreamAsync(PipedOutputStream pipedOutputStream,
										   Consumer<String> lineConsumer) throws MojoFailureException {
		try {
			PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream, 1024);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pipedInputStream));

			Exchanger<Void> exchanger = new Exchanger<>();

			CompletableFuture.runAsync(() -> {
				try {

					while (true) {
						String line = bufferedReader.readLine();
						if (line == null) {
							exchanger.exchange(null);
							break;
						}

						lineConsumer.accept(line);
					}
				} catch (Throwable ignored) {
				}
			});

			return () -> {
				try {
					pipedOutputStream.close();
					exchanger.exchange(null);
				} catch (IOException | InterruptedException ignored) {
				}
			};

		} catch (IOException e) {
			throw new MojoFailureException("Unexpected IOException", e);
		}
	}
}

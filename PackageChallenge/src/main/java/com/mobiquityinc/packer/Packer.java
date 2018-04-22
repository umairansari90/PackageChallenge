package com.mobiquityinc.packer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.parser.PackageParser;
import com.mobiquityinc.packer.processor.PackageProcessor;

/**
 * This class solves Package Challenge. There is a static method named "pack"
 * which takes absolute path as input and returns "String" type response"
 *
 * @author UmairZ
 * @since Apr 20, 2018
 *
 */
public class Packer {

	private PackageParser packageParser = new PackageParser();
	private PackageProcessor packageProcessor = new PackageProcessor();

	/**
	 * 
	 * @param absoluteFilePath
	 * @return Result
	 * @throws APIException
	 *             in case of invalid/missing file name
	 */
	public static String pack(String absoluteFilePath) {
		// As mentioned in PDF file there isn't any main function rather a static method
		// named pack
		// which takes absolutes file name as put and returns response in the form of
		// String

		validateInput(absoluteFilePath);
		Packer packer = new Packer();
		return packer.process(absoluteFilePath);
	}

	private String process(String absoluteFilePath) {

		File path = validateAndGetFile(absoluteFilePath);
		StringBuilder result = new StringBuilder();

		// Used Try-Catch-Resources introduced in Java 1.7 that closes resources
		// automatically
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(packageProcessor.processPackage(packageParser.parsePackage(line)))
						.append(System.lineSeparator());
			}

		} catch (IOException ioException) {
			throw new APIException("Error while reading file");
		}

		return result.toString();
	}

	private File validateAndGetFile(String absolutePath) {
		File file = null;
		try {
			file = new File(absolutePath);

			if (!file.exists() || !file.isFile()) {
				throw new APIException("File does not exist");
			}
		} catch (InvalidPathException invalidPathException) {
			throw new APIException("Path to file isn't valid " + invalidPathException.getReason());
		}

		return file;
	}

	private static void validateInput(String input) {
		if (input == null || input.isEmpty()) {
			throw new APIException("File path is Missing");
		}
	}
}

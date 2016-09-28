package com.skplanet.pandora.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public abstract class CsvCreatorTemplate<T> {

	public final Path create(String filename) throws IOException {
		Path filePath = Paths.get(Constant.UPLOADED_FILE_DIR, filename);

		try (CSVPrinter printer = CSVFormat.DEFAULT.print(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

			List<T> list = nextList();

			if (!list.isEmpty()) {
				// csv 첫줄에 헤더추가
				printHeader(printer, list);

				do {
					for (T t : list) {
						printEach(printer, t);
					}
				} while (!(list = nextList()).isEmpty());
			}
		}

		return filePath;
	}

	abstract protected List<T> nextList();

	protected void printHeader(CSVPrinter printer, List<T> list) {
	}

	abstract protected void printEach(CSVPrinter printer, T t) throws IOException;

}
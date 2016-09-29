package com.skplanet.pandora.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.skplanet.pandora.exception.BizException;

/**
 * 대량 데이터를 끊어서 파일에 쓰기 위한 용도
 */
public abstract class CsvCreatorTemplate<T> {

	public final Path create(String filename) {
		Path filePath = Paths.get(Constant.UPLOADED_FILE_DIR, filename);

		try (CSVPrinter printer = CSVFormat.DEFAULT.print(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

			List<T> list = nextList();

			if (!list.isEmpty()) {
				// 파일 첫줄에 헤더 추가
				printHeader(printer, list);

				do {
					// 각 행을 파일에 쓰기
					for (T t : list) {
						printRecord(printer, t);
					}
				} while (!(list = nextList()).isEmpty()); // 건 단위로 읽어서 없을 때까지 반복
			}
		} catch (IOException e) {
			throw new BizException("CSV 파일 생성 실패", e);
		}

		return filePath;
	}

	abstract protected List<T> nextList();

	protected void printHeader(CSVPrinter printer, List<T> list) throws IOException {
	}

	abstract protected void printRecord(CSVPrinter printer, T t) throws IOException;

}
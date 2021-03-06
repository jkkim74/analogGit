package com.skplanet.web.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.skplanet.web.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 대량 데이터를 끊어서 파일에 쓰기 위한 용도
 */
@Slf4j
public abstract class CsvCreatorTemplate<T> {

	private int offset;
	private int limit;
	private boolean once;

	public CsvCreatorTemplate() {
		this.once = true;
	}

	public CsvCreatorTemplate(int limit) {
		if (limit > 0) {
			this.limit = limit;
		}
		this.once = false;
	}

	public final Path create(Path filePath) {
		return create(filePath, ',', StandardCharsets.UTF_8);
	}

	public final Path create(Path filePath, Charset charset) {
		return create(filePath, ',', charset);
	}
	
	public final Path create(Path filePath, String logTyp) {
		return create(filePath, ',',  logTyp, StandardCharsets.UTF_8);
	}	

	public final Path create(Path filePath, char delimiter, Charset charset) {
		// UTF-8 -> CP949 변환등 문자셋에 존재하지 않는 문자는 무시하도록 설정.
		CharsetEncoder encoder = charset.newEncoder().onUnmappableCharacter(CodingErrorAction.IGNORE);

		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(Files.newOutputStream(filePath), encoder));
				CSVPrinter printer = CSVFormat.RFC4180.withDelimiter(delimiter).withQuote(null).print(writer)) {


			List<T> list = nextList(offset, limit);

			if (list != null && !list.isEmpty()) {
				
				// 파일 첫줄에 헤더 추가				
				printHeader(printer, list);

				while (list != null && !list.isEmpty()) {
					// 각 행을 파일에 쓰기
					for (T t : list) {
						printRecord(printer, t);
					}
					if (once) {
						break;
					}
					offset += limit;
					list = nextList(offset, limit);
				}
			}
			

		} catch (IOException e) {
			throw new BizException("CSV 파일 생성 실패", e);
		}	

		return filePath;
	}
	
	public final Path create(Path filePath, char delimiter, String logTyp, Charset charset) {
		
		log.debug("########## CsvCreatorTemplate.logTyp={}", logTyp);		

		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(Files.newOutputStream(filePath),charset));
				CSVPrinter printer = CSVFormat.RFC4180.withDelimiter(delimiter).withQuote(null).print(writer)) {			
			
			List<T> list = nextList(offset, limit);
			
			log.debug("########## CsvCreatorTemplate.list.size={}", list.size());	

			if (list != null && !list.isEmpty()) {
				
				// 파일 첫줄에 헤더 추가				
				printHeader(printer, list);

				while (list != null && !list.isEmpty()) {
					// 각 행을 파일에 쓰기
					for (T t : list) {
						printRecord(printer, t);
					}
					if (once) {
						break;
					}
					offset += limit;
					list = nextList(offset, limit);
				}
			}

		} catch (IOException e) {
			throw new BizException("CSV 파일 생성 실패", e);
		}	
				
		return filePath;
	}	

	abstract protected List<T> nextList(int offset, int limit);

	protected void printHeader(CSVPrinter printer, List<T> list) throws IOException {
	}

	abstract protected void printRecord(CSVPrinter printer, T t) throws IOException;

}
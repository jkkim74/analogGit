package com.skplanet.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.skplanet.web.model.AutoMap;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.CsvCreatorTemplate;

public class CsvCreatorTemplateTest {

	private Path file;

	@Before
	public void setUp() throws Exception {
		file = Paths.get(Constant.APP_FILE_DIR, "test.csv");
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(file);
	}

	@Test
	public void testNormal() throws IOException {
		CsvCreatorTemplate<AutoMap> csvCreator = new CsvCreatorTemplate<AutoMap>() {
			boolean done;

			@Override
			protected List<AutoMap> nextList() {
				AutoMap m = new AutoMap();
				m.put("aString", "string");
				m.put("aEmptyString", "");
				m.put("aBlankString", "  ");
				m.put("aInt", 1);
				m.put("aBoolean", false);
				m.put("aNull", null);

				List<AutoMap> list = new ArrayList<>();
				list.add(m);

				if (done) {
					return Collections.emptyList();
				}
				done = true;

				return list;
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMap t) throws IOException {
				printer.printRecord(t.values());
			}
		};

		Path testCsv = csvCreator.create(file);

		try (BufferedReader reader = Files.newBufferedReader(testCsv, StandardCharsets.UTF_8)) {
			String line = reader.readLine();

			System.out.println(line);

			Assert.assertEquals(5, StringUtils.countOccurrencesOf(line, ","));
			Assert.assertEquals("string,,  ,1,false,", line);
		}
	}

	@Test
	public void testDelimiterAndEncoding() throws IOException {
		char delimiter = '▦';
		final Charset charset = Charset.forName("CP949");

		CsvCreatorTemplate<AutoMap> csvCreator = new CsvCreatorTemplate<AutoMap>() {
			boolean none;

			@Override
			protected List<AutoMap> nextList() {
				AutoMap m = new AutoMap();
				m.put("aString", "똠방각하");
				m.put("email", " ？？？？@yahoo.co.kr");

				List<AutoMap> list = new ArrayList<>();
				list.add(m);

				if (none) {
					return Collections.emptyList();
				}
				none = true;
				return list;
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMap t) throws IOException {
				printer.printRecord(t.values());
			}
		};

		Path testCsv = csvCreator.create(file, delimiter, charset);

		try (BufferedReader reader = Files.newBufferedReader(testCsv, charset)) {
			String line = reader.readLine();

			System.out.println(line);

			Assert.assertEquals(1, StringUtils.countOccurrencesOf(line, "▦"));
			Assert.assertEquals("방각하▦ ？？？？@yahoo.co.kr", line);
		}
	}

}

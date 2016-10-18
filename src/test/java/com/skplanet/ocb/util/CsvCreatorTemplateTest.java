package com.skplanet.ocb.util;

import java.io.BufferedReader;
import java.io.IOException;
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

import com.skplanet.pandora.util.Constant;

public class CsvCreatorTemplateTest {

	private Path file;

	@Before
	public void setUp() throws Exception {
		file = Paths.get(Constant.UPLOADED_FILE_DIR, "test.csv");
	}

	@After
	public void tearDown() throws Exception {
		Files.delete(file);
	}

	@Test
	public void test() throws IOException {
		CsvCreatorTemplate<AutoMappedMap> csvCreator = new CsvCreatorTemplate<AutoMappedMap>() {
			boolean none;

			@Override
			protected List<AutoMappedMap> nextList() {
				AutoMappedMap m = new AutoMappedMap();
				m.put("aString", "string");
				m.put("aEmptyString", "");
				m.put("aBlankString", "  ");
				m.put("aNull", null);
				m.put("aInt", 1);
				m.put("aBoolean", false);

				List<AutoMappedMap> list = new ArrayList<>();
				list.add(m);

				if (none) {
					return Collections.emptyList();
				}
				none = true;
				return list;
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMappedMap t) throws IOException {
				printer.printRecord(t.valueList());
			}
		};

		Path testCsv = csvCreator.create(file);

		try (BufferedReader reader = Files.newBufferedReader(testCsv, StandardCharsets.UTF_8)) {
			String line = reader.readLine();

			Assert.assertEquals(6, line.split(",").length);
			Assert.assertEquals("string,,  ,,1,false", line);
		}
	}

}

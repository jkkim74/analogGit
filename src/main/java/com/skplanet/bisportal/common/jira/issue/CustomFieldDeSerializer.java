package com.skplanet.bisportal.common.jira.issue;

import java.io.IOException;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

// JIRA Custom Field Serialize
@Slf4j
public class CustomFieldDeSerializer extends JsonDeserializer<Object> {
	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException {
		log.info("Deserialize...");

		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		int nodeSize = node.size();
		for (int i = 0; i < nodeSize; i++) {
			JsonNode child = node.get(i);
			
			if (child == null) {
				log.info("{} th Child node is null", i);
				continue;
			}
			//String
			if (child.isTextual()) {
				Iterator<String> it = child.getFieldNames();
				while (it.hasNext()) {
					String field = it.next();
					log.info("in while loop {}", field);
//					if (field.startsWith("customfield")) {
//
//					}
				}
			}
		}
		return null;
	}

}

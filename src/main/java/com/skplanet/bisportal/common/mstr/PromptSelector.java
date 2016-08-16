package com.skplanet.bisportal.common.mstr;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
import com.microstrategy.web.objects.EnumWebPromptType;
import com.microstrategy.web.objects.WebPrompt;
import com.skplanet.bisportal.model.mstr.MstrRequest;

/**
 * The PromptSelector class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Slf4j
//@Singleton
public class PromptSelector {
	private Map<Integer, PromptHandler> handlerMap;

	public PromptSelector() {
		this.handlerMap = Maps.newHashMap();
		this.handlerMap.put(EnumWebPromptType.WebPromptTypeConstant, new ConstantPromptHandler());
		this.handlerMap.put(EnumWebPromptType.WebPromptTypeElements, new ElementsPromptHandler());
		this.handlerMap.put(EnumWebPromptType.WebPromptTypeObjects, new ObjectsPromptHandler());
		this.handlerMap.put(EnumWebPromptType.WebPromptTypeExpression, new ExpressionPromptHandler());
	}

	public String getHandlePrompt(int promptType, WebPrompt prompt, MstrRequest mstrRequest) throws Exception {
		return this.handlerMap.get(promptType).handlePrompt(prompt, mstrRequest);
	}
}

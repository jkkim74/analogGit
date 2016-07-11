package com.skplanet.bisportal.common.mstr;

import com.microstrategy.web.objects.WebPrompt;
import com.skplanet.bisportal.model.mstr.MstrRequest;

/**
 * The PromptHandler interface.
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
public interface PromptHandler {
	String handlePrompt(WebPrompt prompt, MstrRequest mstrRequest) throws Exception;
}

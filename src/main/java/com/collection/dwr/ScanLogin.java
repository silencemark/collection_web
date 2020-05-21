package com.collection.dwr;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;

public class ScanLogin {

	/**
	 * 页面注册
	 * @param RegId
	 */
	public void onLoad(String RegId) {
		ScriptSession scriptSession = WebContextFactory.get()
				.getScriptSession();
		scriptSession.setAttribute("RegId", RegId);
	}
}

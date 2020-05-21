package com.collection.dwr;

import java.util.Collection;
import java.util.Map;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

public class DwrUtil {

	public static void DwrLogin(final Map<String, Object> data) {

		final String thisRegId = (String) data.get("RegId");

		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			// 实现match方法，条件为真为筛选出来的session
			public boolean match(ScriptSession session) {
				if (session.getAttribute("RegId").equals(thisRegId)) {
					return true;
				}else{
					return false;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();

			public void run() {
				// 设定前台接受消息的方法和参数
				script.appendCall("DwrLogin", data);
				Collection<ScriptSession> sessions = Browser
						.getTargetSessions();
				// 向所有符合条件的页面推送消息
				for (ScriptSession scriptSession : sessions) {
					if (scriptSession.getAttribute("RegId").equals(thisRegId)) {
						scriptSession.addScript(script);
					}
				}
			}
		});
	}
}

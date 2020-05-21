package com.collection.util;

import java.util.UUID;

import org.apache.log4j.Logger;

public class StringUtil {
	private static final Logger LOGGER = Logger.getLogger(StringUtil.class);
	//获取uuid
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
  public static void main(String[] args) {
	LOGGER.debug(StringUtil.getUUID());
}
}

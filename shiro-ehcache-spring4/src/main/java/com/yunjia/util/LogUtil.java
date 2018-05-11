package com.yunjia.util;

public abstract class LogUtil {

	private static boolean isDebug = true;

	public static void i(Object... msgs) {
		if (!isDebug) {
			return;
		}

		for (Object msg : msgs) {
			System.err.print(msg + " ");
		}
		System.err.println();
	}

	public static void f(Object... msgs) {
		if (!isDebug) {
			return;
		}
		String msg = "";
		for (int i = 0; i < msgs.length; i++) {
			if (i == 0) {
				msg = (String) msgs[i];
				continue;
			}
			String m = msgs[i] == null ? "null" : msgs[i].toString();
			msg = msg.replaceFirst("\\?", m);
		}
		System.err.println(msg);
	}
	
}

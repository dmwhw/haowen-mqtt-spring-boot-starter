package com.haowen;

public class ThreadUtils {

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleepOneMillis() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleep500Millis() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleepOneSec() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void Wait(Object obj) {
		try {
			obj.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void Wait(Object obj, int time) {
		try {
			obj.wait(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void WaitWithSyn(Object obj, int time) {
		synchronized (obj) {
			try {
				obj.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void WaitWithSyn(Object obj) {
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void notifyALL(Object obj) {
		try {
			obj.notifyAll();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static void notifyALLWithSyn(Object obj) {
		synchronized (obj) {
			try {
				obj.notifyAll();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public static void Notify(Object obj) {
		try {
			obj.notify();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}

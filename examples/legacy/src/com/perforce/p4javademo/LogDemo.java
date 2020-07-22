/**
 * 
 */
package com.perforce.p4javademo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.perforce.p4java.Log;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.server.callback.ILogCallback;

/**
 * P4Java Log class callback demo class. The output of this
 * demo will (of course) vary greatly depending on the particular
 * circumstances of running it. In any case, the idea is
 * simply to show potential users how to use the log callback --
 * check the log file and see what's in it. Under normal
 * circumstances, there should at least be some INFO messages, but
 * this version may produce nothing at all, or a large number of
 * messages depending on circumstances.
 */

public class LogDemo extends P4JavaDemo {
	
	/**
	 * What we're going to call the log file. Change this
	 * to whatever you want, but be careful of overwriting
	 * existing files...
	 */
	private static final String LOG_FILE_NAME = "/tmp/.p4javademolog";
	
	public static void main(String[] args) {
		LogCallback logCallback = null;
		IOptionsServer server = null;
		
		try {
			logCallback = new LogCallback(new PrintStream(
										new FileOutputStream(new File(LOG_FILE_NAME), true)));
			Log.setLogCallback(logCallback);
			
			server = getOptionsServer(null, null);
			server.disconnect();
			
		} catch (Exception exc) {
			System.err.println(exc.getLocalizedMessage());
		}
	}

	/**
	 * Simple example P4Java ILogCallback implementation.<p>
	 * 
	 * Note that this example does not attempt to prevent concurrent
	 * calls to the various methods here (there's no need in the example
	 * app), but real versions might want to implement synchronized access
	 * or methods (with suitable attention being paid to deadlock and
	 * blocking issues, etc.).
	 */
	
	public static class LogCallback implements ILogCallback {
		
		private LogTraceLevel traceLevel = LogTraceLevel.NONE; // Don't want tracing...
		private PrintStream outStream = null;

		public LogCallback(PrintStream outStream) {
			this.outStream = outStream;
		}
		
		public void setTracelLevel(LogTraceLevel traceLevel) {
			this.traceLevel = traceLevel;
		}

		public LogTraceLevel getTraceLevel() {
			return this.traceLevel;
		}

		public void internalError(String errorString) {
			if (this.outStream != null) {
				
			}
		}

		public void internalException(Throwable thr) {
			if (thr != null) {
				printMessage(thr.getLocalizedMessage(), "EXCEPTION");
				thr.printStackTrace(this.outStream);
			}
		}

		public void internalInfo(String infoString) {
			printMessage(infoString, "INFO");
		}

		public void internalStats(String statsString) {
			printMessage(statsString, "STATS");
		}

		public void internalTrace(LogTraceLevel traceLevel, String traceMessage) {
			// Note: tracing does not work for normal P4Java GA releases, so you
			// should not see any calls to this method unless you have a "special"
			// release...
			
			printMessage(traceMessage, "TRACE");	
		}

		public void internalWarn(String warnString) {
			printMessage(warnString, "WARNING");
		}
		
		private void printMessage(String msg, String pfx) {
			if (msg != null) {
				this.outStream.println(new Date() + " (" + pfx + "): " + msg);
			}
		}
	}
}

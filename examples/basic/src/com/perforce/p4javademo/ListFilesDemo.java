/**
 * 
 */
package com.perforce.p4javademo;

import java.util.List;

import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.RequestException;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.server.callback.IProgressCallback;
import com.perforce.p4java.option.server.GetDepotFilesOptions;

/**
 * Simple P4Java file list and progress callback sample demo.<p>
 * 
 * This example demonstrates a typical pattern used in P4Java
 * to use and retrieve lists of IFileSpec objects; it also
 * demonstrates a very simple progress callback implementation
 * and associated usage.
 */

public class ListFilesDemo extends P4JavaDemo {
	
	public static void main(String[] args) {
		try {		
			IOptionsServer server = getOptionsServer(null, null);
			server.registerProgressCallback(new DemoProgressCallback());
			
			server.setUserName(userName);
			server.login(password);
			System.out.println("Depot files on Perforce server at URI '" + serverUri + "':");
			List<IFileSpec> fileList = server.getDepotFiles(
					FileSpecBuilder.makeFileSpecList("//..."), new GetDepotFilesOptions());
			if (fileList != null) {
				for (IFileSpec fileSpec : fileList) {
					if (fileSpec != null) {
						if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
							System.out.println(formatFileSpec(fileSpec));
						} else {
							System.err.println(fileSpec.getStatusMessage());
						}
					}
				}
			}
			
			server.setCurrentClient(server.getClient(clientName));
			
		} catch (RequestException rexc) {
			System.err.println(rexc.getDisplayString());
			rexc.printStackTrace();
		} catch (Exception exc) {
			System.err.println(exc.getLocalizedMessage());
			exc.printStackTrace();
		}
	}
	
	protected static String formatFileSpec(IFileSpec fileSpec) {
		return fileSpec.getDepotPathString();
	}
	
	/**
	 * A simple demo P4Java progress callback implementation. Real
	 * implementations would probably correlate the key arguments
	 * and associated output, but this version simply puts whatever
	 * it's passed onto standard output with a dash prepended.
	 */
	protected static class DemoProgressCallback implements IProgressCallback {

		public void start(int key) {
			System.out.println("Starting command " + key);
		}

		public void stop(int key) {
			System.out.println("Stopping command " + key);
		}

		public boolean tick(int key, String tickMarker) {
			if (tickMarker != null) {
				System.out.println(key + " - " + tickMarker);
			}
			return true;
		}
	}
}

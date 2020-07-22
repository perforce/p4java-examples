/**
 * 
 */
package com.perforce.p4javademo;

import java.net.URISyntaxException;
import java.util.List;

import com.perforce.p4java.client.IClient;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.exception.RequestException;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.option.client.SyncOptions;

/**
 * Simple P4Java client interface usage demo class.<p>
 * 
 * This class first connects to the Perforce server, then
 * gets a named Perforce client. Once it has a valid client,
 * the program asks the client for a list of all files
 * the Perforce server thinks it has (the "p4 have" command)
 * for the current working directory, then iterates down that
 * list reporting valid files on the list. The current working
 * directory is then sync'd, with the results of that sync
 * being iterated across to print valid files and / or errors and
 * info strings. Both iterations represent very common P4Java
 * usage patterns.<p>
 * 
 * Note what happens if you are not in a working directory covered
 * by the client...
 */

public class ClientUsageDemo extends P4JavaDemo {

	public static void main(String[] args) {
		IOptionsServer server = null;
		try {		
			server = getOptionsServer(null, null);
			
			server.setUserName(userName);
			server.login(password);

			IClient client = server.getClient(clientName);
			if (client != null) {
				server.setCurrentClient(client);
				
				List<IFileSpec> haveList = client.haveList(
									FileSpecBuilder.makeFileSpecList("//..."));
				
				for (IFileSpec fileSpec : haveList) {
					if (fileSpec != null) {
						if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
							System.out.println("have: "
												+ fileSpec.getDepotPath()
												+ "#" + fileSpec.getEndRevision()
												+ " " + fileSpec.getClientPath()
												+ " " + fileSpec.getLocalPath());
						} else {
							System.err.println(fileSpec.getStatusMessage());
						}
					}
				}
				
				List<IFileSpec> syncList = client.sync(
						FileSpecBuilder.makeFileSpecList("//..."), 
						new SyncOptions());
				
				for (IFileSpec fileSpec : syncList) {
					if (fileSpec != null) {
						if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
							System.out.println("sync'd: "
												+ fileSpec.getDepotPath()
												+ "#" + fileSpec.getEndRevision()
												+ " " + fileSpec.getClientPath()
												+ " " + fileSpec.getLocalPath());
						} else {
							System.err.println(fileSpec.getStatusMessage());
						}
					}
				}
			}
		} catch (RequestException rexc) {
			System.err.println(rexc.getDisplayString());
			rexc.printStackTrace();
		} catch (P4JavaException exc) {
			exc.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.disconnect();
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
		}
	}
}

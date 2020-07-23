/**
 *
 */
package com.perforce.exampleproject;

import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.IChangelistSummary;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.option.server.GetChangelistsOptions;
import com.perforce.p4java.server.IOptionsServer;

import java.util.List;

/**
 * Simple P4Java changelist listing sample / demo class.<p>
 * <p>
 * Program retrieves and logs into a Perforce server, then
 * retrieves a maximum of ten changelist summaries as a list from the
 * server for the named user. The summary list is iterated across and used
 * to also get each full changelist object back from the server, allowing
 * the app to print the associated list of files for each changelist.
 */
public class ListChangelistsDemo extends ExampleProject {

	public static void main(String[] args) {
		try {
			// Get perforce server object
			IOptionsServer server = getOptionsServer(null, null);
			if (server == null) {
				System.err.println("Couldn't connect to server");
				return;
			}

			// Log in to server
			server.setUserName(userName);
			server.login(password);

			// Get changelist list
			List<IChangelistSummary> changelistList = server.getChangelists(null, new GetChangelistsOptions().setUserName(userName).setMaxMostRecent(10));
			if (changelistList == null) {
				System.err.println("null returned when getting latest changelists");
				return;
			}

			// Log files in latest changelists
			for (IChangelistSummary changelistSummary : changelistList) {
				if (changelistSummary == null) {
					System.err.println("null in latest changelists");
					continue;
				}

				// Logs key info from changelist
				System.out.println(formatChangelist(changelistSummary));

				// Create changelist object for changelist
				IChangelist changelist = server.getChangelist(changelistSummary.getId());

				// Gets files from changelist
				List<IFileSpec> fileList = changelist.getFiles(true);

				// Logs each file from changelist
				for (IFileSpec fileSpec : fileList) {
					if (fileList == null) {
						System.err.println("null filespec in filespec list");
						continue;
					}
					if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
						System.out.println("\t" + ListFilesDemo.formatFileSpec(fileSpec));
					} else {
						System.err.println(fileSpec.getStatusMessage());
					}
				}
			}
		} catch (P4JavaException jexc) {
			System.err.println(jexc.getLocalizedMessage());
			jexc.printStackTrace();
		} catch (Exception exc) {
			System.err.println(exc.getLocalizedMessage());
			exc.printStackTrace();
		}
	}

	protected static String formatChangelist(IChangelistSummary changelist) {
		return changelist.getId() + " " + changelist.getDate()
				+ " " + changelist.getClientId()
				+ " " + changelist.getDescription();
	}
}

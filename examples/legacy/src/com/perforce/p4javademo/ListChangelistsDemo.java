/**
 * 
 */
package com.perforce.p4javademo;

import java.util.List;

import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.IChangelistSummary;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.option.server.GetChangelistsOptions;

/**
 * Simple P4Java changelist listing sample / demo class.<p>
 * 
 * Program retrieves and logs into a Perforce server, then
 * retrieves a maximum of ten changelist summaries as a list from the
 * server for the named user. The summary list is iterated across and used
 * to also get each full changelist object back from the server, allowing
 * the app to print the associated list of files for each changelist.
 */
public class ListChangelistsDemo extends P4JavaDemo {

	public static void main(String[] args) {
		try {		
			IOptionsServer server = getOptionsServer(null, null);
			
			server.setUserName(userName);
			server.login(password);
				
			List<IChangelistSummary> changelistList = server.getChangelists(null, 
					new GetChangelistsOptions().setUserName(userName).setMaxMostRecent(10));
			
			if (changelistList != null) {
				for (IChangelistSummary changelistSummary : changelistList) {
					System.out.println(formatChangelist(changelistSummary));
					
					IChangelist changelist = server.getChangelist(changelistSummary.getId());
					
					List<IFileSpec> fileList = changelist.getFiles(true);
					
					for (IFileSpec fileSpec : fileList) {
						if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
							System.out.println("\t" + ListFilesDemo.formatFileSpec(fileSpec));
						} else {
							System.err.println(fileSpec.getStatusMessage());
						}
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

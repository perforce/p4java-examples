/**
 * 
 */
package com.perforce.p4javademo;

import java.util.Date;
import java.util.List;

import com.perforce.p4java.client.IClient;
import com.perforce.p4java.core.ChangelistStatus;
import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.FileSpecOpStatus;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.exception.RequestException;
import com.perforce.p4java.impl.generic.core.Changelist;
import com.perforce.p4java.impl.mapbased.server.Server;
import com.perforce.p4java.server.IOptionsServer;

/**
 * Simple P4Java changelist sample / demo code.<p>
 * 
 * This class first retrieves an IOptionsServer object from
 * the ServerFactory (using the associated server URI property),
 * then logs in and retrieves a named Perforce client (name taken from
 * the associated system property). It then creates a new
 * Perforce changelist, opens a sample file for editing, then
 * attempts to submit the changelist. As with all the demos
 * in the P4JavaDemo project, this may or may not work depending
 * on your own circumstances, but it does illustrate some common
 * P4Java usage patterns that should be usable more-or-less as-is.
 */
public class ChangelistDemo extends P4JavaDemo {

	public static void main(String[] args) {
		try {		
			IOptionsServer server = getOptionsServer(null, null);
			
			server.setUserName(userName);
			server.login(password);

			IClient client = server.getClient(clientName);
			if (client != null) {
				server.setCurrentClient(client);
				Changelist changeListImpl = new Changelist(
												IChangelist.UNKNOWN,
												client.getName(),
												userName,
												ChangelistStatus.NEW,
												new Date(),
												"Changelist demo new changelist",
												false,
												(Server) server
						);
				IChangelist changelist = client.createChangelist(changeListImpl);
				EditFilesOptions editFilesOptions = new EditFilesOptions().setChangelistId(changelist.getId());
				
				List<IFileSpec> editList = client.editFiles(
						FileSpecBuilder.makeFileSpecList("SampleText.txt"), editFilesOptions);
				
				if (editList != null) {
					for (IFileSpec fileSpec : editList) {
						if (fileSpec != null) {
							if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
								System.out.println("edited: " + ListFilesDemo.formatFileSpec(fileSpec));
							} else {
								System.err.println(fileSpec.getStatusMessage());
							}
						}
					}
				}
				
				changelist.update();
				List<IFileSpec> submitFiles = changelist.submit(false);
				if (submitFiles != null) {
					for (IFileSpec fileSpec : submitFiles) {
						if (fileSpec != null) {
							if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
								System.out.println("submitted: " + ListFilesDemo.formatFileSpec(fileSpec));
							} else if (fileSpec.getOpStatus() == FileSpecOpStatus.INFO){
								System.out.println(fileSpec.getStatusMessage());
							} else if (fileSpec.getOpStatus() == FileSpecOpStatus.ERROR){
								System.err.println(fileSpec.getStatusMessage());
							}
						}
					}
				}
				
			}
		} catch (RequestException rexc) {
			System.err.println(rexc.getDisplayString());
			rexc.printStackTrace();
		} catch (P4JavaException jexc) {
			System.err.println(jexc.getLocalizedMessage());
			jexc.printStackTrace();
		} catch (Exception exc) {
			System.err.println(exc.getLocalizedMessage());
			exc.printStackTrace();
		}
	}
}

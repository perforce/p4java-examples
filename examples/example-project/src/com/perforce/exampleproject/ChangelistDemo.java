/**
 *
 */
package com.perforce.exampleproject;

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
import com.perforce.p4java.option.client.EditFilesOptions;
import com.perforce.p4java.server.IOptionsServer;

/**
 * Simple P4Java changelist sample / demo code.<p>
 * <p>
 * This class first retrieves an IOptionsServer object from
 * the ServerFactory (using the associated server URI property),
 * then logs in and retrieves a named Perforce client (name taken from
 * the associated system property). It then creates a new
 * Perforce changelist, opens a sample file for editing, then
 * attempts to submit the changelist. As with all the demos
 * in the ExampleProject project, this may or may not work depending
 * on your own circumstances, but it does illustrate some common
 * P4Java usage patterns that should be usable more-or-less as-is.
 */
public class ChangelistDemo extends com.perforce.exampleproject.ExampleProject {

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

			// Get client object
			IClient client = server.getClient(clientName);
			if (client == null) {
				System.err.println("Couldn't find client on server");
				return;
			}

			// Set client on server
			server.setCurrentClient(client);

			// Create changelist object
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

			// Create changelist on perforce server
			IChangelist changelist = client.createChangelist(changeListImpl);

			// Create edit file options
			EditFilesOptions editFilesOptions = new EditFilesOptions().setChangelistId(changelist.getId());

			// Create a list of filespecs for files to be edited
			List<IFileSpec> editList = client.editFiles(FileSpecBuilder.makeFileSpecList("SampleText.txt"), editFilesOptions);
			if (editList == null) {
				System.err.println("Couldn't find any files in the changelist");
				return;
			}

			// Log files that are being edited
			for (IFileSpec fileSpec : editList) {
				if (fileSpec == null) {
					System.err.println("Changelist contained a null filespec");
					continue;
				}
				if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
					System.out.println("edited: " + com.perforce.exampleproject.ListFilesDemo.formatFileSpec(fileSpec));
				} else {
					System.err.println(fileSpec.getStatusMessage());
				}
			}

			// Update changelist so to contain edited files that are added on line 79
			changelist.update();

			// Submit changelist
			List<IFileSpec> submitFiles = changelist.submit(false);
			if (submitFiles == null) {
				System.err.println("Failed to submit changelist");
				return;
			}

			// Log result of changelist submit
			for (IFileSpec fileSpec : submitFiles) {
				if (fileSpec == null) {
					System.err.println("Submitted files contained null filespec");
					continue;
				}
				if (fileSpec.getOpStatus() == FileSpecOpStatus.VALID) {
					System.out.println("submitted: " + ListFilesDemo.formatFileSpec(fileSpec));
				} else if (fileSpec.getOpStatus() == FileSpecOpStatus.INFO) {
					System.out.println(fileSpec.getStatusMessage());
				} else if (fileSpec.getOpStatus() == FileSpecOpStatus.ERROR) {
					System.err.println(fileSpec.getStatusMessage());
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

/**
 * 
 */
package com.perforce.exampleproject;

import java.net.URISyntaxException;
import java.util.List;

import com.perforce.p4java.client.IClient;
import com.perforce.p4java.client.IClientSummary;
import com.perforce.p4java.client.IClientViewMapping;
import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.exception.RequestException;
import com.perforce.p4java.impl.generic.client.ClientView;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.option.server.GetClientsOptions;

/**
 * List all the Perforce clients on a Perforce server, along with
 * each client's view (if it exists).<p>
 * 
 * This demonstrates a common P4Java pattern, i.e. getting a
 * list of summary objects (the client summaries), then iterating
 * across that list to get selected (or all) full versions of
 * the listed objects. It also briefly illustrates the use
 * of the view mapping class(es).
 */

public class ListClientDemo extends ExampleProject {
	
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
			System.out.println("Clients on Perforce server at URI '" + serverUri + "' for user '" + userName + "':");

			// Get client list for all clients owned by user
			List<IClientSummary> clientList = server.getClients(new GetClientsOptions().setUserName(userName));
			if (clientList == null) {
				System.err.println("Client list is null");
				return;
			}

			// Log client info
			for (IClientSummary clientSummary : clientList) {
				if (clientSummary == null) {
					System.err.println("null Client in clients list");
					continue;
				}
				// NOTE: list returns client summaries only; need to get the
				// full client to get the view:

				// Get client from server & log it
				IClient client = server.getClient(clientSummary);
				System.out.println(client.getName() + " " + client.getDescription().trim() + " " + client.getRoot());

				// Get client views
				ClientView clientView = client.getClientView();
				if (clientView == null) {
					System.err.println("Client returned null client view");
					continue;
				}

				// Log client views
				for (IClientViewMapping viewMapping : clientView) {
					if (viewMapping == null) {
						System.err.println("Client view is null");
						continue;
					}
					System.out.println("\t\t" + viewMapping);
				}
			}

		} catch (RequestException rexc) {
			System.err.println(rexc.getDisplayString());
			rexc.printStackTrace();
		} catch (P4JavaException jexc) {
			System.err.println(jexc.getLocalizedMessage());
			jexc.printStackTrace();
		} catch (URISyntaxException uexc) {
			System.err.println(uexc.getLocalizedMessage());
			uexc.printStackTrace();
		}
	}
}

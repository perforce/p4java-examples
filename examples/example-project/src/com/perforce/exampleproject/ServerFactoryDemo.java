/**
 *
 */
package com.perforce.exampleproject.;

import com.perforce.p4java.exception.P4JavaException;
import com.perforce.p4java.exception.RequestException;
import com.perforce.p4java.option.UsageOptions;
import com.perforce.p4java.server.IOptionsServer;
import com.perforce.p4java.server.IServerInfo;
import com.perforce.p4java.server.ServerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Simple P4Java server factory usage demo. Does not require
 * any system properties to be set to run.<p>
 * <p>
 * This example prompts the user for a Perforce server
 * URI (in standard P4Java format), then uses the P4Java
 * ServerFactory class to retrieve a suitable IServer
 * interface onto the Perforce server identified by the URI.
 * The resulting IOptionsServer server interface is then queried
 * for the server's information in a minimalist version of
 * the "p4 info" command.<p>
 * <p>
 * Note the stacked exception / error handling: the use
 * of a separate RequestException catch clause is common
 * due to it being the only P4Java exception that contains
 * useful Perforce server-side generic and severity codes.
 * Note also that the app dies on the first exception instead
 * of trying to continue on...
 */

public class ServerFactoryDemo {

	private static final String PROMPT = "> ";
	private static final String QUIT = "quit";

	public static void main(String[] args) {
		BufferedReader lineReader = new BufferedReader(
				new InputStreamReader(System.in));

		try {
			for (; ; ) {
				System.out.print(PROMPT);
				String serverUriString = lineReader.readLine();

				if ((serverUriString == null) || serverUriString.equalsIgnoreCase(QUIT)) {
					break;
				} else {
					UsageOptions usageOptions = new UsageOptions(null).setProgramName("ExampleProject").setProgramVersion("alpha0.9");
					IOptionsServer server = ServerFactory.getOptionsServer(serverUriString, null, usageOptions);
					server.connect();
					IServerInfo info = server.getServerInfo();

					if (info != null) {
						System.out.println("Info from Perforce server at URI '" + serverUriString + "' for '" + server.getUsageOptions().getProgramName() + "':");
						System.out.println(formatInfo(info));
					}

					if (server != null) {
						server.disconnect();
					}
				}
			}
		} catch (RequestException rexc) {
			System.err.println(rexc.getDisplayString());
			rexc.printStackTrace();
		} catch (P4JavaException exc) {
			System.err.println(exc.getLocalizedMessage());
			exc.printStackTrace();
		} catch (IOException ioexc) {
			System.err.println(ioexc.getLocalizedMessage());
			ioexc.printStackTrace();
		} catch (URISyntaxException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private static String formatInfo(IServerInfo info) {
		return "\tserver address: " + info.getServerAddress() + "\n"
				+ "\tserver version" + info.getServerVersion() + "\n"
				+ "\tclient address: " + info.getClientAddress() + "\n"
				+ "\tclient working directory: " + info.getClientCurrentDirectory() + "\n"
				+ "\tclient name: " + info.getClientName() + "\n"
				+ "\tuser name: " + info.getUserName();
	}
}

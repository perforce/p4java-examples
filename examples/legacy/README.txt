
Introduction 
------------ 
The P4JavaDemo project is a small set of Java apps designed to show
common basic usage patterns for Perforce's P4Java Java SCM API. The
P4JavaDemo project is aimed mostly at developers starting out with
the P4Java API, and assumes experience with Java (JDK 5 or later),
Perforce SCM, and typical Java usage (Java properties, Java template
types, etc.).

The project is designed to be imported as-is into a suitable Eclipse
workspace using P4WSAD or P4V, and contains a number of standalone
Java apps that can be run from the command line or Eclipse's "Run As..."
menu. Alternatively, with a small amount of attention to things
like classpath settings, the demo / sample apps can be compiled and
run from a suitable command line using the standard JDK 6 (or later)
Java VM and environment.

Each application is internally documented with Javadoc comments, and
usage should be fairly straightforward to developers well-versed in
both Java and Perforce. Additional commentary to some of the code
can be found in the companion "P4Java User Guide" available with the
main P4Java distribution, and the main P4Java Javadoc comments provided
with the main P4Java distribution or online.

Support Status
--------------
The P4JavaDemo project is unsupported. The source code is 
provided in the hope that it will be useful to Perforce customers.

Terms of Use
------------
Please see LICENSE.txt for details on the license and terms of use
for this project.

Compatibility
-------------
The P4JavaDemo project requires:

	* Java JDK 6 or later;
	* Perforce P4Java 2009.2 or later;
	* Non-Unicode Perforce Server version 2007.3 or later.

Build and Use
-------------
The P4JavaDemo classes are intended for experienced Java programmers,
so do not contain extensive build and usage instructions; what documentation
does exists is in the class Javodoc and other comments rather than in a
separate document or web page.

Some other notes:

* The results of running these apps depends to a large extent on the
server(s) you connect them to; the intention here is to show you code
and pattern snippets, not to guarantee specific results. However, we
suspect you'll still learn a lot from any errors you see when
running against a Perforce server that does not contain any useful
content.

* Most P4JavaDemo apps are intended to be configured for each run
using Java system properties to set things like the Perforce server
URI, etc. rather than program arguments; the abstract class
com.perforce.P4JavaDemo.P4JavaDemo contains definitions and comments
on the various properties available for use.

* The abstract class P4JavaDemo is the superclass for most P4JavaDemo
apps, but the ServerFactoryDemo class is standalone and self-contained.

* Note that little to none of the sort of error recovery or handling you'd
use in real-world apps is included in the sample code, mostly in order to
keep code sample complexity and size down.

Known Issues
------------
P4JavaDemo apps are intended to show a very basic level of usage (most
suitable for cutting and pasting into "real" apps), and may display
unexpected behavior or issues when used with incompatible JDKs, Perforce
servers, etc.



/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.nabucco.testautomation.engine.proxy.db.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.base.util.FileUtils;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;


/**
 * DatabaseClassloader
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class DatabaseClassloader {

	private static final NBCTestLogger logger = NBCTestLoggingFactory
			.getInstance().getLogger(DatabaseClassloader.class);

	private final File rootPath;
	
	private final Map<String, Driver> driverCache = new HashMap<String, Driver>();
	
	/**
     * Constructs a new classloader loading the given jdbc driver jar from the given root path.
     * 
     * @param rootPath
     *            the path where to find the driver jar
     * 
     */
	public DatabaseClassloader(File rootPath)
			throws ProxyConfigurationException {
		this.rootPath = rootPath;		
	}

	/**
     * Loads the {@link Driver} specified by the given class name.
     * 
     * @param driverJar
     *            the name of the jar file containing the database driver
     * @param driverClass the name of the driver class to be loaded
     * @return the database driver instance
     * @throws ProxyConfigurationException thrown, if the driver could not be loaded
     */
	public Driver getDatabaseDriver(String driverJar, String driverClass)
			throws ProxyConfigurationException {

		// Check Cache for Driver
		if (this.driverCache.containsKey(driverClass)) {
			return this.driverCache.get(driverClass);
		}
		
		File jarFile = new File(rootPath, driverJar);
		URLClassLoader classloader;
		
		logger.info("Loading driver jar: " + jarFile.getAbsolutePath());

		try {
			URL url = FileUtils.toURL(jarFile);
			classloader = new URLClassLoader(new URL[] { url });
		} catch (MalformedURLException ex) {
			throw new ProxyConfigurationException("Could not convert to URL: "
					+ driverJar, ex);
		}
		
		logger.info("Loading driver class: " + driverClass);

		try {
			Driver driver = (Driver) classloader.loadClass(driverClass)
					.newInstance();
			
			// Put Driver into Cache
			this.driverCache.put(driverClass, driver);
			return driver;
		} catch (ClassNotFoundException ex) {
			throw new ProxyConfigurationException(
					"Could not find driver class: " + driverClass, ex);
		} catch (InstantiationException ex) {
			throw new ProxyConfigurationException(
					"Could not instantiate driver class: " + driverClass,
					ex);
		} catch (IllegalAccessException ex) {
			throw new ProxyConfigurationException(
					"Could not access driver class: " + driverClass, ex);
		}
	}

}

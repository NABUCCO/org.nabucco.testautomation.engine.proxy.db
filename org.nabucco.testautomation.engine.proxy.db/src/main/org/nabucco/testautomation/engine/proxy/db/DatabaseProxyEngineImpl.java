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
package org.nabucco.testautomation.engine.proxy.db;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyEngine;
import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.db.classloader.DatabaseClassloader;
import org.nabucco.testautomation.engine.proxy.db.config.DatabaseProxyConfigImpl;
import org.nabucco.testautomation.engine.proxy.db.config.DatabaseProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;

import org.nabucco.testautomation.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * DatabaseProxyEngineImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class DatabaseProxyEngineImpl extends AbstractProxyEngine {

	private static final NBCTestLogger logger = NBCTestLoggingFactory
			.getInstance().getLogger(DatabaseProxyEngineImpl.class);

	private DatabaseClassloader databaseClassloader;

	/**
     * Constructs a new ProxyEngine with {@link SubEngineType.DB}.
     */
	protected DatabaseProxyEngineImpl() {
		super(SubEngineType.DB);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected void initialize() {
		// nothing todo so far
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected void configure(ProxyEngineConfiguration config)
			throws ProxyConfigurationException {
		try {
			DatabaseProxyConfiguration dpc = (DatabaseProxyConfiguration) config;
			databaseClassloader = new DatabaseClassloader(getProxySupport()
					.getDeployPath());
			logger.info("DatabaseProxyEngine configured");
		} catch (ProxyConfigurationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ProxyConfigurationException("Could not configure proxy",
					ex);
		}
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected SubEngine start()
			throws ProxyConfigurationException {
		logger.info("Starting DatabaseProxyEngine ...");
		SubEngine subEngine = null;
		subEngine = new DatabaseSubEngineImpl(this.databaseClassloader);
		logger.info("DatabaseSubEngine created");
		return subEngine;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected void stop() throws ProxyConfigurationException {
		logger.info("Stopping DatabaseProxyEngine ...");
		logger.info("DatabaseProxyEngine stopped");
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected void unconfigure() {
		this.databaseClassloader = null;
		logger.info("DatabaseProxyEngine unconfigured");
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	protected DatabaseProxyConfiguration getProxyConfiguration(
			ProxyConfiguration configuration) {
		DatabaseProxyConfiguration config = new DatabaseProxyConfigImpl(
				getProxySupport().getProxyClassloader(), configuration);
		return config;
	}

}

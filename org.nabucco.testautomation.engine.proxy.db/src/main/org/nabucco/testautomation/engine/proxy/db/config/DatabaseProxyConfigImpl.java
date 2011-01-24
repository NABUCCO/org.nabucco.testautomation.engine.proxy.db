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
package org.nabucco.testautomation.engine.proxy.db.config;

import java.util.Properties;

import org.nabucco.testautomation.engine.proxy.config.AbstractProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.db.config.DatabaseProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.db.config.DatabaseProxyConfigurationType;

import org.nabucco.testautomation.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * DatabaseProxyConfigImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class DatabaseProxyConfigImpl extends AbstractProxyEngineConfiguration
		implements DatabaseProxyConfiguration {

    /**
     * Creates a new instance getting the configuration from
     * the given Properties.
     * 
     * @param the classloader that loaded the proxy
     * @param configuration the technical configuration
     */
	public DatabaseProxyConfigImpl(ClassLoader classloader,
			ProxyConfiguration configuration) {
		super(classloader, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties getDatabaseProperties() {
		Properties props = new Properties();
		props.setProperty("user", getConfigurationValue(DatabaseProxyConfigurationType.DATABASE_USER.getKey())
				.trim());
		props.setProperty("password", getConfigurationValue(
				DatabaseProxyConfigurationType.DATABASE_PASSWORD.getKey()).trim());
		return props;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public String getDatabaseURL() {
		return getConfigurationValue(DatabaseProxyConfigurationType.DATABASE_URL.getKey()).trim();
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public String getDatabaseDriverClass() {
		return getConfigurationValue(DatabaseProxyConfigurationType.DATABASE_DRIVER_CLASS.getKey()).trim();
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public String getDriverJar() {
		return getConfigurationValue(DatabaseProxyConfigurationType.DATABASE_DRIVER_JAR.getKey()).trim();
	}

}

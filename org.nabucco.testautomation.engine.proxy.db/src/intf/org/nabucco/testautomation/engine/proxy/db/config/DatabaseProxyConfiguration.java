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

import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;


/**
 * DatabaseProxyConfiguration
 *
 * @author Steffen Schmidt, PRODYNA AG
 *
 */
public interface DatabaseProxyConfiguration extends ProxyEngineConfiguration {

    /**
     * Gets the configured driver jar.
     * 
     * @return the name of the driver jar
     */
    public String getDriverJar();
    
    /**
     * Gets the configured database specific properties e.g. containing 
     * the username and password.
     * 
     * @return the properties for the database
     */
    public Properties getDatabaseProperties();
    
    /**
     * Gets the configured name of the database driver class.
     * 
     * @return the name of the driver class
     */
    public String getDatabaseDriverClass();
    
    /**
     * Gets the configured databse URL.
     * 
     * @return the URL for the database
     */
    public String getDatabaseURL();
}

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

/**
 * DatabaseProxyConfigurationType
 *
 * @author Steffen Schmidt, PRODYNA AG
 *
 */
public enum DatabaseProxyConfigurationType {

	/**
     * Constant for the property-key for the database driver jar.
     */
    DATABASE_DRIVER_JAR("DATABASE_DRIVER_JAR"),
    
    /**
     * Constant for the property-key for the database driver class.
     */
    DATABASE_DRIVER_CLASS("DATABASE_DRIVER_CLASS"),
    
    /**
     * Constant for the property-key for the database URL.
     */
    DATABASE_URL("DATABASE_URL"),
    
    /**
     * Constant for the property-key for the database user.
     */
    DATABASE_USER("DATABASE_USER"),
    
    /**
     * Constant for the property-key for the database password.
     */
    DATABASE_PASSWORD("DATABASE_PASSWORD");
    
    private String key;
    
    private DatabaseProxyConfigurationType(String key) {
    	this.key = key;    	 
    }
    
    public String getKey() {
    	return this.key;
    }
	
}

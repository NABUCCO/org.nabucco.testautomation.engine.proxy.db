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

import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.db.database.Database;
import org.nabucco.testautomation.engine.proxy.db.sql.SQLStatement;


/**
 * DatabaseEngine
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface DatabaseEngine extends SubEngine {

	 /**
     * Gets an DatabaseEngineOperation to execute a SQL-statement.
     * 
     * @return the DatabaseEngineOperation
     */
    public SQLStatement getSqlStatement();
    
    /**
     * Gets an DatabaseEngineOperation to execute Database-operations.
     * 
     * @return the DatabaseEngineOperation
     */
    public Database getDatabase();
    
}

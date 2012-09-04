/*
 * Copyright 2012 PRODYNA AG
 *
 * Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/eclipse-1.0.php or
 * http://www.nabucco.org/License.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nabucco.testautomation.engine.proxy.db.database;

import java.sql.Connection;

import org.nabucco.testautomation.engine.proxy.ProxyCommand;
import org.nabucco.testautomation.engine.proxy.db.exception.DatabaseCommandException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DatabaseCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface DatabaseCommand extends ProxyCommand {

    public static final String DATABASE_URL = "DATABASE_URL";

    public static final String DATABASE_USER = "DATABASE_USER";

    public static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    public static final String DATABASE_DRIVER_CLASS = "DATABASE_DRIVER_CLASS";

    public static final String DATABASE_DRIVER_JAR = "DATABASE_DRIVER_JAR";

    /**
     * 
     * @param metadata
     * @param properties
     * @return
     * @throws DatabaseCommandException
     */
    public PropertyList execute(Metadata metadata, PropertyList properties) throws DatabaseCommandException;

    /**
     * 
     * @return
     */
    public Connection getConnection();
}

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
package org.nabucco.testautomation.engine.proxy.db.sql;

import org.nabucco.testautomation.engine.proxy.ProxyCommand;
import org.nabucco.testautomation.engine.proxy.db.exception.SQLCommandException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SQLCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface SQLCommand extends ProxyCommand {

    public static final String QUERY = "QUERY";

    public static final String SQL_RESULT = "SQL_RESULT";

    public static final String ROW_COUNT = "ROW_COUNT";

    /**
     * 
     * @param metadata
     * @param properties
     * @return
     * @throws FTPException
     */
    public PropertyList execute(Metadata metadata, PropertyList properties) throws SQLCommandException;

}

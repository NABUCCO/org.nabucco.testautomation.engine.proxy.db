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
package org.nabucco.testautomation.engine.proxy.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.SubEngineOperationType;
import org.nabucco.testautomation.engine.proxy.base.AbstractSubEngine;
import org.nabucco.testautomation.engine.proxy.db.classloader.DatabaseClassloader;
import org.nabucco.testautomation.engine.proxy.db.command.database.DatabaseImpl;
import org.nabucco.testautomation.engine.proxy.db.command.sql.SQLStatementImpl;
import org.nabucco.testautomation.engine.proxy.db.database.Database;
import org.nabucco.testautomation.engine.proxy.db.sql.SQLStatement;
import org.nabucco.testautomation.engine.proxy.exception.SubEngineException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;
import org.nabucco.testautomation.settings.facade.datatype.engine.SubEngineType;

/**
 * DatabaseSubEngineImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DatabaseSubEngineImpl extends AbstractSubEngine implements DatabaseEngine {

    private static final long serialVersionUID = 1L;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            DatabaseSubEngineImpl.class);

    private Database database;

    public DatabaseSubEngineImpl(DatabaseClassloader driverClassloader) {
        super();
        this.database = new DatabaseImpl(driverClassloader);
    }

    @Override
    public ActionResponse executeSubEngineOperation(SubEngineOperationType operationType,
            SubEngineActionType actionType, List<Metadata> metadataList, PropertyList propertyList, TestContext context)
            throws SubEngineException {

        DatabaseEngineOperationType dbEngineOperationType = (DatabaseEngineOperationType) operationType;

        // execute operation
        switch (dbEngineOperationType) {

        case DATABASE:
            return getDatabase().execute(context, propertyList, metadataList, actionType);
        case SQL_STATEMENT:
            return getSqlStatement().execute(context, propertyList, metadataList, actionType);
        default:
            String error = "Unsupported DatabaseEngineOperationType = '" + operationType + "'";
            logger.error(error);
            throw new UnsupportedOperationException(error);
        }
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    @Override
    public SQLStatement getSqlStatement() {
        return new SQLStatementImpl(this.database.getConnection());
    }

    @Override
    public Map<String, SubEngineActionType> getActions() {
        Map<String, SubEngineActionType> actions = new HashMap<String, SubEngineActionType>();

        for (DatabaseActionType action : DatabaseActionType.values()) {
            actions.put(action.toString(), action);
        }
        return actions;
    }

    @Override
    public Map<String, SubEngineOperationType> getOperations() {
        Map<String, SubEngineOperationType> operations = new HashMap<String, SubEngineOperationType>();

        for (DatabaseEngineOperationType operation : DatabaseEngineOperationType.values()) {
            operations.put(operation.toString(), operation);
        }
        return operations;
    }

    @Override
    public SubEngineType getType() {
        return SubEngineType.DB;
    }

}

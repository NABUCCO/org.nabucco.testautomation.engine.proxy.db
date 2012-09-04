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
package org.nabucco.testautomation.engine.proxy.db.command.sql;

import java.sql.Connection;
import java.util.List;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.db.DatabaseActionType;
import org.nabucco.testautomation.engine.proxy.db.exception.SQLCommandException;
import org.nabucco.testautomation.engine.proxy.db.sql.SQLCommand;
import org.nabucco.testautomation.engine.proxy.db.sql.SQLStatement;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SqlStatementImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class SQLStatementImpl implements SQLStatement {

    private static final long serialVersionUID = 1L;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SQLStatementImpl.class);

    private final Connection connection;

    public SQLStatementImpl(Connection con) {
        this.connection = con;
    }

    @Override
    public ActionResponse execute(TestContext context, PropertyList propertyList, List<Metadata> metadataList,
            SubEngineActionType actionType) {

        ActionResponse result = TestResultHelper.createActionResponse();
        SQLCommand command = null;

        if (this.connection == null) {
            result.setErrorMessage("No connection to Database. Execute 'Connect'-Action first");
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        }

        try {
            Metadata metadata = metadataList.get(metadataList.size() - 1);

            if (metadata == null) {
                result.setErrorMessage("No metadata found");
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
            }

            switch ((DatabaseActionType) actionType) {
            case SELECT:
                command = new SelectStatement(this.connection);
                break;
            case INSERT:
                command = new InsertStatement(this.connection);
                break;
            case UPDATE:
                command = new UpdateStatement(this.connection);
                break;
            case DELETE:
                command = new DeleteStatement(this.connection);
                break;
            default:
                result.setErrorMessage("Unsupported DatabaseActionType for SQL: " + actionType);
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
            }

            // Execute SQLCommand
            PropertyList returnProperties = command.execute(metadata, propertyList);

            result.setReturnProperties(returnProperties);
            result.setActionStatus(ActionStatusType.EXECUTED);
            return result;
        } catch (SQLCommandException ex) {
            String errorMessage = "Could not execute " + actionType + "-statement. Cause: " + ex.getMessage();
            logger.error(errorMessage);
            result.setErrorMessage(errorMessage);
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (Exception ex) {
            logger.fatal(ex);
            result.setErrorMessage("Could not execute " + actionType + "-Statement. Cause: " + ex.getMessage());
            result.setActionStatus(ActionStatusType.FAILED);
        } finally {

            if (context.isTracingEnabled() && command != null) {
                result.setActionTrace(command.getActionTrace());
            }
        }
        return result;
    }

}

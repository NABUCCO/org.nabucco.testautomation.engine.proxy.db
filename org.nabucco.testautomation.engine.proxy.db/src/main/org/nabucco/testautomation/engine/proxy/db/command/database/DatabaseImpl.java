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
package org.nabucco.testautomation.engine.proxy.db.command.database;

import java.sql.Connection;
import java.util.List;

import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.db.DatabaseActionType;
import org.nabucco.testautomation.engine.proxy.db.classloader.DatabaseClassloader;
import org.nabucco.testautomation.engine.proxy.db.database.Database;
import org.nabucco.testautomation.engine.proxy.db.database.DatabaseCommand;
import org.nabucco.testautomation.engine.proxy.db.exception.DatabaseCommandException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DatabaseImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DatabaseImpl implements Database {

	private static final long serialVersionUID = 1L;

	private static final NBCTestLogger logger = NBCTestLoggingFactory
			.getInstance().getLogger(DatabaseImpl.class);
	
	private final DatabaseClassloader driverClassloader;
	
	private Connection connection;
	
	public DatabaseImpl(DatabaseClassloader driverClassloader) {
    	this.driverClassloader = driverClassloader;
	}
	
	@Override
	public Connection getConnection() {
		return this.connection;
	}
	
	/**
	 * 
	 */
	@Override
	public ActionResponse execute(TestContext context,
			PropertyList propertyList, List<Metadata> metadataList,
			SubEngineActionType actionType) {

		ActionResponse result = TestResultHelper.createActionResponse();
		DatabaseCommand command = null;
		
		try {
			Metadata metadata = metadataList.get(metadataList.size() - 1);

			if (metadata == null) {
				result.setErrorMessage("No metadata found");
				result.setActionStatus(ActionStatusType.FAILED);
				return result;
			}

			switch ((DatabaseActionType) actionType) {
			case CONNECT:
				command = new ConnectCommand(this.driverClassloader);
				break;
			case DISCONNECT:
				command = new DisconnectCommand(this.connection);
				break;
			default:
				result.setErrorMessage("Unsupported DatabaseActionType for SQL: " + actionType);
                result.setActionStatus(ActionStatusType.FAILED);
                return result;
			}
			
			// Execute DatabaseCommand
        	PropertyList returnProperties = command.execute(metadata, propertyList);
        	this.connection = command.getConnection();
        	
            result.setReturnProperties(returnProperties);
            result.setActionStatus(ActionStatusType.EXECUTED);
            return result;
		} catch (DatabaseCommandException ex) {
			result.setErrorMessage(ex.getMessage());
            result.setActionStatus(ActionStatusType.FAILED);
            return result;
        } catch (Exception ex) {
        	logger.fatal(ex);
			result.setErrorMessage("Could not execute " + actionType
					+ ". Cause: " + ex.toString());
			result.setActionStatus(ActionStatusType.FAILED);
		} finally {
        	
        	if (context.isTracingEnabled() && command != null) {
        		result.setActionTrace(command.getActionTrace());
        	}
        }
		return result;
	}

}

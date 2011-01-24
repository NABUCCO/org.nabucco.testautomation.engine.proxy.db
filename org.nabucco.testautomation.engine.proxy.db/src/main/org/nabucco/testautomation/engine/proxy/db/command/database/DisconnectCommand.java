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
import java.sql.SQLException;

import org.nabucco.testautomation.engine.proxy.db.exception.DatabaseCommandException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * DisconnectCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class DisconnectCommand extends AbstractDatabaseCommand {

	private Connection connection;
	
	protected DisconnectCommand(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public PropertyList execute(Metadata metadata, PropertyList properties)
			throws DatabaseCommandException {

		start();
		disconnect();
		stop();
		
		return null;
	}

	private void disconnect() throws DatabaseCommandException {
		
		if (this.connection == null) {
			info("Already disconnected from database");
			return;
		}
		
		try {
			if (!this.connection.getAutoCommit()) {
				this.connection.commit();
			}
			this.connection.close();
			this.connection = null;
		} catch (SQLException ex) {
			setException(ex);
			error(ex.getMessage());
			throw new DatabaseCommandException("Could not disconnect from Database. Cause: " + ex.getMessage());
		}
		
	}
}

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
package org.nabucco.testautomation.engine.proxy.db.command.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.db.exception.SQLCommandException;

import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * UpdateStatement
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class UpdateStatement extends AbstractSQLCommand {

	/**
	 * 
	 * @param connection
	 */
	UpdateStatement(Connection connection) {
		super(connection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertyList execute(Metadata metadata, PropertyList propertyList)
			throws SQLCommandException {
		
		String sqlQuery = this.getQuery(metadata, propertyList);
		this.info("Preparing SQL-Statement: " + sqlQuery);

		try {

			PreparedStatement statement = this.getConnection().prepareStatement(sqlQuery);
			this.setValues(statement, propertyList);
			
			this.start();
			int row_count = statement.executeUpdate();
			this.stop();
			
			PropertyList resultProperties = this.createResultProperty(row_count);
			this.setResponse(row_count + " row(s) affected in " + (getStop() - getStart()) + " ms");

			if (!getConnection().getAutoCommit()) {
				this.getConnection().commit();
			}
			return resultProperties;
		} catch (SQLException ex) {
			this.setException(ex);
			throw new SQLCommandException(ex.getMessage(), ex);
		}
	}
	
	private PropertyList createResultProperty(int row_count) {

		PropertyList resultProperties = PropertyHelper.createPropertyList(SQL_RESULT);
		IntegerProperty rowCountProperty = PropertyHelper.createIntegerProperty(ROW_COUNT, row_count);
		PropertyHelper.add(rowCountProperty, resultProperties);
		return resultProperties;
	}

}

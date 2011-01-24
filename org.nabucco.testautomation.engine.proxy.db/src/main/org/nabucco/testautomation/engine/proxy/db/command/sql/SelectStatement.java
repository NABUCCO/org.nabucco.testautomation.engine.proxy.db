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

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.db.exception.SQLCommandException;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.DateProperty;
import org.nabucco.testautomation.facade.datatype.property.DoubleProperty;
import org.nabucco.testautomation.facade.datatype.property.LongProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SelectStatement
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class SelectStatement extends AbstractSQLCommand {

	/**
	 * 
	 * @param connection
	 */
	SelectStatement(Connection connection) {
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
			PreparedStatement statement = getConnection().prepareStatement(sqlQuery);
			setValues(statement, propertyList);
			
			this.start();
			ResultSet rs = statement.executeQuery();
			this.stop();
			
			PropertyList resultProperties = createResultProperty(rs);

			if (resultProperties != null) {
				this.setResponse(resultProperties.getPropertyList().size()
						+ " row(s) selected in " + (getStop() - getStart()) + " ms");
			}

			if (!getConnection().getAutoCommit()) {
				this.getConnection().commit();
			}
			return resultProperties;
		} catch (SQLException ex) {
			this.setException(ex);
			throw new SQLCommandException(ex.getMessage(), ex);
		}
	}

	private PropertyList createResultProperty(ResultSet rs) throws SQLException, SQLCommandException {

		PropertyList resultProperties = PropertyHelper.createPropertyList(SQL_RESULT);

		int columns = rs.getMetaData().getColumnCount();
		int[] columnTypes = new int[columns + 1];
		String[] columnNames = new String[columns + 1];
		
		for (int i = 1; i <= columns; i++) {
			columnTypes[i] = rs.getMetaData().getColumnType(i);
			columnNames[i] = rs.getMetaData().getColumnName(i);
		}
		
		int result = 0;
		
		while (rs.next()) {
			PropertyList resultProperty = PropertyHelper.createPropertyList(""
					+ ++result);
			PropertyHelper.add(resultProperty, resultProperties);

			for (int i = 1; i <= columns; i++) {
				Property rowProperty = createProperty(rs.getObject(i),
						columnTypes[i], columnNames[i]);
				if (rowProperty != null) {
					PropertyHelper.add(rowProperty, resultProperty);
				}
			}
		}
		return resultProperties;
	}

	private Property createProperty(Object obj, int type, String name) throws SQLCommandException {

		switch (type) {
		case Types.INTEGER: {
			Property p = null;
			
			if (obj instanceof Long) {
				p = PropertyHelper.createLongProperty(name, (Long) obj);
			} else {			
				p = PropertyHelper.createIntegerProperty(name,
						(Integer) obj);
			}
			return p;
		}
		case Types.BOOLEAN: {
			BooleanProperty p = PropertyHelper.createBooleanProperty(name,
					(Boolean) obj);
			return p;
		}
		case Types.VARCHAR: {
			StringProperty p = PropertyHelper.createStringProperty(name,
					(String) obj);
			return p;
		}
		case Types.CHAR: {
			StringProperty p = PropertyHelper.createStringProperty(name,
					(String) obj);
			return p;
		}
		case Types.DATE: {
			DateProperty p = PropertyHelper
					.createDateProperty(name, (Date) obj);
			return p;
		}
		case Types.NUMERIC:
		case Types.BIGINT: {
			long value;
			
			if (obj instanceof Long) {
				value = (Long) obj;
			} else if (obj instanceof BigInteger) {
				value = ((BigInteger) obj).longValue();
			} else {
				value = Long.parseLong(obj.toString());
			}
			LongProperty p = PropertyHelper.createLongProperty(name, value);
			return p;
		}
		case Types.TIMESTAMP: {
			DateProperty p = PropertyHelper.createDateProperty(name, new Date(
					((Timestamp) obj).getTime()));
			return p;
		}
		case Types.LONGVARCHAR: {
			StringProperty p = PropertyHelper.createStringProperty(name,
					(String) obj);
			return p;
		}
		case Types.DOUBLE:
		case Types.DECIMAL: {
			DoubleProperty p = PropertyHelper.createDoubleProperty(name,
					(Double) obj);
			return p;
		}
		default: {
			throw new SQLCommandException("No mapping for SQL Type: " + type
					+ " available. Skippping column name='" + name + "'");
		}
		}
	}

}

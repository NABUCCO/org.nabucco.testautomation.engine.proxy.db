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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyCommand;
import org.nabucco.testautomation.engine.proxy.db.exception.SQLCommandException;
import org.nabucco.testautomation.engine.proxy.db.sql.SQLCommand;
import org.nabucco.testautomation.property.facade.datatype.BooleanProperty;
import org.nabucco.testautomation.property.facade.datatype.DateProperty;
import org.nabucco.testautomation.property.facade.datatype.NumericProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * AbstractSQLCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public abstract class AbstractSQLCommand extends AbstractProxyCommand implements SQLCommand {

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SQLCommand.class);

    private final Connection connection;

    /**
     * 
     * @param connection
     */
    protected AbstractSQLCommand(Connection connection) {
        this.connection = connection;
    }

    /**
     * 
     * @return
     */
    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * 
     * @return
     */
    protected boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * 
     * @param metadata
     * @param propertyList
     * @return
     * @throws SQLCommandException
     */
    protected String getQuery(Metadata metadata, PropertyList propertyList) throws SQLCommandException {

        Property queryProperty = null;

        // Check ActionProperties first
        queryProperty = PropertyHelper.getFromList(propertyList, PropertyType.SQL);

        if (queryProperty == null) {
            queryProperty = PropertyHelper.getFromList(propertyList, QUERY);
        }

        // Check MetadataProperties
        if (queryProperty == null) {
            queryProperty = PropertyHelper.getFromList(metadata.getPropertyList(), PropertyType.SQL);
        }

        if (queryProperty == null) {
            queryProperty = PropertyHelper.getFromList(metadata.getPropertyList(), QUERY);
        }

        String query = PropertyHelper.toString(queryProperty);

        if (query == null) {
            throw new SQLCommandException("No SQL-Query defined");
        }

        this.setRequest(query);
        return query;
    }

    /**
     * @param pst
     * @param propertyList
     * @throws SQLException
     * @throws SQLCommandException
     */
    protected void setValues(PreparedStatement pst, PropertyList propertyList) throws SQLException, SQLCommandException {

        for (int i = 0; i < propertyList.getPropertyList().size(); i++) {
            Property p = propertyList.getPropertyList().get(i).getProperty();

            switch (p.getType()) {
            case BOOLEAN:
                pst.setBoolean(i + 1, ((BooleanProperty) p).getValue().getValue());
                break;
            case DATE:
                pst.setDate(i + 1, new java.sql.Date(((DateProperty) p).getValue().getValue().getTime()));
                break;
            case NUMERIC:
                pst.setBigDecimal(i + 1, ((NumericProperty) p).getValue().getValue());
                break;
            case TEXT:
                pst.setString(i + 1, ((TextProperty) p).getValue().getValue());
                break;
            default:
                throw new SQLCommandException("Unsupported PropertyType '" + p.getType() + "'");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void info(String msg) {
        logger.info(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void debug(String msg) {
        logger.debug(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void error(String msg) {
        logger.error(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void warning(String msg) {
        logger.warning(msg);
    }

}

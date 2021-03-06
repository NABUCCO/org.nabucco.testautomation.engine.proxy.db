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
package org.nabucco.testautomation.engine.proxy.db.command.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.nabucco.testautomation.engine.proxy.db.classloader.DatabaseClassloader;
import org.nabucco.testautomation.engine.proxy.db.exception.DatabaseCommandException;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * ConnectCommand
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class ConnectCommand extends AbstractDatabaseCommand {

    private DatabaseClassloader databaseClassloader;

    private Connection connection;

    protected ConnectCommand(DatabaseClassloader classloader) {
        this.databaseClassloader = classloader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyList execute(Metadata metadata, PropertyList properties) throws DatabaseCommandException {

        // Find connection parameters
        String driverJar = getDatabaseDriverJar(metadata, properties);
        String driverClass = getDatabaseDriverClass(metadata, properties);
        String url = getDatabaseURL(metadata, properties);
        String username = getUsername(metadata, properties);
        String password = getPassword(metadata, properties);
        Properties databaseProperties = getDatabaseProperties(username, password);

        // Load Database-Driver
        Driver driver = loadDriver(driverJar, driverClass);

        // Connect to Database
        start();
        connect(driver, url, databaseProperties);
        stop();

        return createReturnProperties(driverJar, driverClass, url, username, password);
    }

    private PropertyList createReturnProperties(String driverJar, String driverClass, String url, String username,
            String password) {

        PropertyList returnProperties = PropertyHelper.createPropertyList("ConnectionParams");
        PropertyHelper.add(PropertyHelper.createTextProperty(DATABASE_DRIVER_JAR, driverJar), returnProperties);
        PropertyHelper.add(PropertyHelper.createTextProperty(DATABASE_DRIVER_CLASS, driverClass), returnProperties);
        PropertyHelper.add(PropertyHelper.createTextProperty(DATABASE_URL, url), returnProperties);
        PropertyHelper.add(PropertyHelper.createTextProperty(DATABASE_USER, username), returnProperties);
        PropertyHelper.add(PropertyHelper.createTextProperty(DATABASE_PASSWORD, password), returnProperties);
        return returnProperties;
    }

    private Driver loadDriver(String driverJar, String driverClass) throws DatabaseCommandException {

        info("Loading DatabaseDriver '" + driverClass + "' from '" + driverJar + "'");
        try {
            return this.databaseClassloader.getDatabaseDriver(driverJar, driverClass);
        } catch (ProxyConfigurationException ex) {
            setException(ex);
            error(ex.getMessage());
            throw new DatabaseCommandException(ex.getMessage());
        }
    }

    private void connect(Driver driver, String url, Properties databaseProperties) throws DatabaseCommandException {

        info("Connecting to Database-URL: " + url);
        try {
            this.connection = driver.connect(url, databaseProperties);

            if (this.connection == null) {
                throw new DatabaseCommandException("No connection to Database-URL " + url);
            }
            info("Connection established");
        } catch (SQLException ex) {
            setException(ex);
            error(ex.getMessage());
            throw new DatabaseCommandException("Could not connect to Database-URL "
                    + url + ". Cause: " + ex.getMessage());
        }
    }

    private Properties getDatabaseProperties(String username, String password) throws DatabaseCommandException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        return props;
    }

    private String getUsername(Metadata metadata, PropertyList propertyList) throws DatabaseCommandException {

        TextProperty username = null;

        // Check ActionProperties first
        username = (TextProperty) PropertyHelper.getFromList(propertyList, DATABASE_USER);

        // Check MetadataProperties
        if (username == null) {
            username = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), DATABASE_USER);
        }

        if (username == null || username.getValue() == null || username.getValue().getValue() == null) {
            throw new DatabaseCommandException("No Username provided. Expected TextProperty with name: "
                    + DATABASE_USER);
        }
        return username.getValue().getValue();
    }

    private String getPassword(Metadata metadata, PropertyList propertyList) throws DatabaseCommandException {

        TextProperty password = null;

        // Check ActionProperties first
        password = (TextProperty) PropertyHelper.getFromList(propertyList, DATABASE_PASSWORD);

        // Check MetadataProperties
        if (password == null) {
            password = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), DATABASE_PASSWORD);
        }

        if (password == null || password.getValue() == null || password.getValue().getValue() == null) {
            throw new DatabaseCommandException("No Password provided. Expected TextProperty with name: "
                    + DATABASE_PASSWORD);
        }
        return password.getValue().getValue();
    }

    private String getDatabaseURL(Metadata metadata, PropertyList propertyList) throws DatabaseCommandException {

        TextProperty dbUrl = null;

        // Check ActionProperties first
        dbUrl = (TextProperty) PropertyHelper.getFromList(propertyList, DATABASE_URL);

        // Check MetadataProperties
        if (dbUrl == null) {
            dbUrl = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), DATABASE_URL);
        }

        if (dbUrl == null || dbUrl.getValue() == null || dbUrl.getValue().getValue() == null) {
            throw new DatabaseCommandException("No Database-URL provided. Expected TextProperty with name: "
                    + DATABASE_URL);
        }
        return dbUrl.getValue().getValue();
    }

    private String getDatabaseDriverClass(Metadata metadata, PropertyList propertyList) throws DatabaseCommandException {

        TextProperty driverClass = null;

        // Check ActionProperties first
        driverClass = (TextProperty) PropertyHelper.getFromList(propertyList, DATABASE_DRIVER_CLASS);

        // Check MetadataProperties
        if (driverClass == null) {
            driverClass = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), DATABASE_DRIVER_CLASS);
        }

        if (driverClass == null || driverClass.getValue() == null || driverClass.getValue().getValue() == null) {
            throw new DatabaseCommandException("No DriverClass provided. Expected TextProperty with name: "
                    + DATABASE_DRIVER_CLASS);
        }
        return driverClass.getValue().getValue();
    }

    private String getDatabaseDriverJar(Metadata metadata, PropertyList propertyList) throws DatabaseCommandException {

        TextProperty driverJar = null;

        // Check ActionProperties first
        driverJar = (TextProperty) PropertyHelper.getFromList(propertyList, DATABASE_DRIVER_JAR);

        // Check MetadataProperties
        if (driverJar == null) {
            driverJar = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), DATABASE_DRIVER_JAR);
        }

        if (driverJar == null || driverJar.getValue() == null || driverJar.getValue().getValue() == null) {
            throw new DatabaseCommandException("No DriverJar provided. Expected TextProperty with name: "
                    + DATABASE_DRIVER_JAR);
        }
        return driverJar.getValue().getValue();
    }

}

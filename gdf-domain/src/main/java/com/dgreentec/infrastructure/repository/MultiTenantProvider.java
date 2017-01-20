package com.dgreentec.infrastructure.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class MultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

	private static final long serialVersionUID = -477959940549088708L;
	private DataSource dataSource;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean isUnwrappableAs(Class clz) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> clz) {
		return null;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		Map lSettings = serviceRegistry.getService(ConfigurationService.class).getSettings();
		dataSource = (DataSource) lSettings.get("hibernate.connection.datasource");
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		Connection connection = getAnyConnection();
		try {
			logger.debug("Change to schema " + tenantIdentifier);System.out.println("****************\n"+"Change to schema " + tenantIdentifier+"\n***********************");
			try (Statement stm = connection.createStatement()) {
				stm.execute("SET SCHEMA '" + tenantIdentifier + "'");
			}
			logger.debug("SET SCHEMA OK");
		} catch (SQLException e) {
			throw new HibernateException(
					"[getConnection] Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]",
					e);
		}
		return connection;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		try {
			while (!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			throw new HibernateException("Could not close JDBC connection", e);
		}
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		logger.debug("Release connection for " + tenantIdentifier);
		releaseAnyConnection(connection);
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

}

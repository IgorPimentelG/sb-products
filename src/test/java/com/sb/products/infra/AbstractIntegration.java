package com.sb.products.infra;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegration.Initializer.class)
public class AbstractIntegration {
	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");

		private static void startContainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}

		private Map<String, String> createConnection() {
			return Map.of(
			  "spring.datasource.url", mysql.getJdbcUrl(),
			  "spring.datasource.username", mysql.getUsername(),
			  "spring.datasource.password", mysql.getPassword()
			);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public void initialize(ConfigurableApplicationContext context) {
			startContainers();

			ConfigurableEnvironment environment = context.getEnvironment();
			MapPropertySource testContainers = new MapPropertySource(
			  "test-containers",
			  (Map) createConnection()
			);

			environment.getPropertySources().addFirst(testContainers);
		}
	}
}

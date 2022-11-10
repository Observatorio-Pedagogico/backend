package com.obervatorio_pedagogico.backend;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.Testcontainers;

import io.github.wimdeblauwe.testcontainers.cypress.CypressContainer;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTest;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestResults;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestSuite;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BackendApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendApplicationTests.class);

    @LocalServerPort
    private int port;

    @TestFactory
    List<DynamicContainer> runCypressTests() throws InterruptedException, IOException, TimeoutException, java.io.IOException {

        Testcontainers.exposeHostPorts(port);

        try (CypressContainer container = new CypressContainer()) {
            container.withLocalServerPort(port);
            //container.withBrowser("electron");
            container.withBaseUrl("http://host.testcontainers.internal:".concat(Integer.toString(port)).concat("/observatorio-pedagogico/api"));
            container.withSpec("cypress/e2e/sequenced-tests.cy.js");
            container.start();
            CypressTestResults testResults = container.getTestResults();

            return convertToJUnitDynamicTests(testResults);
        }
    }

    @NotNull
    private List<DynamicContainer> convertToJUnitDynamicTests(CypressTestResults testResults) {
        List<DynamicContainer> dynamicContainers = new ArrayList<>();
        List<CypressTestSuite> suites = testResults.getSuites();
        for (CypressTestSuite suite : suites) {
            createContainerFromSuite(dynamicContainers, suite);
        }
        return dynamicContainers;
    }

    private void createContainerFromSuite(List<DynamicContainer> dynamicContainers, CypressTestSuite suite) {
        List<DynamicTest> dynamicTests = new ArrayList<>();
        for (CypressTest test : suite.getTests()) {
            dynamicTests.add(DynamicTest.dynamicTest(test.getDescription(), () -> {
                if (!test.isSuccess()) {
                    LOGGER.error(test.getErrorMessage());
                    LOGGER.error(test.getStackTrace());
                }
                assertTrue(test.isSuccess());
            }));
        }
        dynamicContainers.add(DynamicContainer.dynamicContainer(suite.getTitle(), dynamicTests));
    }

}

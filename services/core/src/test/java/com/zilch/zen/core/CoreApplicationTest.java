package com.zilch.zen.core;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import testutils.IntegrationBaseTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class CoreApplicationTest extends IntegrationBaseTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // expect
        assertNotNull(applicationContext);
        assertTrue(applicationContext.containsBean("coreApplication"));
    }

}

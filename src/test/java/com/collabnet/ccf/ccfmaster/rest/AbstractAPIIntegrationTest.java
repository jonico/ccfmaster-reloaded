package com.collabnet.ccf.ccfmaster.rest;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/applicationContext.xml",
        "classpath*:/META-INF/spring/applicationContext-test-contentresolver.xml",
        "classpath*:/META-INF/spring/applicationContext-test-ccfruntimeproperties.xml" })
/*
 * Was: extends AbstractTransactionalJUnit4SpringContextTests. That class is JUnit 4 only
 * (it is annotated @RunWith(SpringJUnit4ClassRunner.class) via its superclass) and
 * deprecated for removal in Spring 6. Its whole contribution here was loading the context
 * and wrapping each test in a rolled-back transaction - none of the 14 subclasses touched
 * its jdbcTemplate, applicationContext or logger members - so @ExtendWith(SpringExtension)
 * plus @Transactional is an exact replacement.
 */
@ExtendWith(SpringExtension.class)
@Transactional
public abstract class AbstractAPIIntegrationTest {

    public static String               ccfAPIUrl = "http://localhost:9090/CCFMaster/api";

    protected static RestTemplate      restTemplate;
    protected static DispatcherServlet servlet;

    @BeforeAll
    public static void initServlet() throws ServletException {
        servlet = new DispatcherServlet();
        servlet.setContextConfigLocation("classpath*:/WEB-INF/spring/webmvc-config-test.xml");
        servlet.init(new MockServletConfig());
        restTemplate = new RestTemplate(
                new MockServletClientHttpRequestFactory(servlet, "CCFMaster"));
    }
}

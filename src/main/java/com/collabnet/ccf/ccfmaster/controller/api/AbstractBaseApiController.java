package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.collabnet.ccf.ccfmaster.util.HttpUtils;

public class AbstractBaseApiController {

    private static final Logger  log = LoggerFactory
                                             .getLogger(AbstractBaseApiController.class);

    @Autowired
    protected HttpServletRequest request;
    protected String             contextPath;
    protected String             contextUrl;

    /**
     * because of Spring Bug https://jira.springsource.org/browse/SPR-3150 we
     * provide this default constructor and rely on autowiring instead of
     * constructor injection to set the request for us.
     * 
     * In addition, the @PostConstruct method setupContextUrl is responsible for
     * setting up instance variables based on the request.
     * 
     * Who needs OO design and final fields when we've got DI and frameworks
     * :-(.
     */
    public AbstractBaseApiController() {

    }

    public AbstractBaseApiController(HttpServletRequest request) {
        this.request = request;
        setupContextUrl();
    }

    /*
      * ---------------------------------------------------------------------------------
      * These @ExceptionHandler methods were dead code at the baseline. Spring 3.0.5's
      * AnnotationMethodHandlerExceptionResolver did not pick up @ExceptionHandler methods
      * inherited from a superclass, so every one of these exceptions fell through to the
      * SimpleMappingExceptionResolver declared in webmvc-config.xml, and *its* statusCodes
      * map is the API's real, observable contract:
      *
      *     .DataRetrievalFailureException       -> resourceNotFound         404
      *     .PermissionDeniedDataAccessException -> accessDenied             403
      *     .AccessDeniedException               -> accessDenied             403
      *     .BadRequestException                 -> badRequest               401
      *     .IllegalArgumentException            -> badRequest               401
      *     .DataAccessException                 -> dataAccessFailure        500
      *
      * Spring 6 *does* find inherited @ExceptionHandler methods, which silently changed
      * every one of those codes - a BadRequestException started answering 400, and a
      * DataRetrievalFailureException 400 instead of 404. The handlers below reproduce the
      * table above exactly. 401 for a bad request is odd, but it is what this API has
      * always answered and what its clients and its tests expect.
      *
      * Each handler also sets the status explicitly before writing anything.
      * @ResponseStatus is applied *after* the handler method returns, and these handlers
      * dump a full stack trace into the response first - more than
      * MockHttpServletResponse's 4096-byte buffer, so the response is already committed and
      * setStatus() is silently dropped. A real servlet container behaves the same way once
      * the buffer has been flushed.
      * ---------------------------------------------------------------------------------
      */
    @ExceptionHandler(value = { BadRequestException.class,
            ConversionFailedException.class, IllegalArgumentException.class })
    @ResponseStatus(UNAUTHORIZED)
    public void badRequest(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        log.debug("handling bad request.", ex);
        ex.printStackTrace(response.getWriter());
    }

    /*
     * MissingPathVariableException is what Spring 5.3+ raises for a path variable that is
     * present but converts to null. The API's show/update/delete methods take the entity
     * itself - @PathVariable("id") Landscape landscape - and rely on IdToEntityConverter
     * calling findLandscape(id), which returns null for an id that does not exist. Spring 3
     * passed that null through and the controller raised DataRetrievalFailureException, so
     * 404 either way.
     */
    @ExceptionHandler(value = { DataRetrievalFailureException.class,
            MissingPathVariableException.class })
    @ResponseStatus(NOT_FOUND)
    public void notFound(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(NOT_FOUND.value());
        log.debug("handling not found.", ex);
        ex.printStackTrace(response.getWriter());
    }

    @ExceptionHandler(value = { DataAccessException.class })
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void dataAccessFailure(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(INTERNAL_SERVER_ERROR.value());
        log.debug("handling data access failure.", ex);
        ex.printStackTrace(response.getWriter());
    }

    @ExceptionHandler(value = { AccessDeniedException.class,
            PermissionDeniedDataAccessException.class })
    @ResponseStatus(FORBIDDEN)
    public void permissionDenied(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(FORBIDDEN.value());
        log.debug("handling permission denied.", ex);
        ex.printStackTrace(response.getWriter());
    }

    @PostConstruct
    public void setupContextUrl() {
        log.debug("setupContextUrl called.");
        Assert.notNull(request, "[Assertion failed] - this argument is required; it must not be null");
        this.contextPath = request.getContextPath();
        this.contextUrl = HttpUtils.buildContextUrl(request);
    }

    protected void setLocationHeader(HttpServletResponse response,
            String contextRelativePath) {
        // FIXME: according to RFC1945, this should be an absolute URL.
        response.setHeader("Location", contextUrl + contextRelativePath);
    }

}
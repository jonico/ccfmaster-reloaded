package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
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
      * Note the explicit setStatus() before anything is written. @ResponseStatus is still
      * declared, but Spring applies it *after* the handler method returns, and these two
      * handlers dump a full stack trace into the response first. A stack trace is well over
      * MockHttpServletResponse's 4096-byte buffer, so the response is already committed by
      * then and setStatus() is silently ignored - the API answered 200 OK to every bad
      * request. This is a latent bug rather than a new one: a real servlet container behaves
      * the same way once the buffer has been flushed. Spring 3.0's
      * AnnotationMethodHandlerExceptionResolver happened to set the status before invoking
      * the handler, which is why it never showed.
      */
    @ExceptionHandler(value = { BadRequestException.class,
            DataAccessException.class, ConversionFailedException.class })
    @ResponseStatus(BAD_REQUEST)
    public void badRequest(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(BAD_REQUEST.value());
        log.debug("handling bad request.", ex);
        ex.printStackTrace(response.getWriter());
    }

    /*
      * The API's show/update/delete methods take the entity itself as the path variable -
      * @PathVariable("id") Landscape landscape - and rely on Spring's IdToEntityConverter
      * calling the static findLandscape(id). When the id does not exist the converter
      * returns null. Spring 3 passed that null straight through, the controller raised
      * DataRetrievalFailureException, and SimpleMappingExceptionResolver turned it into 404.
      * Spring 5.3 started rejecting a null-converted required @PathVariable up front with
      * MissingPathVariableException, which DefaultHandlerExceptionResolver reports as 400.
      * Mapping it back to 404 here keeps the HTTP contract the tests and clients expect;
      * "the id in the path does not resolve to an entity" is a not-found, not a bad request.
      */
    @ExceptionHandler(value = { MissingPathVariableException.class })
    @ResponseStatus(NOT_FOUND)
    public void notFound(Exception ex, HttpServletResponse response)
            throws IOException {
        response.setStatus(NOT_FOUND.value());
        log.debug("handling not found.", ex);
        ex.printStackTrace(response.getWriter());
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
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
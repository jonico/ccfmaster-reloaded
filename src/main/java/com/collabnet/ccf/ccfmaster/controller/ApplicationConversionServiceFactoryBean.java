package com.collabnet.ccf.ccfmaster.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.RooConversionService;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

/**
 * A central place to register application Converters and Formatters.
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    private final class StringToExternalAppConverter implements Converter<String, ExternalApp> {
        @Override
        public ExternalApp convert(String linkId) {
            if (linkId.matches("^prpl\\d+$")) {
                return ExternalApp.findExternalAppsByLinkIdEquals(linkId)
                        .getSingleResult();
            } else {
                return ExternalApp.findExternalApp(Long.valueOf(linkId));
            }
        }
    }

    private final class StringToLandscapeConverter implements Converter<String, Landscape> {
        @Override
        public Landscape convert(String linkId) {
            if (linkId.matches("^plug\\d+$")) {
                return Landscape.findLandscapesByPlugIdEquals(linkId)
                        .getSingleResult();
            } else {
                return Landscape.findLandscape(Long.valueOf(linkId));
            }
        }
    }

    /*
     * Spring 3.1 removed FormattingConversionServiceFactoryBean.installFormatters(), which
     * is the hook this class used to override. afterPropertiesSet() - previously an ITD in
     * ApplicationConversionServiceFactoryBean_Roo_ConversionService - has been pushed in
     * here so that the Roo-generated label converters and the two hand-written ones are
     * both registered, in that order, exactly as before.
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
        installFormatters(getObject());
    }

    protected void installFormatters(FormatterRegistry registry) {
        // Register application converters and formatters
        registry.addConverter(new StringToExternalAppConverter());
        registry.addConverter(new StringToLandscapeConverter());
    }

}

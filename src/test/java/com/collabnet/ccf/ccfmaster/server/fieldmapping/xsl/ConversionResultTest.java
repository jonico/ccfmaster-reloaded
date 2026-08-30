package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import mockit.Mocked;

import org.junit.jupiter.api.Test;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

public class ConversionResultTest {
    @Mocked
    ConversionResult.CustomXsl customXsl;
    @Mocked
    ConversionResult.MapForce  mapForce;
    @Mocked
    MappingRules               mappingRules;

    @Test
    public void badXmlThrows() {
        org.junit.jupiter.api.Assertions.assertThrows(CoreConfigurationException.class, () -> {    
            FieldMappingRule fmr = new FieldMappingRule();
            fmr.setXmlContent("not XML");
            ConversionResult.xmlContent2Element.transform(fmr);
                });
    }

    @Test
    public void customXmlResult() {
        assertNotNull(customXsl);
        ConversionResult cr = new ConversionResult(customXsl);
        assertTrue(cr.customXsl().isSome());
        assertFalse(cr.mapForce().isSome());
        assertFalse(cr.mappingRules().isSome());
    }

    @Test
    public void mapForceResult() {
        assertNotNull(mapForce);
        ConversionResult cr = new ConversionResult(mapForce);
        assertFalse(cr.customXsl().isSome());
        assertTrue(cr.mapForce().isSome());
        assertFalse(cr.mappingRules().isSome());
    }

    @Test
    public void mappingRulesResult() {
        assertNotNull(mappingRules);
        ConversionResult cr = new ConversionResult(mappingRules);
        assertFalse(cr.customXsl().isSome());
        assertFalse(cr.mapForce().isSome());
        assertTrue(cr.mappingRules().isSome());
    }
}

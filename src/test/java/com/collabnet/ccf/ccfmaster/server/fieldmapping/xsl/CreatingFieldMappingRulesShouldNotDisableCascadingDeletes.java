package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
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
public class CreatingFieldMappingRulesShouldNotDisableCascadingDeletes {

    @Autowired
    private FieldMappingDataOnDemand fmdod;
    private FieldMapping             fm;

    @Test
    public void deleteActiveFieldMappingShouldFail() {
        org.junit.jupiter.api.Assertions.assertThrows(JpaSystemException.class, () -> {    
            assertNotNull(fm);
            final RepositoryMappingDirection parent = fm.getParent();
            parent.setActiveFieldMapping(fm);
            parent.merge();
            long id = fm.getId();
            fm.remove();
            fm.flush();
            assertNull(FieldMapping.findFieldMapping(id));
                });
    }

    @Test
    public void deleteFieldMappingRuleShouldFail() {
        org.junit.jupiter.api.Assertions.assertThrows(ObjectRetrievalFailureException.class, () -> {    
            FieldMappingRule fmr = fm.getRules().get(0);
            long id = fmr.getId();
            fmr.remove();
            fmr.flush();
            assertNull(FieldMappingRule.findFieldMappingRule(id));
                });
    }

    @Test
    public void deleteInactiveFieldMapping() {
        assertNotNull(fm);
        final RepositoryMappingDirection parent = fm.getParent();
        assertTrue(parent.getActiveFieldMapping() == null
                || parent.getActiveFieldMapping().getId().equals(fm.getId()), "mapping is active");
        long id = fm.getId();
        fm.remove();
        fm.flush();
        assertNull(FieldMapping.findFieldMapping(id));
    }

    @Test
    public void deleteLandscape() {
        Landscape landscape = fm.getParent().getRepositoryMapping()
                .getExternalApp().getLandscape();
        long id = landscape.getId();
        landscape.remove();
        landscape.flush();
        assertNull(Landscape.findLandscape(id));
    }

    @Test
    public void deleteRepositoryMappingDirection() {
        RepositoryMappingDirection rmd = fm.getParent();
        long id = rmd.getId();
        rmd.remove();
        rmd.flush();
        assertNull(RepositoryMappingDirection
                .findRepositoryMappingDirection(id));
    }

    @Test
    public void removeFieldMappingRuleFromList() {
        org.junit.jupiter.api.Assertions.assertThrows(CoreConfigurationException.class, () -> {    
            FieldMappingRule fmr = fm.getRules().get(0);
            long id = fmr.getId();
            fm.getRules().remove(0);
            fm.merge();
            assertNull(FieldMappingRule.findFieldMappingRule(id));
                });
    }

    @BeforeEach
    public void setup() {
        fm = fmdod.getNewTransientFieldMapping(42);
        fm.persist();
        fm.getRules().clear();
        fm.setKind(FieldMappingKind.CUSTOM_XSLT);
        FieldMappingDataOnDemand.processMappingKind(fm);
        //		for (FieldMappingRule rule : fm.getRules()) {
        //			rule.persist();
        //		}
        fm = fm.merge();
        assertEquals(1, fm.getRules().size());
    }
}

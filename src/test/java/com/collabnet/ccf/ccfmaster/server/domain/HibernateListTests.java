package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
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
public class HibernateListTests {

    @Autowired
    FieldMappingDataOnDemand                    fmdod;
    @Autowired
    FieldMappingLandscapeTemplateDataOnDemand   fmltdod;
    @Autowired
    FieldMappingExternalAppTemplateDataOnDemand fmeatdod;
    @Autowired
    FieldMappingValueMapDataOnDemand            fmvmdod;
    private List<FieldMappingValueMap>          fmValueMaps;
    private List<FieldMappingValueMap>          fmltValueMaps;
    private List<FieldMappingValueMap>          fmeatValueMaps;
    private FieldMapping                        fm;
    private FieldMappingLandscapeTemplate       fmlt;
    private FieldMappingExternalAppTemplate     fmeat;
    // used by fiveValueMaps to generate "random" fmvm's
    private int                                 idx = 42;

    @Test
    public void canRemoveAndAddAgainToList() {
        FieldMappingValueMap removed = fm.getValueMaps().remove(0);
        fm.getValueMaps().add(removed);
        fm.merge();
        Assertions.assertNotNull(FieldMappingValueMap
                .findFieldMappingValueMap(removed.getId()));
        for (FieldMappingValueMap fmvm : fmltValueMaps) {
            Assertions.assertTrue(fmlt.getValueMaps()
                    .contains(fmvm), String.format("missing fmltValueMaps[%d]: %s",
                    fmltValueMaps.indexOf(fmvm), fmvm));
        }
        for (FieldMappingValueMap fmvm : fmeatValueMaps) {
            Assertions.assertTrue(fmeat.getValueMaps()
                    .contains(fmvm), String.format("missing fmeatValueMaps[%d]: %s",
                    fmeatValueMaps.indexOf(fmvm), fmvm));
        }
    }

    @Test
    public void removeDoesNotDeleteOtherValueMaps() {
        FieldMappingValueMap removed = fm.getValueMaps().remove(0);
        fm.merge();
        Assertions.assertNull(FieldMappingValueMap.findFieldMappingValueMap(removed
                .getId()));
        for (FieldMappingValueMap fmvm : fmltValueMaps) {
            Assertions.assertTrue(fmlt.getValueMaps()
                    .contains(fmvm), String.format("missing fmltValueMaps[%d]: %s",
                    fmltValueMaps.indexOf(fmvm), fmvm));
        }
        for (FieldMappingValueMap fmvm : fmeatValueMaps) {
            Assertions.assertTrue(fmeat.getValueMaps()
                    .contains(fmvm), String.format("missing fmeatValueMaps[%d]: %s",
                    fmeatValueMaps.indexOf(fmvm), fmvm));
        }
        //		Assertions.assertEquals(fmltValueMaps, fmlt.getValueMaps());
        //		Assertions.assertEquals(fmeatValueMaps, fmeat.getValueMaps());
    }

    @BeforeEach
    public void setUp() {
        fmValueMaps = fiveValueMaps();
        fm = fmdod.getRandomFieldMapping();
        fm.getValueMaps().addAll(fmValueMaps);
        fm.merge();

        fmltValueMaps = fiveValueMaps();
        fmlt = fmltdod.getRandomFieldMappingLandscapeTemplate();
        fmlt.getValueMaps().addAll(fmltValueMaps);
        fmlt.merge();

        fmeatValueMaps = fiveValueMaps();
        fmeat = fmeatdod.getRandomFieldMappingExternalAppTemplate();
        fmeat.getValueMaps().addAll(fmeatValueMaps);
        fmeat.merge();
    }

    private ImmutableList<FieldMappingValueMap> fiveValueMaps() {
        Builder<FieldMappingValueMap> builder = ImmutableList.builder();
        for (int i = 0; i < 5; i++, idx++) {
            final FieldMappingValueMap fmvm = fmvmdod
                    .getNewTransientFieldMappingValueMap(idx);
            builder.add(fmvm);
        }
        final ImmutableList<FieldMappingValueMap> res = builder.build();
        return res;
    }
}

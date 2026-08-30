package com.collabnet.ccf.ccfmaster.server.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;
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
public class RepositoryIdBelongsToExternalAppTest {
    @Autowired
    private RepositoryMappingDataOnDemand dod;

    @Test
    public void preventRepositoryidNotEqualsExternalApp() {
        org.junit.jupiter.api.Assertions.assertThrows(CoreConfigurationException.class, () -> {    
    
            RepositoryMapping rMapping = dod.getRandomRepositoryMapping();
            RepositoryMapping modifiedRMapping = new RepositoryMapping();
            modifiedRMapping.setDescription("Modified Description");
            modifiedRMapping.setExternalApp(rMapping.getExternalApp());
            modifiedRMapping.setId(rMapping.getId());
            modifiedRMapping.setParticipantRepositoryId(rMapping
                    .getParticipantRepositoryId());
            modifiedRMapping.setTeamForgeRepositoryId(rMapping
                    .getTeamForgeRepositoryId());
            modifiedRMapping.setVersion(rMapping.getVersion());
            modifiedRMapping.merge();
                });
    }

}

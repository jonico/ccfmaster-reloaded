package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@RooEntity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldMappingRule {
    private String               name;
    private String               description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingRuleType type;

    private String               source;
    private boolean              sourceIsTopLevelAttribute;

    private String               target;
    private boolean              targetIsTopLevelAttribute;

    @Size(max = 1024)
    private String               condition;

    private String               valueMapName;

    @Size(max = 10485760)
    // 10MB should be enough - same as *Config.val
    private String               xmlContent;

}

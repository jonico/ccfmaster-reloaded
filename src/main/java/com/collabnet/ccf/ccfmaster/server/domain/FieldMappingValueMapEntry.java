package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldMappingValueMapEntry {
    public String source;
    public String target;
}
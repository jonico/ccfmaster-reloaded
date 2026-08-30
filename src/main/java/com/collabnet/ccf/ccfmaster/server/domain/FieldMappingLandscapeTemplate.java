package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement(name = "fieldMappingTemplate")
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@RooEntity(finders = {
        "findFieldMappingLandscapeTemplateNamesByParentAndDirection",
        "findFieldMappingLandscapeTemplatesByParentAndNameAndDirection",
        "findFieldMappingLandscapeTemplatesByParent",
        "findFieldMappingLandscapeTemplatesByParentAndDirection",
        "findFieldMappingLandscapeTemplatesByDirection",
        "countFieldMappingLandscapeTemplatesByDirection" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT", "NAME",
        "DIRECTION" }))
public class FieldMappingLandscapeTemplate implements Template<Landscape> {

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    private Landscape                  parent;

    @NotNull
    @Pattern(regexp = "[\\w\\s]+")
    private String                     name;

    @NotNull
    @Enumerated
    private Directions                 direction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingKind           kind;

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingRule>     rules     = new ArrayList<FieldMappingRule>();

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingValueMap> valueMaps = new ArrayList<FieldMappingValueMap>();

    @Override
    public Directions getMappingDirection() {
        return getDirection();
    }

    @Override
    public File getStorageDirectory(File baseDir) {
        Landscape landscape = getParent();
        final File dir = new File(baseDir, String.format(
                "landscape%d/fieldmappings/%s/landscape/%s", landscape.getId(),
                getDirection(), getName()));
        return dir;
    }

    public static long countFieldMappingLandscapeTemplatesByDirection(
            Directions direction) {
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        TypedQuery<Long> q = entityManager()
                .createQuery(
                        "SELECT COUNT(o) FROM FieldMappingLandscapeTemplate o WHERE o.direction = :direction",
                        Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

}

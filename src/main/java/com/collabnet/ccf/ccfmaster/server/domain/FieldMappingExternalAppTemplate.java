package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
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
        "findFieldMappingExternalAppTemplateNamesByParentAndDirection",
        "findFieldMappingExternalAppTemplatesByParentAndNameAndDirection",
        "findFieldMappingExternalAppTemplatesByParentAndDirection",
        "findFieldMappingExternalAppTemplatesByParent" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT", "NAME",
        "DIRECTION" }))
public class FieldMappingExternalAppTemplate implements Template<ExternalApp> {

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(ExternalApp.XmlAdapter.class)
    private ExternalApp                parent;

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
        final ExternalApp ea = getParent();
        final Landscape landscape = ea.getLandscape();
        final File dir = new File(baseDir, String.format(
                "landscape%d/fieldmappings/%s/%s/%s", landscape.getId(),
                getDirection(), ea.getLinkId(), getName()));
        return dir;
    }

    public static long countFieldMappingExternalAppTemplatesByParent(
            ExternalApp externalApp) {
        if (null == externalApp)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(fieldmappingexternalapptemplate) FROM FieldMappingExternalAppTemplate AS fieldmappingexternalapptemplate WHERE fieldmappingexternalapptemplate.parent = :externalApp",
                        Long.class);
        q.setParameter("externalApp", externalApp);
        return q.getSingleResult();
    }

    public static long countFieldMappingExternalAppTemplatesByParentAndDirection(
            ExternalApp externalApp, Directions direction) {
        if (null == externalApp)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        if (null == direction)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(fieldmappingexternalapptemplate) FROM FieldMappingExternalAppTemplate AS fieldmappingexternalapptemplate WHERE fieldmappingexternalapptemplate.parent = :externalApp AND fieldmappingexternalapptemplate.direction = :direction",
                        Long.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplateNamesByParentAndDirection(
            ExternalApp parent, Directions direction) {
        if (null == parent)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        if (null == direction)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent AND o.direction = :direction ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParent(
            ExternalApp parent) {
        if (null == parent)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        return q;
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParentAndDirection(
            ExternalApp parent, Directions direction) {
        if (null == parent)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        if (null == direction)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent AND o.direction = :direction ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParentAndNameAndDirection(
            ExternalApp parent, String name, Directions direction) {
        if (null == parent)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        if (null == name || 0 == name.length())
            throw new IllegalArgumentException("The name argument is required");
        if (null == direction)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent AND o.name = :name AND o.direction = :direction ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        q.setParameter("name", name);
        q.setParameter("direction", direction);
        return q;
    }
}

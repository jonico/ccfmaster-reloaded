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

@RooJavaBean
@XmlRootElement
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT", "SCOPE",
        "NAME" }))
@RooEntity(finders = { "findFieldMappingsByParent",
        "findFieldMappingsByNameAndParentAndScope" })
public class FieldMapping implements Mapping<RepositoryMappingDirection> {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, FieldMapping> {

        @Override
        public Long marshal(FieldMapping v) throws Exception {
            return v.getId();
        }

        @Override
        public FieldMapping unmarshal(Long v) throws Exception {
            return findFieldMapping(v);
        }
    }

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(RepositoryMappingDirection.XmlAdapter.class)
    private RepositoryMappingDirection parent;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingScope          scope;

    @NotNull
    @Pattern(regexp = "[\\w\\s]+")
    private String                     name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingKind           kind;

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingRule>     rules     = new ArrayList<FieldMappingRule>();

    @OneToMany(cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingValueMap> valueMaps = new ArrayList<FieldMappingValueMap>();

    @Override
    public Directions getMappingDirection() {
        return getParent().getDirection();
    }

    @Override
    public File getStorageDirectory(File baseDir) {
        final RepositoryMappingDirection rmd = getParent();
        final Landscape landscape = rmd.getRepositoryMapping().getExternalApp()
                .getLandscape();
        final File dir = new File(baseDir, String.format(
                "landscape%d/fieldmappings/%s/%d/%s", landscape.getId(),
                rmd.getDirection(), rmd.getId(), getName()));
        return dir;
    }

    public static long countFieldMappingsByExternalApp(ExternalApp ea) {
        return entityManager()
                .createQuery(
                        "select count(o) from FieldMapping o where o.parent.repositoryMapping.externalApp = :externalApp",
                        Long.class).setParameter("externalApp", ea)
                .getSingleResult();
    }

    public static long countFieldMappingsByParent(RepositoryMappingDirection rmd) {
        return entityManager()
                .createQuery(
                        "select count(o) from FieldMapping o where o.parent = :parent",
                        Long.class).setParameter("parent", rmd)
                .getSingleResult();
    }

    public static TypedQuery<FieldMapping> findFieldMappingsByExternalApp(
            ExternalApp externalApp) {
        if (null == externalApp)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<FieldMapping> q = em
                .createQuery(
                        "SELECT fieldmapping FROM FieldMapping AS fieldmapping WHERE fieldmapping.parent.repositoryMapping.externalApp = :externalApp ORDER BY fieldmapping.id",
                        FieldMapping.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }
}

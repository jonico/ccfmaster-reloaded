package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "EXTERNAL_APP",
        "participantRepositoryId", "teamForgeRepositoryId" }))
@RooEntity(finders = {
        "findRepositoryMappingsByExternalApp",
        "findRepositoryMappingsByExternalAppAndParticipantRepositoryIdAndTeamForgeRepositoryId" })
public class RepositoryMapping {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, RepositoryMapping> {

        @Override
        public Long marshal(RepositoryMapping v) throws Exception {
            return v.getId();
        }

        @Override
        public RepositoryMapping unmarshal(Long v) throws Exception {
            return findRepositoryMapping(v);
        }
    }

    @NotNull
    private String      description;

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(ExternalApp.XmlAdapter.class)
    private ExternalApp externalApp;

    @NotNull
    @Size(max = 128)
    @Index(name = "teamForgeRepositoryIndex")
    @Pattern(regexp = "^[\\w\\-]+$")
    private String      teamForgeRepositoryId;

    @NotNull
    @Size(max = 128)
    @Index(name = "participantRepositoryIdIndex")
    @Pattern(regexp = "^[^']+$")
    private String      participantRepositoryId;

    public static long countRepositoryMappingsByExternalApp(ExternalApp ea) {
        return entityManager()
                .createQuery(
                        "select count(o) from RepositoryMapping o where o.externalApp = :externalApp",
                        Long.class).setParameter("externalApp", ea)
                .getSingleResult();
    }

    public static long countRepositoryMappingsByLandscape(Landscape landscape) {
        return entityManager()
                .createQuery(
                        "select count(o) from RepositoryMapping o where o.externalApp.landscape = :landscape",
                        Long.class).setParameter("landscape", landscape)
                .getSingleResult();
    }

    public static TypedQuery<RepositoryMapping> findRepositoryMappingsByExternalApp(
            ExternalApp externalApp) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMapping> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMapping AS o WHERE o.externalApp = :externalApp ORDER BY o.id",
                        RepositoryMapping.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }

    public static TypedQuery<RepositoryMapping> findRepositoryMappingsByLandscape(
            Landscape landscape) {
        return entityManager()
                .createQuery(
                        "select o from RepositoryMapping o where o.externalApp.landscape = :landscape ORDER BY o.id",
                        RepositoryMapping.class).setParameter("landscape",
                        landscape);
    }
}

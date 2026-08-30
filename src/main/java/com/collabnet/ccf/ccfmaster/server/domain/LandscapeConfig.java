package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@RooToString
@RooEntity(finders = { "findLandscapeConfigsByLandscape",
        "findLandscapeConfigsByLandscapeAndName" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "landscape",
        "name" }))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LandscapeConfig implements PersistableConfigItem<LandscapeConfig> {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, LandscapeConfig> {

        @Override
        public Long marshal(LandscapeConfig v) throws Exception {
            return v.getId();
        }

        @Override
        public LandscapeConfig unmarshal(Long v) throws Exception {
            return findLandscapeConfig(v);
        }
    }

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    private Landscape landscape;

    @NotNull
    private String    name;

    @NotNull
    @Size(max = 10485760)
    private String    val;

    public static long countLandscapeConfigsByLandscape(Landscape landscape) {
        if (null == landscape)
            throw new IllegalArgumentException(
                    "The landscape argument is required");
        EntityManager em = LandscapeConfig.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(landscapeconfig) FROM LandscapeConfig AS landscapeconfig WHERE landscapeconfig.landscape = :landscape",
                        Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
    }

}

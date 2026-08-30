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
@RooEntity(finders = { "findDirectionConfigsByDirection",
        "findDirectionConfigsByDirectionAndName" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "direction",
        "name" }))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectionConfig implements ConfigItem, PersistableConfigItem<DirectionConfig> {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, DirectionConfig> {

        @Override
        public Long marshal(DirectionConfig v) throws Exception {
            return v.getId();
        }

        @Override
        public DirectionConfig unmarshal(Long v) throws Exception {
            return findDirectionConfig(v);
        }
    }

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Direction.XmlAdapter.class)
    private Direction direction;

    @NotNull
    private String    name;

    @NotNull
    @Size(max = 10485760)
    private String    val;

    public static long countDirectionConfigsByDirection(Direction direction) {
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = DirectionConfig.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(directionconfig) FROM DirectionConfig AS directionconfig WHERE directionconfig.direction = :direction",
                        Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

}

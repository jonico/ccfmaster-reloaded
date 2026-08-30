package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
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
@RooEntity(finders = { "findParticipantConfigsByParticipant",
        "findParticipantConfigsByParticipantAndName" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "participant",
        "name" }))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipantConfig implements ConfigItem, PersistableConfigItem<ParticipantConfig> {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, ParticipantConfig> {

        @Override
        public Long marshal(ParticipantConfig v) throws Exception {
            return v.getId();
        }

        @Override
        public ParticipantConfig unmarshal(Long v) throws Exception {
            return findParticipantConfig(v);
        }
    }

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Participant.XmlAdapter.class)
    private Participant participant;

    @NotNull
    private String      name;

    @NotNull
    @NotBlank
    @Size(max = 10485760)
    private String      val;

    public static long countParticipantConfigsByParticipant(
            Participant participant) {
        if (participant == null)
            throw new IllegalArgumentException(
                    "The participant argument is required");
        EntityManager em = ParticipantConfig.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(participantconfig) FROM ParticipantConfig AS participantconfig WHERE participantconfig.participant = :participant",
                        Long.class);
        q.setParameter("participant", participant);
        return q.getSingleResult();
    }
}

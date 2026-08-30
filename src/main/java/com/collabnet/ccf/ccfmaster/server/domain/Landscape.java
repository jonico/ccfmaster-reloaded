package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
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
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(name = "UNIQUE_PARTICIPANTS", columnNames = {
        "TEAM_FORGE", "PARTICIPANT" }))
@RooEntity(finders = { "findLandscapesByPlugIdEquals" })
public class Landscape {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, Landscape> {

        @Override
        public Long marshal(Landscape v) throws Exception {
            return v.getId();
        }

        @Override
        public Landscape unmarshal(Long v) throws Exception {
            return findLandscape(v);
        }
    }

    /**
     * by convention, TF is the Participant with ID==1. Use this as default.
     * 
     * public Landscape() { super(); try {
     * setTeamForge(Participant.findParticipant(1L)); } catch
     * (PersistenceException e) { log.info(
     * "exception setting teamForge to default value. If this occurs during initialization, it's OK."
     * , e); } }
     */
    @NotNull
    @NotBlank
    private String      name;

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Participant.XmlAdapter.class)
    private Participant teamForge;

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Participant.XmlAdapter.class)
    private Participant participant;

    @NotNull
    @Pattern(regexp = "^plug\\d+$")
    @Column(unique = true)
    private String      plugId;

    public static TypedQuery<Landscape> findLandscapesByTeamForgeOrParticipant(
            Participant participant) {
        if (participant == null)
            throw new IllegalArgumentException(
                    "The participant argument is required");
        EntityManager em = Landscape.entityManager();
        TypedQuery<Landscape> q = em
                .createQuery(
                        "SELECT o FROM Landscape AS o WHERE o.teamForge = :teamForge OR o.participant = :participant",
                        Landscape.class);
        q.setParameter("teamForge", participant);
        q.setParameter("participant", participant);
        return q;
    }
}

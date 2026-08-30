package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import jakarta.validation.constraints.Size;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import jakarta.persistence.Enumerated;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;

@XmlRootElement
@RooJavaBean
@RooToString
@RooEntity
@XmlAccessorType(XmlAccessType.FIELD)
public class Participant {

    public static class XmlAdapter extends jakarta.xml.bind.annotation.adapters.XmlAdapter<Long, Participant> {

        @Override
        public Long marshal(Participant v) throws Exception {
            return v.getId();
        }

        @Override
        public Participant unmarshal(Long v) throws Exception {
            Participant res = findParticipant(v);
            return res;
        }
    }

    @NotNull
    private String     description;

    @NotNull
    @Size(max = 128)
    @Column(unique = true)
    private String     systemId;

    @Size(max = 128)
    private String     encoding;

    @NotNull
    @Enumerated(EnumType.STRING)
    @XmlJavaTypeAdapter(Timezone.XmlAdapter.class)
    private Timezone   timezone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SystemKind systemKind;

    private String     prefix;
}

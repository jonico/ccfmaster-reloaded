package com.collabnet.ccf.ccfmaster.server.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.collabnet.ccf.ccfmaster.config.Version;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Capabilities {

    private Version                      ccfMasterVersion;

    private Version                      coreVersion;

    private final ParticipantSystemKinds participantSystemKinds = new ParticipantSystemKinds();

    public Capabilities() {
    } //Default constructor required for Jaxb

    public Capabilities(Version ccfMasterVersion, Version coreVersion) {
        this.ccfMasterVersion = ccfMasterVersion;
        this.coreVersion = coreVersion;
    }

    @XmlElement(name = "coreversion")
    public Version getCoreVersion() {
        return coreVersion;
    }

    @XmlElement
    public ParticipantSystemKinds getParticipantSystemKinds() {
        return participantSystemKinds;
    }

    @XmlElement(name = "version")
    public Version getVersion() {
        return ccfMasterVersion;
    }

    public void setCoreVersion(Version coreVersion) {
        this.coreVersion = coreVersion;
    }

    public void setVersion(Version version) {
        this.ccfMasterVersion = version;
    }

}

package com.foton.buffer.protocol.type;

import java.util.ArrayList;
import java.util.List;

public class ProtocolProperties {
    private String namespace;
    private VersionProperties versionProperties;
    private ValidProperties validProperties;
    private String mainGroupName;
    private String mainModuleName;
    private List<ModuleProperties> modules = new ArrayList<>();

    public String getNamespace() {
        return namespace;
    }

    public ProtocolProperties setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public VersionProperties getVersionProperties() {
        return versionProperties;
    }

    public ProtocolProperties setVersionProperties(VersionProperties versionProperties) {
        this.versionProperties = versionProperties;
        return this;
    }

    public ValidProperties getValidProperties() {
        return validProperties;
    }

    public ProtocolProperties setValidProperties(ValidProperties validProperties) {
        this.validProperties = validProperties;
        return this;
    }

    public String getMainGroupName() {
        return mainGroupName;
    }

    public ProtocolProperties setMainGroupName(String mainGroupName) {
        this.mainGroupName = mainGroupName;
        return this;
    }

    public String getMainModuleName() {
        return mainModuleName;
    }

    public ProtocolProperties setMainModuleName(String mainModuleName) {
        this.mainModuleName = mainModuleName;
        return this;
    }

    public List<ModuleProperties> getModules() {
        return modules;
    }

    public ProtocolProperties setModules(List<ModuleProperties> modules) {
        this.modules = modules;
        return this;
    }

    public void addModuleProperties(ModuleProperties moduleProperties) {
        modules.add(moduleProperties);
    }
}

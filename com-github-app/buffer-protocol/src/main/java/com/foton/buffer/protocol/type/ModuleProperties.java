package com.foton.buffer.protocol.type;

import com.foton.buffer.protocol.plugin.ModulePlugin;

import java.util.ArrayList;
import java.util.List;

public class ModuleProperties {
    // 模块名称
    private String name;
    // 模块编组
    private String group;
    // 模块组内ID
    private Integer id;
    // ID字段是否参与编解码
    private boolean isIdCodec = false;
    private boolean isIdMask = true;
    private boolean isFlat = false;
    // 模块ID的属性
    private LengthProperties idProperties;
    // 长达字段是否参与编解码
    private boolean isLengthCodec = false;
    private boolean isLengthMask = true;
    // 模块长度属性
    private LengthProperties lengthProperties;
    // 模块成员
    private List<CoderProperties> columnList = new ArrayList<>();
    // 自定义拓展插件
    private ModulePlugin modulePlugin;

    public ModuleProperties addCoderProperties(CoderProperties coderProperties) {
        columnList.add(coderProperties);
        return this;
    }

    public String getName() {
        return name;
    }

    public ModuleProperties setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public ModuleProperties setGroup(String group) {
        this.group = group;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ModuleProperties setId(Integer id) {
        this.id = id;
        return this;
    }

    public boolean isIdCodec() {
        return isIdCodec;
    }

    public ModuleProperties setIdCodec(boolean idCodec) {
        isIdCodec = idCodec;
        return this;
    }

    public LengthProperties getIdProperties() {
        return idProperties;
    }

    public ModuleProperties setIdProperties(LengthProperties idProperties) {
        this.idProperties = idProperties;
        return this;
    }

    public boolean isLengthCodec() {
        return isLengthCodec;
    }

    public ModuleProperties setLengthCodec(boolean lengthCodec) {
        isLengthCodec = lengthCodec;
        return this;
    }

    public LengthProperties getLengthProperties() {
        return lengthProperties;
    }

    public ModuleProperties setLengthProperties(LengthProperties lengthProperties) {
        this.lengthProperties = lengthProperties;
        return this;
    }

    public List<CoderProperties> getColumnList() {
        return columnList;
    }

    public ModuleProperties setColumnList(List<CoderProperties> columnList) {
        this.columnList = columnList;
        return this;
    }

    public boolean isIdMask() {
        return isIdMask;
    }

    public ModuleProperties setIdMask(boolean idMask) {
        isIdMask = idMask;
        return this;
    }

    public boolean isLengthMask() {
        return isLengthMask;
    }

    public ModuleProperties setLengthMask(boolean lengthMask) {
        isLengthMask = lengthMask;
        return this;
    }

    public ModulePlugin getModulePlugin() {
        return modulePlugin;
    }

    public ModuleProperties setModulePlugin(ModulePlugin modulePlugin) {
        this.modulePlugin = modulePlugin;
        return this;
    }


    public boolean isFlat() {
        return isFlat;
    }

    public ModuleProperties setFlat(boolean flat) {
        isFlat = flat;
        return this;
    }
}

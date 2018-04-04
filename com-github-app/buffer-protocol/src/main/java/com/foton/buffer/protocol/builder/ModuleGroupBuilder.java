package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.plugin.ModulePlugin;
import com.foton.buffer.protocol.type.LengthProperties;

public class ModuleGroupBuilder {
    /**
     * 组内模块之间属性相同的部分，提取为组属性，方便模块的构建
     */
    // 模块编组
    private String group;
    // ID字段是否参与编解码
    private Boolean isIdCodec;
    private Boolean isIdMask;
    // 模块ID的属性
    private LengthProperties idProperties;
    // 长达字段是否参与编解码
    private Boolean isLengthCodec;
    private Boolean isLengthMask;
    private Boolean isFlat;
    // 模块长度属性
    private LengthProperties lengthProperties;
    // 自定义拓展插件
    private ModulePlugin modulePlugin;

    public ModuleGroupBuilder(String group) {
        this.group = group;
    }

    public ModuleBuilder buildModule(String name, Integer id) {
        ModuleBuilder m = new ModuleBuilder(group, name, id);
        if (isIdCodec != null)
            m.enableIdCodec();
        if (isIdMask != null) {
            m.enableIdOutput();
        }
        if (idProperties != null) {
            m.setIdProperties(idProperties);
        }
        if (isLengthCodec != null) {
            m.enableLengthCodec();
        }
        if (isFlat != null) {
            m.enableFlat();
        }
        if (isLengthMask != null) {
            m.enableLengthOutput();
        }
        if (lengthProperties != null) {
            m.setLengthProperties(lengthProperties);
        }
        if (modulePlugin != null) {
            m.setPlugin(modulePlugin);
        }
        return m;
    }

    public ModuleBuilder buildFlatModule(Integer id) {
        return buildModule(null, id);
    }

    public ModuleGroupBuilder enableIdCodec() {
        isIdCodec = true;
        return this;
    }

    public ModuleGroupBuilder enableIdOutput() {
        isIdMask = false;
        return this;
    }

    public ModuleGroupBuilder setIdProperties(LengthProperties idProperties) {
        this.idProperties = idProperties;
        return this;
    }

    public ModuleGroupBuilder enableLengthCodec() {
        isLengthCodec = true;
        return this;
    }

    public ModuleGroupBuilder enableFlat() {
        isFlat = true;
        return this;
    }

    public ModuleGroupBuilder enableLengthOutput() {
        isLengthMask = false;
        return this;
    }

    public ModuleGroupBuilder setLengthProperties(LengthProperties lengthProperties) {
        this.lengthProperties = lengthProperties;
        return this;
    }

    public ModuleGroupBuilder setModulePlugin(ModulePlugin modulePlugin) {
        this.modulePlugin = modulePlugin;
        return this;
    }
}

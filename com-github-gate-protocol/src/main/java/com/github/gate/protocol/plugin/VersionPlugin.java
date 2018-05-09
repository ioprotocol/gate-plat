package com.github.gate.protocol.plugin;

import io.netty.buffer.ByteBuf;

public interface VersionPlugin {

    ByteBuf findVersion(ByteBuf byteBuf);

}

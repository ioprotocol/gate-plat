package com.foton.buffer.protocol.plugin;

import io.netty.buffer.ByteBuf;

public interface VersionPlugin {

    ByteBuf findVersion(ByteBuf byteBuf);

}

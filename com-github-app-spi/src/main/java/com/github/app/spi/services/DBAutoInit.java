package com.github.app.spi.services;

import java.io.IOException;

public interface DBAutoInit {
    void init() throws IOException;
}

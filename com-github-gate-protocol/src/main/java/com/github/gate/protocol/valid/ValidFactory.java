package com.github.gate.protocol.valid;

import java.util.HashMap;
import java.util.Map;

public class ValidFactory {
    private Map<String, ValidMethod> validMethodMap = new HashMap();

    private static ValidFactory validFactory = null;

    private ValidFactory() {
        validMethodMap.put("crc16", new CRC16Valid());
        validMethodMap.put("xor", new XORValid());
    }

    public static ValidFactory instance() {
        if(validFactory == null) {
            validFactory = new ValidFactory();
        }
        return validFactory;
    }

    /**
     *
     * @param name
     * @param method
     */
    public void registeValidMethod(String name, ValidMethod method) {
        validMethodMap.put(name, method);
    }

    /**
     *
     * @param name
     * @return
     */
    public ValidMethod getValidMethod(String name) {
        return validMethodMap.get(name);
    }
}

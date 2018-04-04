/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.foton.buffer.protocol.utils;

public class BCDUtil {

    private BCDUtil() {

    }

    /**
     * @param bcd 0x0 ~ 0x99
     * @return
     */
    public static int bcd2value(byte bcd) {
        int value = 0;
        value += (bcd & 0xF0) >>> 4;
        value = value * 10;
        value += bcd & 0x0F;
        return value;
    }

    /**
     * 以大端格式解析bcd的值
     *
     * @param bcd 取值范围 0x0 ~ 0x9999
     * @return
     */
    public static int bcd2value(short bcd) {
        int value = bcd2value((byte) (bcd & 0xFF));
        value = value + bcd2value((byte) ((bcd & 0xFFFF) >>> 8)) * 100;
        return value;
    }

    /**
     * 3 个字节的bcd码值转换
     *
     * @param bcd
     * @return
     */
    public static int mediumBcd2value(int bcd) {
        int value = bcd2value((byte) (bcd & 0xFF));
        value = value + bcd2value((short) ((bcd >>> 8) & 0xFFFF)) * 100;
        return value;
    }

    /**
     * 以大端格式解析bcd的值
     *
     * @param bcd 取值范围 0x0 ~ 0x9999_9999
     * @return
     */
    public static int bcd2value(int bcd) {
        int value1 = bcd2value((short) (bcd & 0xFFFF));
        int value2 = bcd2value((short) (bcd >>> 16));
        return value2 * 10000 + value1;
    }

    /**
     * 以大端格式解析bcd的值
     *
     * @param bcd 取值范围 0x0 ~ 0x9999_9999_9999_9999
     * @return
     */
    public static long bcd2value(long bcd) {
        long value1 = bcd2value((int) (bcd & 0xFFFF_FFFFL)) & 0xFFFF_FFFF;
        long value2 = bcd2value((int) (bcd >>> 32)) & 0xFFFF_FFFF;
        return value2 * 10000_0000L + value1;
    }


    /**
     * 将数值转为bcd码
     *
     * @param value 范围(0 ~ 99)
     * @return
     */
    public static byte value2bcd(byte value) {
        if (value < 0 || value > 99) {
            throw new RuntimeException(String.format("value:%s out of valid range", value));
        }
        return (byte) (((value / 10) << 4) | (value % 10));
    }

    /**
     * 以大端格式将数值转为bcd码
     *
     * @param value 0 ~ 9999
     * @return 2个字节
     */
    public static short value2bcd(short value) {
        int b1 = value2bcd((byte) (value % 100)) & 0xFF;
        int b2 = value2bcd((byte) (value / 100)) & 0xFF;
        return (short) ((b2 << 8) | b1);
    }

    /**
     *
     * @param value
     * @return
     */
    public static int mediumValue2bcd(int value) {
        return value2bcd(value);
    }

    /**
     * 以大端格式将数值转为bcd码
     *
     * @param value 取值范围 0 ~ 9999_9999
     * @return 0x0 ~ 0x9999_9999
     */
    public static int value2bcd(int value) {
        int low = value2bcd((short) (value % 10000)) & 0xFFFF;
        int high = value2bcd((short) (value / 10000)) & 0xFFFF;
        return (high << 16) | low;
    }

    /**
     * 以大端格式将数值转为bcd码
     *
     * @param value 取值范围 0 ~ 9999_9999_9999_9999
     * @return 0x0 ~ 0x9999_9999_9999_9999
     */
    public static long value2bcd(long value) {
        long low = value2bcd((int) (value % 10000_0000L)) & 0xFFFF_FFFFL;
        long high = value2bcd((int) (value / 10000_0000L)) & 0xFFFF_FFFFL;
        return (high << 32) | low;
    }
}

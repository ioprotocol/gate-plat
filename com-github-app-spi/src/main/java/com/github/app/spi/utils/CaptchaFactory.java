package com.github.app.spi.utils;

public class CaptchaFactory {
    private static String WORD_TEMPLATE = "abcdefghijklmnopqrstuvwxyz";
    private static String NUMBER_TEMPLATE = "0123456789";
    private static String WORD_AND_NUMBER_TEMPLATE = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 0: 数字模式
     * 1: 字母模式
     * 2: 数字与字母混合模式
     */
    private CaptchaModel model = CaptchaModel.LETTER;
    /**
     * 验证码长度
     */
    private int length = 6;

    public CaptchaFactory() {
    }

    public CaptchaFactory(CaptchaModel model, int length) {
        this.model = model;
        this.length = length;
    }

    public String next() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            if (model == CaptchaModel.NUMBER) {
                builder.append(NUMBER_TEMPLATE.charAt(getRandom(NUMBER_TEMPLATE.length() - 1)));
            } else if (model == CaptchaModel.LETTER) {
                builder.append(WORD_TEMPLATE.charAt(getRandom(WORD_TEMPLATE.length() - 1)));
            } else if (model == CaptchaModel.VARCHAR) {
                builder.append(WORD_AND_NUMBER_TEMPLATE.charAt(getRandom(WORD_AND_NUMBER_TEMPLATE.length() - 1)));
            }
        }
        return builder.toString();
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    public enum CaptchaModel {
        NUMBER, // 纯数字模式
        LETTER, // 纯字母模式
        VARCHAR // 字母或者数字混合模式
    }
}

package com.in.g.constant;

/**
 * @author Ship
 * @date 2020-04-30 10:08
 */
public enum YesOrNoEnum {

    /**
     * 是
     */
    YES((byte) 1, "是"),

    /**
     * 否
     */
    NO((byte) 2, "否");

    private Byte value;
    private String text;

    YesOrNoEnum(Byte value, String text) {
        this.value = value;
        this.text = text;
    }

    public Byte getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

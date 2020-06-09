package com.svnyoung.pubsub.message;

/**
 * 延迟级别
 * @author sunyang
 * @date 2019/1/10 19:53
 * @version: 1.0
 * @since: 1.0
 **/
public enum DelayLevel {
    LEVEL00(0, 0),
    LEVEL01(1, 1),
    LEVEL02(2, 5),
    LEVEL03(3, 10),
    LEVEL04(4, 30),
    LEVEL05(5, 60),
    LEVEL06(6, 120),
    LEVEL07(7, 180),
    LEVEL08(8, 240),
    LEVEL09(9, 300),
    LEVEL10(10, 360),
    LEVEL11(11, 420),
    LEVEL12(12, 480),
    LEVEL13(13, 540),
    LEVEL14(14, 600),
    LEVEL15(15, 1200),
    LEVEL16(16, 1800),
    LEVEL17(17, 3600),
    LEVEL18(18, 7200);

    private int levelId;
    private int value;

    private DelayLevel(int levelId, int value) {
        this.levelId = levelId;
        this.value = value;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static DelayLevel getByLevelId(int levelId) {
        switch(levelId) {
            case 1:
                return LEVEL01;
            case 2:
                return LEVEL02;
            case 3:
                return LEVEL03;
            case 4:
                return LEVEL04;
            case 5:
                return LEVEL05;
            case 6:
                return LEVEL06;
            case 7:
                return LEVEL07;
            case 8:
                return LEVEL08;
            case 9:
                return LEVEL09;
            case 10:
                return LEVEL10;
            case 11:
                return LEVEL11;
            case 12:
                return LEVEL12;
            case 13:
                return LEVEL13;
            case 14:
                return LEVEL14;
            case 15:
                return LEVEL15;
            case 16:
                return LEVEL16;
            case 17:
                return LEVEL17;
            case 18:
                return LEVEL18;
            default:
                return null;
        }
    }
}

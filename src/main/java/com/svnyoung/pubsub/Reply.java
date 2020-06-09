package com.svnyoung.pubsub;

/**
 *
 * 回复mq消息
 * @author ：sunyang
 * @date ：2018/12/11 11:33
 * @modified By：
 * @version: 1.0
 */
public enum Reply {

    SUCCESS(0,0),
    RECONSUME_01(1,1),
    RECONSUME_02(2,5),
    RECONSUME_03(3,10),
    RECONSUME_04(4,30),
    RECONSUME_05(5, 60),
    RECONSUME_06(6, 120),
    RECONSUME_07(7, 180),
    RECONSUME_08(8, 240),
    RECONSUME_09(9, 300),
    RECONSUME_10(10, 360),
    RECONSUME_11(11, 420),
    RECONSUME_12(12, 480),
    RECONSUME_13(13, 540),
    RECONSUME_14(14, 600),
    RECONSUME_15(15, 1200),
    RECONSUME_16(16, 1800),
    RECONSUME_17(17, 3600),
    RECONSUME_18(18, 7200);

    private int code;
    private int timeout;

    Reply(int code, int timeout) {
        this.code = code;
        this.timeout = timeout;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static Reply getReply(int code) {
        switch(code) {
            case 0:
                return SUCCESS;
            case 1:
                return RECONSUME_01;
            case 2:
                return RECONSUME_02;
            case 3:
                return RECONSUME_03;
            case 4:
                return RECONSUME_04;
            case 5:
                return RECONSUME_05;
            case 6:
                return RECONSUME_06;
            case 7:
                return RECONSUME_07;
            case 8:
                return RECONSUME_08;
            case 9:
                return RECONSUME_09;
            case 10:
                return RECONSUME_10;
            case 11:
                return RECONSUME_11;
            case 12:
                return RECONSUME_12;
            case 13:
                return RECONSUME_13;
            case 14:
                return RECONSUME_14;
            case 15:
                return RECONSUME_15;
            case 16:
                return RECONSUME_16;
            case 17:
                return RECONSUME_17;
            case 18:
                return RECONSUME_18;
            default:
                return null;
        }
    }

}

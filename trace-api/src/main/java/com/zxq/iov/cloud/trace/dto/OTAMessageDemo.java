package com.zxq.iov.cloud.trace.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OTAMessageDemo implements Serializable {
    //上行的OTA请求, 包括主动上行和ApplicationACK请求
    public static final int TYPE_UPLINK = 0;
    //上行Dispatch ACK请求
    public static final int TYPE_UPLINK_DISPATCH_ACK = 1;
    //预留上行Application ACK请求
    public static final int TYPE_UPLINK_APPLICATION_ACK = 2;
    //重试的上行请求
    public static final int TYPE_UPLINK_RETRY = 3;
    //主动下行的请求
    public static final int TYPE_DOWNLINK = 4;
    //针对上行请求的Dispatch ACK请求
    public static final int TYPE_DISPATCH_ACK = 5;
    //针对上行请求的Application ACK请求
    public static final int TYPE_APPLICATION_ACK = 6;


    //消息类型, 是上行还是下行还是dispatch ACK
    private int type;

    //传输类型的标识为0:ASCII, 1:Binary
    private String transportFlag;

    //请求来源IP
    private String sourceIP;

    //消息关键字, 用于连接主动下行请求和TBOX回复的dispatch ack
    private String messageKey;

    //---------dispatch 属性-----------
    //协议版本
    private Integer protocolVersion;

    //application id
    private String aid;

    //message id
    private Integer mid;

    //application data version
    private Integer appVersion;

    //是否需要dispatch ack
    private Boolean ackRequired;

    //test flag是否为测试/分流消息
    private Integer testFlag;

    //传的是tboxCommID还是tboxsn
    private Integer idType;

    //tboxCommID为传输过程中tbox的唯一标示, 即为ota协议里header里的tboxid
    private String tboxCommID;

    //tboxid为后台数据库中的主键id, 进tap后使用主键id和后台应用/服务交互
    private Long tboxID;

    private String tboxSN;

    private Long eventID;

    private Long eventCreationTime;

    private Integer uplinkMessageCounter;

    private Integer downlinkMessageCounter;

    private Integer ackMessageCounter;

    private Integer result;
    //---------dispatch 属性 end -----------

    //存放最原始的ota数据, 解密之前的数据
    private byte[] otaSourceBytes;

    //存放ota数据, 包含header, body和applicationData
    //在解密阶段存放解密前后的ota数据
    //在解码阶段存放解码前(解密后)的数据
    private byte[] otaBytes;

    //解码后的ota请求对象
    private Object otaRequest;

    private byte[] appData;

    public OTAMessageDemo() {
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public Integer getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte[] getOtaBytes() {
        return otaBytes;
    }

    public void setOtaBytes(byte[] otaBytes) {
        this.otaBytes = otaBytes;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(Integer testFlag) {
        this.testFlag = testFlag;
    }

    public Long getTboxID() {
        return tboxID;
    }

    public void setTboxID(Long tboxID) {
        this.tboxID = tboxID;
    }

    public String getTboxSN() {
        return tboxSN;
    }

    public void setTboxSN(String tboxSN) {
        this.tboxSN = tboxSN;
    }

    public Object getOtaRequest() {
        return otaRequest;
    }

    public void setOtaRequest(Object otaRequest) {
        this.otaRequest = otaRequest;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public Integer getUplinkMessageCounter() {
        return uplinkMessageCounter;
    }

    public void setUplinkMessageCounter(Integer uplinkMessageCounter) {
        this.uplinkMessageCounter = uplinkMessageCounter;
    }

    public Boolean getAckRequired() {
        return ackRequired;
    }

    public void setAckRequired(Boolean ackRequired) {
        this.ackRequired = ackRequired;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getDownlinkMessageCounter() {
        return downlinkMessageCounter;
    }

    public void setDownlinkMessageCounter(Integer downlinkMessageCounter) {
        this.downlinkMessageCounter = downlinkMessageCounter;
    }

    public Integer getAckMessageCounter() {
        return ackMessageCounter;
    }

    public void setAckMessageCounter(Integer ackMessageCounter) {
        this.ackMessageCounter = ackMessageCounter;
    }

    public Long getEventCreationTime() {
        return eventCreationTime;
    }

    public void setEventCreationTime(Long eventCreationTime) {
        this.eventCreationTime = eventCreationTime;
    }

    public String getTransportFlag() {
        return transportFlag;
    }

    public void setTransportFlag(String transportFlag) {
        this.transportFlag = transportFlag;
    }

    public byte[] getAppData() {
        return appData;
    }

    public void setAppData(byte[] appData) {
        this.appData = appData;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public byte[] getOtaSourceBytes() {
        return otaSourceBytes;
    }

    public void setOtaSourceBytes(byte[] otaSourceBytes) {
        this.otaSourceBytes = otaSourceBytes;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getTboxCommID() {
        return tboxCommID;
    }

    public void setTboxCommID(String tboxCommID) {
        this.tboxCommID = tboxCommID;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }
}

package com.bolttest.pojo;

/**
 * An ordinary POJO
 * @author liuyu
 * @date 2020/8/3
 */
public class TopoResult {
    public final Long id;
    public final String devId;
    public final Boolean isConn;

    public TopoResult(Long id, String devId, Boolean isConn) {
        this.id = id;
        this.devId = devId;
        this.isConn = isConn;
    }
}

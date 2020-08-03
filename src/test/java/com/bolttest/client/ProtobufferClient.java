package com.bolttest.client;

import com.bolttest.server.BoltProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.neo4j.driver.*;

/**
 * @author liuyu
 * @date 2020/8/3
 */
public class ProtobufferClient {
    private static final Driver driver = GraphDatabase.driver("bolt://localhost:7687");
    private static final Session session = driver.session();
    private static void query() {
        long t = System.currentTimeMillis();
        session.readTransaction(tx -> {
            Result rs = tx.run("call test.topopb()");
            Record row = rs.next();
            byte[] bt = row.get(0).asByteArray();
            try {
                BoltProto.BoltPbArrPb arr = BoltProto.BoltPbArrPb.parseFrom(bt);
                for (BoltProto.BoltPb boltPb : arr.getArrList()) {
                    boltPb.getConn();
                    boltPb.getDevId();
                    boltPb.getId();
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            return tx;
        });

        System.out.println("elapsed time "+(System.currentTimeMillis() - t));
    }



    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            query();
        }
        System.exit(0);
    }
}

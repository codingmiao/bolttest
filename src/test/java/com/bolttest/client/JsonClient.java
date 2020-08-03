package com.bolttest.client;

import com.bolttest.server.BoltProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.neo4j.driver.*;

import java.util.Map;

/**
 * @author liuyu
 * @date 2020/8/3
 */
public class JsonClient {
    private static final Driver driver = GraphDatabase.driver("bolt://localhost:7687");
    private static final Session session = driver.session();
    private static void query() {
        long t = System.currentTimeMillis();
        session.readTransaction(tx -> {
            Result rs = tx.run("call test.topo()");
            rs.forEachRemaining((row)->{
                row.get(0);
                row.get(1);
                row.get(2);
            });
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

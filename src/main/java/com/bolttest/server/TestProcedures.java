package com.bolttest.server;

import com.bolttest.pojo.TopoResult;
import com.bolttest.pojo.TopoResultPb;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * procedures
 *
 * @author liuyu
 * @date 2020/8/3
 */
public class TestProcedures {
    @Context
    public GraphDatabaseService db;

    /**
     * Construct some test data
     *
     * @return
     */
    private List<TopoResult> query() {
        int testNum = 100000;
        List<TopoResult> res = new ArrayList<>(testNum);
        for (int i = 0; i < testNum; i++) {
            TopoResult rs = new TopoResult((long) i, "hello" + i, false);
            res.add(rs);
        }
        return res;
    }

    @Procedure("test.topo")
    @Description("return ordinary POJO")
    public Stream<TopoResult> topo() {
        //query test data and return it
        return query().stream();
    }


    @Procedure("test.topopb")
    @Description("return pbf")
    public Stream<TopoResultPb> topopb() {
        BoltProto.BoltPbArrPb.Builder boltPbArrPbOrBuilder = BoltProto.BoltPbArrPb.newBuilder();
        //query test data, convert it to pbf and return bytes
        List<BoltProto.BoltPb> pbList = new LinkedList<>();
        for (TopoResult topoResult : query()) {
            BoltProto.BoltPb.Builder builder = BoltProto.BoltPb.newBuilder();
            builder.setConn(topoResult.isConn);
            builder.setDevId(topoResult.devId);
            builder.setId(topoResult.id);
            pbList.add(builder.build());
        }
        boltPbArrPbOrBuilder.addAllArr(pbList);
        byte[] bts = boltPbArrPbOrBuilder.build().toByteArray();
        TopoResultPb topoResultPb = new TopoResultPb(bts);
        Stream<TopoResultPb> stream = Stream.of(topoResultPb);
        return stream;
    }
}

package com.bolttest.server;

import org.neo4j.exceptions.KernelException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.api.procedure.GlobalProcedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.server.CommunityBootstrapper;
import org.wowtools.common.utils.ResourcesReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * main class
 *
 * @author liuyu
 * @date 2020/8/3
 */
public class StartUpServer {


    public static final GraphDatabaseService graphDb;

    //start up neo4j
    static {
        Properties p1 = new Properties();
        try {
            p1.load(ResourcesReader.readStream(StartUpServer.class, "neo4j.conf"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        CommunityBootstrapper serverBootstrapper = new CommunityBootstrapper();
        //
        Set<String> cfgKeys = p1.stringPropertyNames();
        Map<String, String> configOverrides = new HashMap<>(cfgKeys.size());
        for (String cfgKey : cfgKeys) {
            configOverrides.put(cfgKey, p1.getProperty(cfgKey));
        }
        String dbPath = p1.getProperty("dbms.directories.data");

        serverBootstrapper.start(new File(dbPath), configOverrides);
        graphDb = serverBootstrapper.getDatabaseManagementService().database("neo4j");
    }

    //register custom procedures
    static {
        Class procedure = TestProcedures.class;
        GlobalProcedures globalProcedures = ((GraphDatabaseAPI) graphDb).getDependencyResolver().resolveDependency(GlobalProcedures.class);
        try {
            globalProcedures.registerProcedure(procedure, true);
            globalProcedures.registerFunction(procedure, true);
            globalProcedures.registerAggregationFunction(procedure, true);
        } catch (KernelException e) {
            throw new RuntimeException("while registering " + procedure, e);
        }
    }

    public static void main(String[] args) {

    }
}

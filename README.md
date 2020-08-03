# bolt test
a demo for issues: https://github.com/neo4j/neo4j/issues/12551

## 综述 overview 
neo4j的bolt使用json格式进行数据传输，显然json是一种低效的方式，这个demo用json和protobuffer做比较，演示json有多慢

The BOLT of Neo4j use JSON for data transmission, obviously JSON is an inefficient way, and this demo shows how slow JSON is by comparing it with the ProtoBuffer

## run this demo
运行src\main\java\com\bolttest\server\StartUpServer.java，将启动一个Neo4j，并注册两个自定义存储过程`test.topo`和`test.topopb`，
这两个存储过程都是返回100000条测试数据，只是test.topopb用protobuffer套了个壳

随后，运行src\test\java\com\bolttest\client下的两个测试类，观察运行耗时，可发现test.topopb快了10倍


Run `src\main\java\com\bolttest\server\StartUpServer.java` will launch a Neo4j and register two custom stored procedures' test.topo 'and' test.topopb ',
Both of these stored procedures return 100,000 test data, but with a shell called ProtoBuffer for `test.topopb`

Then, run the two test classes under `src\test\java\com\bolttest\client`,
and observe that the run takes time, you can find that 'test.topopb' is 10 times faster

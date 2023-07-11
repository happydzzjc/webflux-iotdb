package org.abandon.iot.bean;

import lombok.extern.slf4j.Slf4j;
import org.abandon.iot.entity.IotDataBase;
import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * IotDBService
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/12 15:14
 */
@Slf4j
@Service
public class IotDBService {

    private final SessionPool iotSession;

    public IotDBService(SessionPool iotSession) {
        this.iotSession = iotSession;
    }

    public Mono<List<IotDataBase>> showDatabases(String dbname) {
        List<IotDataBase> iotDataBases = new ArrayList<>();
        SessionDataSetWrapper sessionDataSetWrapper = null;
        try {
            log.info("show databases: database = {}", dbname);
            sessionDataSetWrapper = iotSession.executeQueryStatement("show databases " + dbname + ";");
            while (sessionDataSetWrapper.hasNext()) {
                RowRecord rowRecord = sessionDataSetWrapper.next();
                List<Field> fields = rowRecord.getFields();
                IotDataBase iotDataBase = new IotDataBase(fields.get(0).getStringValue(),
                        fields.get(1).getDataType() == null ? null : fields.get(1).getLongV(),
                        fields.get(2).getDataType() == null ? null : fields.get(2).getLongV(),
                        fields.get(3).getDataType() == null ? null : fields.get(3).getLongV(),
                        fields.get(4).getDataType() == null ? null : fields.get(4).getLongV());
                iotDataBases.add(iotDataBase);
            }
        } catch (IoTDBConnectionException | StatementExecutionException e) {
            log.error("query databases error: {}", e.getMessage(), e);
        } finally {
            if (sessionDataSetWrapper != null) iotSession.closeResultSet(sessionDataSetWrapper);
        }
        return Mono.just(iotDataBases);
    }

    public Mono<String> creatDatabase(String database) {
        try {
            log.info("creat database: database = {}", database);
            iotSession.createDatabase(database);
        } catch (IoTDBConnectionException | StatementExecutionException e) {
            log.error("creat database error: {}", e.getMessage(), e);
            return Mono.just("creat database error: " + e.getMessage());
        }
        return Mono.just("OK");
    }

    public Mono<String> deleteDatabase(String database) {
        try {
            log.info("delete database: database = {}", database);
            iotSession.deleteDatabase(database);
        } catch (IoTDBConnectionException | StatementExecutionException e) {
            log.error("delete database error: {}", e.getMessage(), e);
            return Mono.just("delete database error: " + e.getMessage());
        }
        return Mono.just("OK");
    }

    public Mono<SessionDataSetWrapper> executeCustomerSQL(String customerSQL) {
        SessionDataSetWrapper sessionDataSetWrapper = null;
        try {
            log.info("execute customer sql: sql = {}", customerSQL);
            sessionDataSetWrapper = iotSession.executeQueryStatement(customerSQL);
            log.info("查询结果{}", sessionDataSetWrapper);
        } catch (IoTDBConnectionException | StatementExecutionException e) {
            log.error("execute customer sql error: {}", e.getMessage(), e);
        }
        return Mono.just(sessionDataSetWrapper);
    }
}

package util;

import java.sql.SQLException;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.*;
import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

public class DatabaseChangeListenerService {
    private DatabaseChangeRegistration registration;

    public void listenToTableChanges(String tableName, DataChangeCallback callback) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            OracleConnection oconn = conn.unwrap(OracleConnection.class);

            Properties props = new Properties();
            props.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
            registration = oconn.registerDatabaseChangeNotification(props);

            registration.addListener(event -> {
                for (TableChangeDescription desc : event.getTableChangeDescription()) {
                    for (RowChangeDescription row : desc.getRowChangeDescription()) {
                        callback.onDataChanged(desc.getTableName(), row.getRowid().stringValue(), row.getRowOperation().toString());
                    }
                }
            });

            // 👉 Dùng tableName được truyền vào
            Statement stmt = oconn.createStatement();
            ((OracleStatement) stmt).setDatabaseChangeRegistration(registration);
            stmt.executeQuery("SELECT * FROM " + tableName);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stopListening(Connection conn) {
        try {
            if (registration != null && conn != null && !conn.isClosed()) {
                OracleConnection oconn = conn.unwrap(OracleConnection.class);
                oconn.unregisterDatabaseChangeNotification(registration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Interface callback cho controller sử dụng
    public interface DataChangeCallback {
        void onDataChanged(String tableName, String rowId, String operation);
    }
}

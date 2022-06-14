module com.levaniphoenix.remoteturtle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.eclipse.jetty.websocket.api;
    requires spark.core;
    requires org.json;
    requires java.sql;
    requires javax.websocket.api;
    requires slf4j.api;

    opens com.levaniphoenix.remoteturtle to javafx.fxml;
    exports com.levaniphoenix.remoteturtle;
}
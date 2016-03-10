package cc.caucas.vertx.cdi;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.jdbc.JDBCClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * @author Georgy Davityan.
 */
@ApplicationScoped
public class JDBCProducer {

    @Inject
    private Vertx vertx;

    @Produces
    @ApplicationScoped
    public JDBCClient createSQLClient() {
        JsonObject config = vertx.getOrCreateContext().config().getJsonObject("database.connection");
        return JDBCClient.createShared(vertx, config);
    }

}

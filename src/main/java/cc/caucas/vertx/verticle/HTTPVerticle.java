package cc.caucas.vertx.verticle;

import cc.caucas.vertx.verticle.dao.UserDao;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Georgy Davityan.
 */
@ApplicationScoped
public class HTTPVerticle extends AbstractVerticle {

    @Inject
    private Vertx vertx;
    @Inject
    private UserDao userDao;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.requestStream().toObservable()
                .subscribe(request -> userDao.allRaw()
                                .subscribe(data -> {
                                            request.response().putHeader("Content-Type", "application/json");
                                            request.response().end(data);
                                        },
                                        Throwable::printStackTrace),
                        Throwable::printStackTrace);
        server.listen(8080);
    }

}

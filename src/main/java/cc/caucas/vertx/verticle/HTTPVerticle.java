package cc.caucas.vertx.verticle;

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

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.requestStream().toObservable()
                .subscribe(request -> {
                    request.response().end("Hello World!");
                });
        server.listen(8080);
    }

}

package cc.caucas.vertx.cdi;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import rx.Observable;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * @author Georgy Davityan.
 */
@ApplicationScoped
public class VertxLauncher {

    private Vertx vertx;

    @Inject
    @Any
    private Instance<Verticle> allDiscoveredVerticles;

    public void initVertx(@Observes @Initialized(ApplicationScoped.class) Object o) {
        this.vertx = Vertx.vertx();

        Observable.from(allDiscoveredVerticles)
                .subscribe(verticle -> {
                    vertx.deployVerticle(verticle);
                });
    }

    @Produces
    @ApplicationScoped
    public io.vertx.rxjava.core.Vertx getVertx() {
        return new io.vertx.rxjava.core.Vertx(vertx);
    }

    @PreDestroy
    public void shutdown() {
        this.vertx.close();
    }
}

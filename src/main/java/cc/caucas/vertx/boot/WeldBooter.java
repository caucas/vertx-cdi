package cc.caucas.vertx.boot;

import org.jboss.weld.environment.se.Weld;

/**
 * @author Georgy Davityan.
 */
public class WeldBooter {

    public static void main(String[] args) {
        Weld weld = new Weld();
        weld.initialize();
    }
}

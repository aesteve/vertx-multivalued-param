import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TestValues {

    Vertx vertx;
    HttpServer server;


    @Before
    public void createServer(TestContext context) {
        vertx = Vertx.vertx();
        server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/test").handler(ctx -> {
            ctx.response().putHeader("received", "" + ctx.request().params().getAll("param").size());
            ctx.response().end();
        });
        server.requestHandler(router::accept);
        server.listen(9000, context.asyncAssertSuccess());
    }

    @Test
    public void multiValued(TestContext context) {
        Async async = context.async();
        HttpClient client = vertx.createHttpClient();
        client.getNow(9000, "localhost", "/test?param=value1&param=value2", resp -> {
            System.out.println(resp.getHeader("received"));
            async.complete();
        });
    }

    @After
    public void stopServer(TestContext context) {
        server.close(context.asyncAssertSuccess());
    }



}

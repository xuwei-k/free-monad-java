package free;

import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClients;

public abstract class Http<A> implements _1<Http.z, A> {
  private final String url;
  private final F1<String, A> action;

  protected Http(String url, F1<String, A> action) {
    this.url = url;
    this.action = action;
  }

  static final class z{}

  private static final class Get<A> extends Http<A>{
    private Get(String url, F1<String, A> action) {
      super(url, action);
    }
  }

  private static final class Post<A> extends Http<A>{
    private Post(String url, F1<String, A> action) {
      super(url, action);
    }
  }

  public static <F> Free<Coyoneda<Http.z, ?>, String> get(final String url){
    return Free.liftFC(new Get<>(url, F1.id()));
  }

  public static <F> Free<Coyoneda<Http.z, ?>, String> post(final String url){
    return Free.liftFC(new Post<>(url, F1.id()));
  }

  final static class HttpError{
    final String url;
    final int code;
    final String body;

    HttpError(String url, int code, String body) {
      this.url = url;
      this.code = code;
      this.body = body;
    }
  }

  public static NT<Http.z, Either<HttpError, ?>> apacheInterpreter =
    new NT<z, Either<HttpError, ?>>() {
      @Override
      public <A> _1<Either<HttpError, ?>, A> apply(_1<z, A> fa) {
        final Http<A> http = ((Http<A>)fa);
        final HttpRequestBase request;
        if(http instanceof Get){
          request = new HttpGet(http.url);
        }else if(http instanceof Post){
          request = new HttpPost(http.url);
        }else{
          throw new RuntimeException("no match http method " + http);
        }
        try(final CloseableHttpResponse res = HttpClients.createDefault().execute(request)){
          final int code = res.getStatusLine().getStatusCode();
          final String body = EntityUtils.toString(res.getEntity(), "UTF-8");
          if(code < 300){
            return Either.right(http.action.apply(body));
          }else{
            return Either.left(new HttpError(http.url, code, body));
          }
        }catch(IOException e){
          throw new RuntimeException(e);
        }
      }
    };

  public static final Monad<Free<Coyoneda<Http.z, ?>, ?>> monad = Free.freeCoyonedaMonad();

  private static final String githubRawURL =
    "https://raw.githubusercontent.com/";

  private static String freeMonadJavaRaw(final String file){
    return githubRawURL + "xuwei-k/free-monad-java/master/src/main/java/free/" + file;
  }

  public static Free<Coyoneda<Http.z, ?>, String> sample1 =
    (Free<Coyoneda<z,?>,String>)(
      monad.apply3(
        () -> get(freeMonadJavaRaw("Free.java")),
        () -> get(freeMonadJavaRaw("Coyoneda.java")),
        () -> get(freeMonadJavaRaw("Monad.java")),
        (freeDotJava, coyonedaDotJava, monadDotJava) ->
          freeDotJava + coyonedaDotJava + monadDotJava
      )
    );

}
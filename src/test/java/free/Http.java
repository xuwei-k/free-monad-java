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

  public static NT<Http.z, Id.z> apacheInterpreter =
    new NT<z, Id.z>() {
      @Override
      public <A> _1<Id.z, A> apply(_1<z, A> fa) {
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
          final String body = EntityUtils.toString(res.getEntity(), "UTF-8");
          return new Id<>(http.action.apply(body));
        }catch(IOException e){
          throw new RuntimeException(e);
        }
      }
    };

  public static final Monad<Free<Coyoneda<Http.z, ?>, ?>> monad = Free.freeCoyonedaMonad();

  private static final String githubRawURL =
    "https://raw.githubusercontent.com/";

  private static final String freeMonadJavaRaw(final String file){
    return githubRawURL + "xuwei-k/free-monad-java/master/src/main/java/free/" + file;
  }

  public static Free<Coyoneda<Http.z, ?>, String> sample1 =
    get(freeMonadJavaRaw("Free.java")).flatMap(
      freeDotJava ->
        get(freeMonadJavaRaw("Coyoneda.java")).flatMap(
          coyonedaDotJava ->
            get(freeMonadJavaRaw("Monad.java")).map(
              monadDotJava ->
              freeDotJava + coyonedaDotJava + monadDotJava
            )
        )
    );

}
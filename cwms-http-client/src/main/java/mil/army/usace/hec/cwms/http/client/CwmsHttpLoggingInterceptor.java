package mil.army.usace.hec.cwms.http.client;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

final class CwmsHttpLoggingInterceptor implements Interceptor {

    private final HttpLoggingInterceptor delegate;

    CwmsHttpLoggingInterceptor() {
        this.delegate = new HttpLoggingInterceptor(new CwmsHttpLoggingInterceptorLogger());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return delegate.intercept(chain);
    }

    public void level(HttpLoggingInterceptor.Level level) {
        delegate.level(level);
    }


}

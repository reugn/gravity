package reug.gravity.util;

import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

public class FutureUtils {

    private FutureUtils() {
    }

    public static <T> T get(Future<T> f) {
        T result;
        try {
            result = f.get();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
        return result;
    }
}

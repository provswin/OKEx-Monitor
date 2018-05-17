package cn.a4miles.okex_monitor.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * OKEX 网络访问辅助类
 *
 * @author
 * @create 2018-05-14 下午4:29
 **/
public class OKHttpManager {
    private static OKHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OKHttpManager() {
        OkHttpClient okHttpClient = new OkHttpClient();
//        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxy);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        mOkHttpClient = builder.build();

        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());

        mGson = new Gson();
    }

    public static OKHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpManager();
                }
            }
        }
        return mInstance;
    }

    public static Response getAsyn(String url) throws IOException {
        return getInstance()._getAsyn(url);
    }

    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    /**
     * post body
     *
     * @param url
     * @param json
     * @param callback
     */
    public static void postAsyn(Object tag, String url, String json, final ResultCallback
            callback) {
        getInstance()._postAsyn(tag, url, callback, json);
    }

    /**
     * post body
     *
     * @param url
     * @param json
     * @param callback
     */
    public static void postAsyn(String url, String json, final ResultCallback
            callback) {
        getInstance()._postAsyn(null, url, callback, json);
    }

    /**
     * post body
     *
     * @param url
     * @param json
     * @param callback
     */
    public static void postAsyn(boolean isShowErrorToast, String url, String json, final ResultCallback
            callback) {
        getInstance()._postAsyn(isShowErrorToast, null, url, callback, json);
    }

    /**
     * post token body
     *
     * @param url
     * @param token
     * @param json
     * @param callback
     */
    public static void postAsyn(String url, String token, String json, final ResultCallback callback) {
        postAsyn(url, token, json, true, callback);
    }

    /**
     * post token body
     *
     * @param url
     * @param token
     * @param json
     * @param callback
     */
    public static void postAsyn(String url, String token, String json, boolean debug, final ResultCallback
            callback) {
        if (token == null || url == null || json == null) return;
        getInstance()._postAsyn(url, callback, json, token);
    }

    public static void postAsyn(boolean isShowErrorToast, String url, String token, String json, final ResultCallback callback) {
        if (token == null || url == null || json == null) return;
        getInstance()._postAsyn(isShowErrorToast, url, callback, json, token);
    }

    public void getBitmap(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().get()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                if (e == null || e.getMessage() == null || !e.getMessage().equals("Canceled")) {
                    sendFailedStringCallback(e, callback);
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        sendSuccessResultCallback(response.body().bytes(), callback);
                    } else {
                        sendFailedStringCallback(null, callback);
                    }
                } catch (Exception e) {
                    sendFailedStringCallback(e, callback);
                }
            }
        });
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    private void _postAsyn(Object tag, String url, final ResultCallback callback, String json) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .post(body)
                .build();
        deliveryResult(callback, request);
    }

    private void _postAsyn(boolean isShowErrorToast, Object tag, String url, final ResultCallback callback, String json) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .post(body)
                .build();
        deliveryResult(isShowErrorToast, callback, request);
    }

    private void _postAsyn(boolean isShowErrorToast, String url, final ResultCallback callback, String json, String token) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .addHeader("token", token)
                .url(url)
                .post(body)
                .build();
        deliveryResult(isShowErrorToast, callback, request);
    }

    private void _postAsyn(String url, final ResultCallback callback, String json, String token) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .addHeader("token", token)
                .url(url)
                .post(body)
                .build();
        deliveryResult(callback, request);
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                if (e == null || e.getMessage() == null || !e.getMessage().equals("Canceled")) {
                    sendFailedStringCallback(e, callback);
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response == null) return;
                    final String string = response.body().string();
                    if (string == null) return;
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }


                } catch (Exception e) {
                    sendFailedStringCallback(e, callback);
                }
            }
        });
    }

    private void deliveryResult(final boolean isShowErrorToast, final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                if (e == null || e.getMessage() == null || !e.getMessage().equals("Canceled")) {
                    sendFailedStringCallback(e, callback);
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response == null) return;
                    final String string = response.body().string();
                    if (string == null) return;
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }


                } catch (Exception e) {
                    sendFailedStringCallback(e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private void sendLoadingResultCallback(final int total, final int current, final DownloadResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoading(total, current);
                }
            }
        });
    }

    public void cancelCallsWithTag(Object tag) {
        if (tag == null || mOkHttpClient == null) {
            return;
        }
        synchronized (Dispatcher.class) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }
        }
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Exception e);

        public abstract void onResponse(T response);
    }

    public static abstract class DownloadResultCallback<T> extends ResultCallback<T> {
        public abstract void onLoading(int total, int current);
    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}

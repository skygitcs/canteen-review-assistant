package edu.thu.canteen.data.network;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String PREF_NAME = "canteen_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    private static ApiService apiService;
    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    String token = getToken();
                    if (token != null && !token.isEmpty()) {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                    return chain.proceed(chain.request());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getService() {
        return apiService;
    }

    public static void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    public static void addLocalActivity(String content) {
        String existing = prefs.getString("local_activities", "");
        String updated = content + ";" + existing;
        // Keep only last 20 activities
        String[] parts = updated.split(";");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(parts.length, 20); i++) {
            if (!parts[i].isEmpty()) sb.append(parts[i]).append(";");
        }
        prefs.edit().putString("local_activities", sb.toString()).apply();
    }

    public static java.util.List<String> getLocalActivities() {
        String data = prefs.getString("local_activities", "");
        return java.util.Arrays.asList(data.split(";"));
    }

    public static void clearLocalActivities() {
        prefs.edit().remove("local_activities").apply();
    }
}

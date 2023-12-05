package luishenrique.zipimoveis.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofitZIP = null;
    private static Retrofit retrofitCEP = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofitZIP == null) {
            retrofitZIP = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitZIP;
    }

    public static Retrofit getCEP(String baseUrl) {
        if (retrofitCEP == null) {
            retrofitCEP = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitCEP;
    }


}

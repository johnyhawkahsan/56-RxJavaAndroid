package com.johnyhawkdesigns.a56_rxjavaandroid.MVVM;

// https://codingwithmitch.com/courses/rxjava-rxandroid-for-beginners/rx-operators-from-future/
// This class is responsible for creating the Retrofit instance and getting a reference to the
// RequestApi class that I defined above. The thing to pay attention to here is the addCallAdapterFactory
// method call. Without that, we can't convert Retrofit Call objects Flowables/Observables.

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com"; // This website is used to test requests

    // method to build retrofit from BASE URL
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Without that, we can't convert Retrofit Call objects Flowables/Observables.
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build(); // Build retrofit object from retrofitBuilder method

    private static RequestApi requestApi = retrofit.create(RequestApi.class); // this class has @get method to create Observable makeObservableQuery

    // method to return created request API
    public static RequestApi getRequestApi(){
        return requestApi;
    }

}

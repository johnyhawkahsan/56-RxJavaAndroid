package com.johnyhawkdesigns.a56_rxjavaandroid.MVVM;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

// Here is the "bread and butter" of the example. This is where I'm using an Executor to make
// a network call, and then returning a Future Observable to the ViewModel.
public class Repository {

    private static Repository repositoryInstance;

    // Method to return repositoryInstance
    public static Repository getInstance(){
        if(repositoryInstance == null){
            repositoryInstance = new Repository();
        }
        return repositoryInstance;
    }

    // Return "LiveData" Object
    public LiveData<ResponseBody> makeReactiveQuery(){
        return LiveDataReactiveStreams.fromPublisher(ServiceGenerator.getRequestApi() // this gets requestApi created out of retrofit
        .makeQuery() // method in RequestApi
        .subscribeOn(Schedulers.io())); // this tells to do this task in the background

    }

}

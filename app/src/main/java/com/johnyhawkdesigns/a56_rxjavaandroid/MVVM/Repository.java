package com.johnyhawkdesigns.a56_rxjavaandroid.MVVM;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
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

    // Return "Future" Object
    public Future<Observable<ResponseBody>> makeFutureQuery(){

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<ResponseBody>> myNetworkCallable = new Callable<Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> call() throws Exception {
                return ServiceGenerator.getRequestApi().makeObservableQuery();
            }
        };

        // This is our main method which returns futureObservable
        final Future<Observable<ResponseBody>> futureObservable = new Future<Observable<ResponseBody>>(){

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if(mayInterruptIfRunning){
                    executor.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<ResponseBody> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<ResponseBody> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeout, unit);
            }
        };

        return futureObservable;
    }


}

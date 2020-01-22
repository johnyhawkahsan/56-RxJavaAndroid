package com.johnyhawkdesigns.a56_rxjavaandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import okhttp3.ResponseBody;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.MainViewModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ui
    private TextView textView;

    //ui
    private SearchView searchView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable(); // global disposables object
    private long timeSinceLastRequest; // for log printouts only. Not part of logic.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);

        searchView = findViewById(R.id.search_view);
        timeSinceLastRequest = System.currentTimeMillis();

        // Suppose you have a SearchView in your app. As the user enters characters into the SearchView, you want to perform queries on the server.
        // If you don't limit the capturing of the characters to a time period, a new request will be made every time they enter a new character into the SearchView.
        // Typically this is unnecessary and would yield undesirable results. Executing a new search every 0.5 seconds or so is probably fine.
        //======================================Debounce - Example: Restrict Server Requests===================================//
        Observable<String> observableQueryText = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        // Listen for text input into the SearchView
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if(!emitter.isDisposed()){ // if emitter is not disposed
                                    emitter.onNext(newText); // Pass the query to the emitter
                                }
                                return false;
                            }
                        });
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS) // Apply Debounce() operator to limit requests - 0.5 sec or 500 ms
                .subscribeOn(Schedulers.io()); // Do this in background


        // Subscribe an Observer
        observableQueryText.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: time  since last request: " + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();

                // method for sending a request to the server
                sendRequestToServer(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    // Fake method for sending a request to the server
    private void sendRequestToServer(String query){
        // do nothing
    }

    // make sure to clear disposables when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}


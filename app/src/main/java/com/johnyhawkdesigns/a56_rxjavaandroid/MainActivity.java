package com.johnyhawkdesigns.a56_rxjavaandroid;

import androidx.appcompat.app.AppCompatActivity;
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

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ui
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);


//===================================Using "Handler" and "Runnable" Operator for DELAY================================//
//A pretty common problem we Android programmers run into is 'running a method' or 'executing some kind of process' at a specific time interval or delay.

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            int elapsedTime = 0;

            @Override
            public void run() {
                if (elapsedTime >= 5) { // if greater than 5 seconds
                    handler.removeCallbacks(this); // remove callback
                } else {
                    elapsedTime = elapsedTime + 1; // As long as less than "5", increment by 1
                    handler.postDelayed(this, 1000);
                    Log.d(TAG, "run: elapsedTime = " + elapsedTime);
                }
            }
        };

        //Typically, delay is implemented using a Runnable and a Handler with a postDelayed() method call.
        handler.postDelayed(runnable, 1000); // 1 sec delay


        //======================================interval() Operator===================================//


        // We are using "interval" operator = emit an observable every time interval
        Observable<Long> intervalObservable = Observable
                .interval(1, TimeUnit.SECONDS) // For "1" second
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .takeWhile(new Predicate<Long>() { // stop the process if more than 5 seconds passes
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        Log.d(TAG, "test: aLong = " + aLong + ", thread name: " + Thread.currentThread().getName());
                        return aLong <= 5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread

        //Subscribe to the Observable:
        intervalObservable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: interval: " + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called.");
            }
        });



        //======================================timer() Operator===================================//
        // emit single observable after a given delay
        Observable<Long> timeObservable = Observable
                .timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        timeObservable.subscribe(new Observer<Long>() {

            long time = 0; // variable for demonstrating how much time has passed

            @Override
            public void onSubscribe(Disposable d) {
                time = System.currentTimeMillis() / 1000;
            }
            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: " + ((System.currentTimeMillis() / 1000) - time) + " seconds have elapsed." );
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });


    }


}

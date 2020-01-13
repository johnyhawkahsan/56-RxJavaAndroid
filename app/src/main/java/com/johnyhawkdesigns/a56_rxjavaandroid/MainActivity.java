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
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ui
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);

        // This is a single task because we want to test "create" operator which only accepts one task
        final Task task = new Task("Walk the dog", false, 3);


        //======================================JUST Operator===================================//
        // We are using "create" operator
        Observable<Task> createObservable = Observable
                //The Create() operator is used to create Observables. The 'just()' and 'create()' operators should be used if you want to create a single Observable. The Just() operator has the ability to accept a list of up to 10 entries.
                .create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {


                     /*
                        // If we want to use single task in "create" operator
                        if (!emitter.isDisposed()) { // check if emitter is not disposed
                            emitter.onNext(task); // just initiate single task
                            emitter.onComplete(); // call complete method to finish emitter
                        }
                     */

                        // we use for loop if we want multiple tasks instead of single task in "create" operator
                        for (Task task : DataSource.createTasksList()) { // creating list of Tasks
                            if (!emitter.isDisposed()) {
                                emitter.onNext(task);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete(); // call complete method to finish emitter
                        }

                    }
                })
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread

        createObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: ThreadName = " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());
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



        //======================================JUST Operator===================================//
        // Example of just operator = In this example I'm using the "just()" operator and passing a data set of --10-- Strings.
        Observable<Task> justObservable = Observable
                //The Create() operator is used to create Observables. The 'just()' and 'create()' operators should be used if you want to create a single Observable. The Just() operator has the ability to accept a list of up to 10 entries.
                .just(task)
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread

        justObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: ThreadName = " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());
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



        //======================================Range Operator===================================//
        // We are using "range" operator
        Observable<Integer> rangeObservable = Observable
                .range(0, 9)
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread

        rangeObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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

        // We are using "range" operator with MAP
        Observable<Task> rangeObservableWithMap = Observable
                .range(0, 9)
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .map(new Function<Integer, Task>() {
                    @Override
                    public Task apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: " + Thread.currentThread().getName());
                        return new Task("This is task with priority: " + String.valueOf(integer),
                                false,
                                integer);
                    }
                })
                .takeWhile(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        return task.getPriority() < 9;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread


        rangeObservableWithMap.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Task task1) {
                Log.d(TAG, "onNext: " + task1.getPriority());
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


        //======================================Repeat Operator===================================//
        // We are using "range" operator
        Observable<Integer> repeatObservable = Observable
                .range(0, 3)
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .repeat(3) // repeat 3 times
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread


        repeatObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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






    }


}

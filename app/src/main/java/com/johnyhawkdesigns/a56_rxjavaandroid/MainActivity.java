package com.johnyhawkdesigns.a56_rxjavaandroid;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.MainViewModel;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ui
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);


        // Transformation Operators - Map
        // map = "Applies a function to each emitted item. It transforms each emitted item by applying a function to it."
        //====================================== Mapping (Task -> String) ===================================//
        Observable<String> extractDescriptionObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .map(extractDescriptionFunction) // map = Returns an Observable that applies a specified function to each item emitted by the source ObservableSource and emits the results of these function applications.
                .observeOn(AndroidSchedulers.mainThread());

        extractDescriptionObservable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: extracted description: " + s);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });




        // In this example I will create a custom map function that updates a Task object and then emits that updated Task.
        //====================================== Mapping (Task -> Updated Task) ===================================//
        Observable<Task> completeTaskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .map(completeTaskFunction)
                .observeOn(AndroidSchedulers.mainThread());

        completeTaskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: is this task complete? " + task.isComplete());
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });




    }

    // Function (RxJava) = A functional interface that takes a value and returns another value, possibly with a different type
    // This is a function to extract description from task.
    Function<Task, String> extractDescriptionFunction = new Function<Task, String>() {
        @Override
        public String apply(Task task) throws Exception {
            Log.d(TAG, "apply: doing work on thread: " + Thread.currentThread().getName());
            return task.getDescription();
        }
    };


    // Receives a Task object and modifies it and then emits that updated Task.
    Function<Task, Task> completeTaskFunction = new Function<Task, Task>() {
        @Override
        public Task apply(Task task) throws Exception {
            Log.d(TAG, "apply: doing work on thread: " + Thread.currentThread().getName());
            task.setComplete(true);
            return task;
        }
    };
}


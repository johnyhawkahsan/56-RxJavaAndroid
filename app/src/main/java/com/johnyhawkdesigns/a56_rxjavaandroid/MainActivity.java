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

        Observable<Task> taskObservable = Observable
                //The Create() operator is used to create Observables. The 'just()' and 'create()' operators should be used if you want to create a single Observable. The Just() operator has the ability to accept a list of up to 10 entries.
                .create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {

                        if (!emitter.isDisposed()){ // check if emitter is not disposed
                            emitter.onNext(task);
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.io()) // We want it to work on background thread
                .observeOn(AndroidSchedulers.mainThread()); // We want to show results on main thread

        taskObservable.subscribe(new Observer<Task>() {
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


    }


}

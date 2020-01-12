package com.johnyhawkdesigns.a56_rxjavaandroid;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList()) // from our custom list of tasks
                .subscribeOn(Schedulers.io())               // designate worker thread (background) where work is going to be done
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        Log.d(TAG, "filter test: " + Thread.currentThread().getName());

                        // Sleep/delay thread for 1 sec
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); // where the results are going to be observed/emit from (designate observer thread)

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

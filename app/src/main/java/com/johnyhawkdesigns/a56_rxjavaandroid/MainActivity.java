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

        // This is a single task because we want to test "create" operator which only accepts one task
        final Task task = new Task("Walk the dog", false, 3);

        //"The Filter operator filters an Observable by only allowing items through that pass a test that you specify in the form of a predicate function."
        //======================================filter() - Filtering a STRING ===================================//


        Observable<Task> strFilterObservable = Observable
                .fromIterable(DataSource.createTasksList()) // create list of observable
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        if(task.getDescription().equals("Walk the dog")){ // Only display the task with this description
                            return true;
                        }
                        return false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        strFilterObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: This task matches the description: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });




        //======================================filter() - Filtering a BOOLEAN ===================================//
        Observable<Task> boolFilterObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        return task.isComplete(); // only return tasks that are "complete"
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        boolFilterObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: This is a completed task: " + task.getDescription());
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });


        //======================================distinct() - Filtering a BOOLEAN ===================================//
        // The Distinct operator filters an Observable by only allowing items through that have not already been emitted.
        // But what defines the object as "distinct" is up to the developer to determine.
        Observable<Task> distinctTaskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .distinct(new Function<Task, String>() { // <--- CORRECT
                    @Override
                    public String apply(Task task) throws Exception {
                        return task.getDescription();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        distinctTaskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: distinct = " + task.getDescription());
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


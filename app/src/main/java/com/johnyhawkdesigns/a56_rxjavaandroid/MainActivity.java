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
                Log.d(TAG, "onNext:filter() String = This task matches the description: " + task.getDescription());
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
                Log.d(TAG, "onNext: filter() Boolean = This is a completed task: " + task.getDescription());
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



        //======================================take() - Filtering a BOOLEAN ===================================//
        //The main difference between the take() operators and the filter() operator is that the filter() operator will check every object in the list. So you could say the filter() operator is inclusive.
        //Whereas the take() operators would be considered exclusive because they don't necessary check every item in the list. They will emit objects only until the condition of their function is satisfied.
        Observable<Task> takeObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .take(3) //Even though there is 5 Task objects added to the list, only 3 are emitted.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        takeObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: take = " + task.getDescription());
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });


        //======================================takeWhile() - Filtering a BOOLEAN ===================================//
        // The TakeWhile() mirrors the source Observable until such time as some condition you specify becomes false. If the condition becomes false, TakeWhile() stops mirroring the source Observable and terminates its own Observable.
        Observable<Task> takeWhileObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .takeWhile(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        return task.isComplete(); //Only the first Task will be emitted because it is marked as complete.
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        takeWhileObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: takeWhile = " + task.getDescription());
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


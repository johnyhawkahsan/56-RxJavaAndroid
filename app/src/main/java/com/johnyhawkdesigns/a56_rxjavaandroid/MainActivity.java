package com.johnyhawkdesigns.a56_rxjavaandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import okhttp3.ResponseBody;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.Comment;
import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.MainViewModel;
import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.Post;
import com.johnyhawkdesigns.a56_rxjavaandroid.MVVM.ServiceGenerator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // ui
    private TextView textView;
    private Button button;
    private SearchView searchView;
    private RecyclerView recyclerView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable(); // global disposables object
    private long timeSinceLastRequest; // for log printouts only. Not part of logic.
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);
        button = findViewById(R.id.button);
        searchView = findViewById(R.id.search_view);
        timeSinceLastRequest = System.currentTimeMillis();
        recyclerView = findViewById(R.id.recycler_view);

        //A Flatmap is ideal for this situation because I need to get data from more than one source (i.e; posts + comments) and then combine it into a single emission.
        //======================================flatmap() - Example: Get sample posts + comments ===================================//

        initRecyclerView();

        getPostsObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Post, ObservableSource<Post>>() { // we use this flatmap to again call an observable for comments
                    @Override
                    public ObservableSource<Post> apply(Post post) throws Exception {
                        return getCommentsObservable(post);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Post post) {
                        updatePost(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    // This method generates Observable "getPosts()" method from RequestApi after creating required service.
    private Observable<Post> getPostsObservable() {
        return ServiceGenerator.getRequestApi()
                //  getPosts() returns an observable
                .getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Post>, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(final List<Post> posts) throws Exception {
                        adapter.setPosts(posts);
                        return Observable.fromIterable(posts)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }



    private void updatePost(final Post p) {
        Observable
                .fromIterable(adapter.getPosts())
                .filter(new Predicate<Post>() {
                    @Override
                    public boolean test(Post post) throws Exception {
                        return post.getId() == p.getId();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Post post) {
                        Log.d(TAG, "onNext: updating post: " + post.getId() + ", thread: " + Thread.currentThread().getName());
                        adapter.updatePost(post);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private Observable<Post> getCommentsObservable(final Post post) {
        return ServiceGenerator.getRequestApi()
                // we provide id to getComments method to return comments of this specific post
                .getComments(post.getId())
                .map(new Function<List<Comment>, Post>() {
                    @Override
                    public Post apply(List<Comment> comments) throws Exception {

                        int delay = ((new Random()).nextInt(5) + 1) * 1000; // sleep thread for x ms
                        Thread.sleep(delay);
                        Log.d(TAG, "apply: sleeping thread " + Thread.currentThread().getName() + " for " + String.valueOf(delay) + "ms");

                        post.setComments(comments);
                        return post;
                    }
                })
                .subscribeOn(Schedulers.io());

    }

    // setup recyclerView
    private void initRecyclerView() {
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // make sure to clear disposables when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}


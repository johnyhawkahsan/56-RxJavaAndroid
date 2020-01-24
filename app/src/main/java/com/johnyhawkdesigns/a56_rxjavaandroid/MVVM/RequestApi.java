package com.johnyhawkdesigns.a56_rxjavaandroid.MVVM;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// https://codingwithmitch.com/courses/rxjava-rxandroid-for-beginners/rx-operators-from-future/
// Here is the method Retrofit uses to make the query to the REST API.
// Notice it returns an Observable. It's able to return an Observable because
// of the RxJava Call Adapter dependency I mentioned in the dependencies section.
//Also notice the RxJava Call Adapter dependency. By default, you can't return
// an Observable or a Flowable from a Retrofit request. This dependency will
// allow you to convert Retrofit Call objects to Flowables / Observables.
public interface RequestApi {

    // This test method is provided by : https://jsonplaceholder.typicode.com/
//    @GET("todos/1")
//    Observable<ResponseBody> makeObservableQuery();


    @GET("todos/1")
    Flowable<ResponseBody> makeQuery();


    //getPosts() will retrieve the list of posts.
    @GET("posts")
    Observable<List<Post>> getPosts();

    //getComments() will retrieve a list of comments for a particular post using post's id
    @GET("/comments")
    Observable<List<Comment>> getComments(
            @Query("postId") int id
    );

}
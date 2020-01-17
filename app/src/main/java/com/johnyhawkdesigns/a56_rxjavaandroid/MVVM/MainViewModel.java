package com.johnyhawkdesigns.a56_rxjavaandroid.MVVM;

import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

// Since I'm using MVVM architecture I must have a ViewModel. This ViewModel is pretty straight-forward.
// There's just a single method that accesses the Repository and returns the Future Observable.
public class MainViewModel extends ViewModel {

    private Repository repository;

    // Constructor of MainViewModel also returns repository instance
    public MainViewModel() {
        repository = Repository.getInstance();
    }

    // This is just MVVM requirement to return method defined inside "Repository" to return here.
    public LiveData<ResponseBody> makeQuery(){
        return repository.makeReactiveQuery();
    }

}
package com.johnyhawkdesigns.a56_rxjavaandroid;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    // This is a single task
    public static Task singleTask(){
        return new Task("Walk the dog", false, 3);
    }

    // Returns a task array
    public static Task[] taskArray(){

        Task[] arrayList = new Task[5];
        arrayList[0] = (new Task("Take out the trash", true, 3));
        arrayList[1] = (new Task("Walk the dog", false, 2));
        arrayList[2] = (new Task("Make my bed", true, 1));
        arrayList[3] = (new Task("Unload the dishwasher", false, 0));
        arrayList[4] = (new Task("Make dinner", true, 5));

        return arrayList;
    }

    // Returns a list of Tasks
    public static List<Task> createTasksList(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Take out the trash", true, 3));
        tasks.add(new Task("Walk the dog", false, 2));
        tasks.add(new Task("Make my bed", true, 1));
        tasks.add(new Task("Unload the dishwasher", false, 0));
        tasks.add(new Task("Make dinner", true, 5));
        return tasks;
    }


}

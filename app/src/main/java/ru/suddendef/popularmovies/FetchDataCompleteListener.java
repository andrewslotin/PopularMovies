package ru.suddendef.popularmovies;

/**
 * Created by suddendef on 03/04/2017.
 */

public interface FetchDataCompleteListener<T> {
    public void onFetchDataTaskComplete(T data);
}

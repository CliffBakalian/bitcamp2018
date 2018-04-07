package edu.umcp.jacc.burnt;

/* CustomListener<T>
 *
 * Interface for responding to network calls
 * getResult(T object) is called when a network call succeeds
 *
 * **/
interface CustomListener<T> {
    void getResult(T object);
}
package ru.otlsoft.msword;

public interface Callback<T> {
    public abstract void run(T parameter);
}

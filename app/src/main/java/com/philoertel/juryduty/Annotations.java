package com.philoertel.juryduty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/** Annotations used throughout the project. */
public final class Annotations {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Now {}

    private Annotations() {}
}

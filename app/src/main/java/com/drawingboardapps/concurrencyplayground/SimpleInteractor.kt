package com.drawingboardapps.concurrencyplayground

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import java.lang.Thread.sleep

class SimpleInteractor {

    val disposables = CompositeDisposable()

    fun foo() {
        println("called foo: ${Thread.currentThread().name}")
    }

    fun bar() {
        println("called bar:  ${Thread.currentThread().name}")
    }

    fun doWork(timeMs: Long): SimpleResult {
        foo()
        sleep(timeMs)
        bar()
        return SimpleResult.Success
    }

    fun doWorkObservable(timeMs: Long): Observable<SimpleResult> {
        return Observable.fromCallable {
            foo()
            sleep(timeMs)
            bar()
            SimpleResult.Success
        }
    }

    fun doWorkContainedObservableAndSubscribed(timeMs: Long) {
        disposables.add(
            Observable.fromCallable {
                doWork(timeMs)
            }.subscribeBy(
                { it.printStackTrace() },
                { println("complete") },
                { bar() })
        )
    }


    sealed class SimpleResult {
        object Success : SimpleResult()
        data class Fail(val exception: Exception) : SimpleResult()
    }
}

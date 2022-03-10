import com.drawingboardapps.concurrencyplayground.SimpleInteractor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SimplePresenter(val simpleInteractor: SimpleInteractor) {

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWork(timeMs: Long): SimpleInteractor.SimpleResult {
        return simpleInteractor.doWork(timeMs)
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkObservable(timeMs: Long): Observable<SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkReturnsObservableSubscribeIoMain(timeMs: Long): Observable<out SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }.iOsubscribeMain()
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkObservableIoMain(timeMs: Long): Observable<out SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWOrkContainedIoIo(timeMs: Long): Observable<out SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }.ioSubscribeIo()
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkNestedFlatMapObservable(timeMs: Long): Observable<out SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }.flatMap {
            simpleInteractor.doWorkObservable(timeMs)
        }
    }

    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkNestedSubFlatMapObservable(timeMs: Long): Observable<out SimpleInteractor.SimpleResult> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }.flatMap {
            simpleInteractor.doWorkObservable(timeMs).ioSubscribeIo()
        }
    }


    /**
     * Do some work by sleeping
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkNestedSubFlatMapObservableSubscribed(timeMs: Long): Observable<Unit> {
        return Observable.fromCallable {
            simpleInteractor.doWork(timeMs)
        }.map {
            simpleInteractor.doWorkContainedObservableAndSubscribed(timeMs)
        }
    }

    /**
     * Do some work by calling doWorkContainedObservableAndSubscribed
     * @param timeMs time to sleep in milliseconds
     */
    fun doWorkContainedAndSubscribedObservable(timeMs: Long) {
        simpleInteractor.doWorkContainedObservableAndSubscribed(timeMs)
    }

    private fun sleep(timeMs: Long) {
        Thread.sleep(timeMs)
    }
}

fun <T> Observable<T>.iOsubscribeMain(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

fun <T> Observable<T>.newThreadSubscribeIo(): Observable<T> {
    return this
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.io())

}
fun <T> Observable<T>.ioSubscribeIo(): Observable<T> {
    return this
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())

}

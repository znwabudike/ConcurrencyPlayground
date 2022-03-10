package com.drawingboardapps.concurrencyplayground

import SimplePresenter
import iOsubscribeMain
import io.mockk.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import ioSubscribeIo
import junit.framework.TestCase.fail
import newThreadSubscribeIo
import org.junit.*
import java.lang.Exception
import java.util.concurrent.CountDownLatch

import java.util.concurrent.TimeUnit

class SimpleInteractorCaseTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {

        }
    }

    @Before
    fun setUp() {
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
//        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Ignore
    @Test
    fun testFunction() {
        val simpleInteractor: SimpleInteractor = spyk()
        val simpleSlot = slot<SimpleInteractor>()

        every {
            simpleInteractor.doWork(any())
        } answers {
            SimpleInteractor.SimpleResult.Success
        }

        SimplePresenter(simpleInteractor)
            .doWorkContainedAndSubscribedObservable(2000)


        verify { simpleInteractor.doWork(any()) }
        verify(exactly = 1) { simpleInteractor.foo() }
    }

    @Test
    fun testFunctionUsingTest() {
        //use spy when you want to call real methods
        val simpleInteractor: SimpleInteractor = spyk()

        val waitTime: Long = 2000
        SimplePresenter(simpleInteractor)
            .doWorkObservable(waitTime)
            .test()
            .await(waitTime + 1000, TimeUnit.MILLISECONDS)

        verify { simpleInteractor.doWorkContainedObservableAndSubscribed(any()) }
        verify(exactly = 1) { simpleInteractor.foo() }
    }

    @Test
    fun testFunctionContainedObservableAndSubscribed() {
        val simpleInteractor: SimpleInteractor = spyk()
        val waitTime: Long = 2000

        val obs = SimplePresenter(simpleInteractor)
            .doWorkObservableIoMain(waitTime)
            .test()

        obs.await(waitTime + 600, TimeUnit.MILLISECONDS)

        verify(exactly = 1) { simpleInteractor.foo() }
    }

    @Test
    fun doWOrkIoIo() {
        val simpleInteractor: SimpleInteractor = spyk()
        val waitTime: Long = 2000

        RxJavaPlugins.reset()

        SimplePresenter(simpleInteractor)
            .doWorkObservable(waitTime)
            .ioSubscribeIo()
            .test()
            .await(waitTime + 600, TimeUnit.MILLISECONDS)

        verify(exactly = 1) { simpleInteractor.foo() }
        verify(exactly = 1) { simpleInteractor.bar() }
    }

    @Test
    fun doWorkTest() {
        val simpleInteractor: SimpleInteractor = spyk()
        val waitTime: Long = 2000
        val latch = CountDownLatch(1)

        RxJavaPlugins.reset()

        every { simpleInteractor.bar() } answers { latch.countDown() }

        SimplePresenter(simpleInteractor)
            .doWorkObservable(waitTime)
            .iOsubscribeMain()
            .subscribe()

        latch.await(waitTime + 600, TimeUnit.MILLISECONDS)
        verify(exactly = 1) { simpleInteractor.foo() }
        verify(exactly = 1) { simpleInteractor.bar() }
    }

    @Test
    fun doWorkNested() {
        val simpleInteractor: SimpleInteractor = spyk()
        val waitTime: Long = 2000

        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()

        val latch = CountDownLatch(4)

        every { simpleInteractor.bar() } answers { latch.countDown() }

        SimplePresenter(simpleInteractor)
            .doWorkNestedSubFlatMapObservable(waitTime)
            .newThreadSubscribeIo()
            .subscribe()

        try{
            latch.await(2* waitTime + 600, TimeUnit.MILLISECONDS)
        } catch (ex: Exception){
            fail(ex.message)
        }

        verify(exactly = 2) { simpleInteractor.foo() }
        verify(exactly = 2) { simpleInteractor.bar() }
    }

    @Test
    fun doWorkNestedSubscribed() {
        val simpleInteractor: SimpleInteractor = spyk()
        val waitTime: Long = 2000

        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()

        val latch = CountDownLatch(4)

        every { simpleInteractor.foo() } answers { latch.countDown() }
        every { simpleInteractor.bar() } answers { latch.countDown() }

        SimplePresenter(simpleInteractor)
            .doWorkNestedSubFlatMapObservableSubscribed(waitTime)
            .newThreadSubscribeIo()
            .subscribe()

        try{
            latch.await(2* waitTime + 600, TimeUnit.MILLISECONDS)
        } catch (ex: Exception){
            fail(ex.message)
        }

        verify(exactly = 2) { simpleInteractor.foo() }
        verify(exactly = 3) { simpleInteractor.bar() }
    }
}
package com.yuseok.android.rxbasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 실제 Task를 처리하는 객체 (발행자)
        Observable<String> simpleObservable =
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        // 네트웍을 통해서 데이터를 긁어온다.
                        // 반복문을 돌면서 ----------
                        // for (네트웍에서 가져온 데이터) {
                        subscriber.onNext("Hello RxAndroid !!1");
                        subscriber.onNext("Hello RxAndroid !!2");
                        subscriber.onNext("Hello RxAndroid !!3");
                        subscriber.onNext("Hello RxAndroid !!4");
                        // }
                        // ---------------------

                        subscriber.onCompleted(); // 더이상 넘길 객체가 없을때 onCompleted() 호출
                    }
                });

        // 위 객체를 람다식으로 바꾼 경우
//        Observable<String> simpleObservable =
//
//                Observable.create((subscriber) -> {
//                            subscriber.onNext("Hello RxAndroid !!1");
//                            subscriber.onNext("Hello RxAndroid !!2");
//                            subscriber.onNext("Hello RxAndroid !!3");
//                            subscriber.onNext("Hello RxAndroid !!4");
//
//                            subscriber.onCompleted();
//                        }
//                );


        // 옵저버 (구독자)를 등록해주는 함수 - 기본형
        simpleObservable
                .subscribe(new Subscriber<String>() { // Observer(구독자)
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "[Observer1] complete!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "[Observer1] error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String text) {
                        Toast.makeText(MainActivity.this, "[Observer1]" + text, Toast.LENGTH_SHORT).show();
                    }
                });

        // 옵저버를 등록하는 함수 - 진화형 (각 함수를 하나의 콜백객체에 나눠서 담아준다)
        //subscribe안에 3개의 함수가 들어있는 것을 람다식으로 표현하기 위해 세개의 객체로 나눔
        // 함수를 하나만 가지고 있는 Callback객체를 생성
        simpleObservable.subscribe(new Action1<String>() { // onNext함수와 동일한 역할을 하는 콜백객체
            @Override
            public void call(String s) {
                Toast.makeText(MainActivity.this, "[Observer2]" + s, Toast.LENGTH_SHORT).show();
            }
        }, new Action1<Throwable>() { // onError 함수와 동일한 역할을 하는 콜백객체
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "[Observer2] error: " + throwable.getMessage());
            }
        }, new Action0() { // onComplete과 동일한 역할을 하는 콜백객체 // 받는인자가 없는 것 Action0
            @Override
            public void call() {
                Log.d(TAG, "[Observer2] complete!");
            }
        });

        // 옵저버를 등록하는 함수 - 최종진화형 (람다형)
        simpleObservable.subscribe(
                (string) -> { Toast.makeText(MainActivity.this, "[Observer3]" + string, Toast.LENGTH_SHORT).show();}
                , (error) -> { Log.e(TAG, "[Observer3] error: " + error.getMessage());}
                ,() -> { Log.d(TAG, "[Observer3] complete!");}
        );
    }
}
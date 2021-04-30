package com.skts.ourmemory.view.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.HomeRoomAdapter;
import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.model.main.HomeRoomData;
import com.skts.ourmemory.model.main.HomeRoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    HomeRoomAdapter mHomeRoomAdapter;

    /**
     * setHasOptionsMenu true : 액티비티보다 프레그먼트의 메뉴가 우선
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_main_home_recyclerview);

        // Init layoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set layoutManger
        recyclerView.setLayoutManager(linearLayoutManager);

        mHomeRoomAdapter = new HomeRoomAdapter();
        recyclerView.setAdapter(mHomeRoomAdapter);

        showRoomList(container.getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Show room list
     */
    private void showRoomList(Context context) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(context);
        int userId = mySharedPreferences.getIntExtra(Const.USER_ID);

        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<HomeRoomPostResult> observable = service.getHomeRoomData(userId);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<HomeRoomPostResult>() {
                    String resultCode;
                    String message;


                                   @Override
                                   public void onNext(@NonNull HomeRoomPostResult homeRoomPostResult) {
                                       DebugLog.i("testtt", homeRoomPostResult.toString());
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {

                                   }

                                   @Override
                                   public void onComplete() {

                                   }
                               }
                ));

        /*HomeRoomData homeRoomData = new HomeRoomData("테스트", "오광석");
        HomeRoomData homeRoomData2 = new HomeRoomData("테스트2", "오광석2");
        mHomeRoomAdapter.addItem(homeRoomData);
        mHomeRoomAdapter.addItem(homeRoomData2);*/
    }
}

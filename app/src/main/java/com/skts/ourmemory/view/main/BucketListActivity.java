package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.ArcMotion;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.BucketListContract;
import com.skts.ourmemory.presenter.BucketListPresenter;
import com.skts.ourmemory.util.CircularRevealTransition;
import com.skts.ourmemory.view.BaseActivity;

import java.util.Objects;

import butterknife.BindView;

public class BucketListActivity extends BaseActivity implements BucketListContract.View {
    private BucketListContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_bucket_list)
    MaterialToolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sceneRoot)
    ViewGroup mSceneRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);

        initSet();
        setRecycler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void initSet() {
        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mPresenter = new BucketListPresenter();
        mPresenter.setView(this);

        showScene1(false);
    }

    @Override
    public void setRecycler() {
    }

    private void showScene1(boolean animated) {
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_bucket_list_scene1, null);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            showScene2();
        });
        Scene scene = new Scene(mSceneRoot, root);
        Transition transition = animated ? getScene1Transition() : null;
        TransitionManager.go(scene, transition);
    }

    private void showScene2() {
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_bucket_list_scene2, null);
        View btnBack = root.findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(v -> {
            showScene1(true);
        });

        Scene scene = new Scene(mSceneRoot, root);
        Transition transition = getScene2Transition();
        TransitionManager.go(scene, transition);
    }

    private Transition getScene2Transition() {
        TransitionSet set = new TransitionSet();

        // fab changes position
        ChangeBounds changeTransform = new ChangeBounds();
        changeTransform.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                // hide fab button on the end of animation
                mSceneRoot.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            }
        });
        changeTransform.addTarget(R.id.fab);
        changeTransform.setDuration(300);
        //fab arc path
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMaximumAngle(45);
        arcMotion.setMinimumHorizontalAngle(90);
        arcMotion.setMinimumVerticalAngle(0);
        changeTransform.setPathMotion(arcMotion);
        set.addTransition(changeTransform);

        //bg circular reveal animation starts
        CircularRevealTransition crt = new CircularRevealTransition();
        crt.addTarget(R.id.ib_dialog_color_color1);
        crt.setStartDelay(200);
        crt.setDuration(600);
        set.addTransition(crt);

        //buttons appear
        Fade fade = new Fade();
        fade.addTarget(R.id.btnBegin);
        fade.addTarget(R.id.btnCancel);
        fade.addTarget(R.id.text);
        fade.setStartDelay(600);
        set.addTransition(fade);

        //left buttons column slide to left
        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(R.id.slideLeftContainer);
        set.addTransition(slide);
        //right buttons column slide to right
        Slide slide2 = new Slide(Gravity.RIGHT);
        slide2.addTarget(R.id.slideRightContainer);
        set.addTransition(slide2);
        return set;
    }

    private Transition getScene1Transition() {
        TransitionSet set = new TransitionSet();

        //buttons from scene2 fade out
        Fade fade = new Fade();
        fade.addTarget(R.id.btnBegin);
        fade.addTarget(R.id.btnCancel);
        fade.addTarget(R.id.text);
        set.addTransition(fade);

        //Circular Reveal collapse animation starts
        CircularRevealTransition crt = new CircularRevealTransition();
        crt.addTarget(R.id.ib_dialog_color_color1);
        crt.setDuration(600);
        set.addTransition(crt);

        //then fab button changes position
        ChangeBounds changeTransform = new ChangeBounds();
        changeTransform.addTarget(R.id.fab);
        changeTransform.setDuration(300);
        changeTransform.setStartDelay(500);
        //arc path
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMaximumAngle(45);
        arcMotion.setMinimumHorizontalAngle(90);
        arcMotion.setMinimumVerticalAngle(0);
        changeTransform.setPathMotion(arcMotion);
        set.addTransition(changeTransform);

        //left buttons column slide in from left
        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(R.id.slideLeftContainer);
        slide.setStartDelay(500);
        set.addTransition(slide);

        //right buttons column slide in from right
        Slide slide2 = new Slide(Gravity.RIGHT);
        slide2.addTarget(R.id.slideRightContainer);
        slide2.setStartDelay(500);
        set.addTransition(slide2);
        return set;
    }
}

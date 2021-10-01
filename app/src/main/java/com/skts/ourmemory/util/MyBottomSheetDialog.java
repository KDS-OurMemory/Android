package com.skts.ourmemory.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skts.ourmemory.R;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener = null;

    public interface BottomSheetListener {
        void onClickTakePhoto();        // 사진 촬영
        
        void onClickSelectPhoto();      // 사진 선택

        void onClickDeletePhoto();      // 사진 삭제
    }

    public void setOnClickListener(BottomSheetListener bottomSheetListener) {
        this.mListener = bottomSheetListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_profile, container, false);

        LinearLayout takePhotoLayout = view.findViewById(R.id.ll_dialog_bottom_sheet_profile_take_photo);
        LinearLayout selectPhotoLayout = view.findViewById(R.id.ll_dialog_bottom_sheet_profile_select_photo);
        LinearLayout deletePhotoLayout = view.findViewById(R.id.ll_dialog_bottom_sheet_profile_delete_photo);
        LinearLayout cancelPhotoLayout = view.findViewById(R.id.ll_dialog_bottom_sheet_profile_cancel_photo);

        // 사진 촬영
        takePhotoLayout.setOnClickListener(view1 -> {
            dismiss();
            mListener.onClickTakePhoto();
        });
        // 사진 선택
        selectPhotoLayout.setOnClickListener(view1 -> {
            dismiss();
            mListener.onClickSelectPhoto();
        });
        // 사진 삭제
        deletePhotoLayout.setOnClickListener(view1 -> {
            dismiss();
            mListener.onClickDeletePhoto();
        });

        // 취소
        cancelPhotoLayout.setOnClickListener(view1 -> dismiss());
        return view;
    }
}

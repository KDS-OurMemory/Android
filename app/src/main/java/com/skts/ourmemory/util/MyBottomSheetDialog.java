package com.skts.ourmemory.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skts.ourmemory.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {
    private Unbinder unbinder;
    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        TextView selectPhoto = view.findViewById(R.id.tv_dialog_bottom_sheet_profile_select_photo);
        selectPhoto.setOnClickListener(view1 -> {
            mListener.onClickSelectPhoto();
            dismiss();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public interface BottomSheetListener {
        void onClickSelectPhoto();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }
}

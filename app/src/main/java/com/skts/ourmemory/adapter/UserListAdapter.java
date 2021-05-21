package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private ArrayList<String> mData;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        ImageView addFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_fragment_add_friend_search_user_data_profile_image);
            this.userName = itemView.findViewById(R.id.tv_fragment_add_friend_search_user_data_text_view);
            this.addFriendButton = itemView.findViewById(R.id.iv_fragment_add_friend_search_user_data_plus_button);
        }
    }

    public UserListAdapter() {
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public UserListAdapter(ArrayList<String> list) {
        mData = list;
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.fragment_add_friend_search_user_data, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        //holder.imageView.setBackground();
        holder.userName.setText(text);
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListClear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }
}

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
import com.skts.ourmemory.model.user.UserDAO;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private final ArrayList<UserDAO> mData;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.iv_recyclerview_activity_friend_list_item_profile_image);
            this.textView = itemView.findViewById(R.id.tv_recyclerview_activity_friend_list_item_text_view);
        }
    }

    public FriendListAdapter() {
        mData = new ArrayList<>();
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public FriendListAdapter(ArrayList<UserDAO> list) {
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

        View view = layoutInflater.inflate(R.layout.recyclerview_activity_friend_list_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageView.setBackground();
        holder.textView.setText(mData.get(position).getName());
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(UserDAO userDAO) {
        mData.add(userDAO);
        notifyDataSetChanged();
    }
}

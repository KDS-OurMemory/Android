package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.Person;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private ArrayList<Person> mPersonData;
    private OnItemClickListener mOnItemClickListener = null;
    private OnClickListener mOnClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        Button addFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_fragment_add_friend_search_user_data_profile_image);
            this.userName = itemView.findViewById(R.id.tv_fragment_add_friend_search_user_data_text_view);
            this.addFriendButton = itemView.findViewById(R.id.btn_fragment_add_friend_search_user_data_plus_button);

            // 리사이클러뷰 클릭
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });

            // 친추 추가 버튼 클릭
            addFriendButton.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onClick(view, pos);
                }
            });
        }
    }

    public UserListAdapter() {
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public UserListAdapter(ArrayList<Person> personData) {
        mPersonData = personData;
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
        String profile = mPersonData.get(position).getProfile();
        String name = mPersonData.get(position).getName();

        //holder.profileImage
        holder.userName.setText(name);
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mPersonData.size();
    }

    public void setListClear() {
        if (mPersonData != null) {
            mPersonData.clear();
            notifyDataSetChanged();
        }
    }

    public Person getItem(int position) {
        return mPersonData.get(position);
    }
}

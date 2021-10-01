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

import com.bumptech.glide.Glide;
import com.skts.ourmemory.R;
import com.skts.ourmemory.model.user.UserDAO;

import java.util.ArrayList;

public class RequestFriendListAdapter extends RecyclerView.Adapter<RequestFriendListAdapter.ViewHolder> {
    private ArrayList<UserDAO> mData;
    private ArrayList<UserDAO> mRemainData;     // 데이터 복사본

    private OnItemClickListener mOnItemClickListener = null;
    private boolean mCollapsible;
    private Context mContext;

    public ArrayList<UserDAO> getData() {
        return mData;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button okButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.iv_recyclerview_activity_friend_request_list_item_profile_image);
            this.textView = itemView.findViewById(R.id.tv_recyclerview_activity_friend_request_list_item_text_view);
            this.okButton = itemView.findViewById(R.id.btn_recyclerview_activity_friend_request_list_item_ok);

            okButton.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });
        }
    }

    public RequestFriendListAdapter() {
        mData = new ArrayList<>();
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public RequestFriendListAdapter(ArrayList<UserDAO> list) {
        // 접혀있을 경우 추가하지 않음
        if (mCollapsible) {
            mRemainData = list;
        } else {
            mData = list;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recyclerview_activity_friend_request_list_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Glide 로 이미지 표시
        if (mData.get(position).getProfileImageUrl() == null) {
            Glide.with(mContext).load(R.drawable.ic_baseline_person_30).override(150, 150).circleCrop().into(holder.imageView);
        } else {
            Glide.with(mContext).load(mData.get(position).getProfileImageUrl()).override(150, 150).circleCrop().into(holder.imageView);
        }

        holder.textView.setText(mData.get(position).getName());
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int userId) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getUserId() == userId) {
                mData.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 아이템 전체 제거
     */
    public void removeAllItem() {
        mCollapsible = true;        // 접힐 때

        mRemainData = new ArrayList<>();
        mRemainData.addAll(mData);

        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 아이템 전체 추가
     */
    public void addAllItem() {
        mCollapsible = false;       // 펼 때

        mData = mRemainData;
        notifyDataSetChanged();
    }
}

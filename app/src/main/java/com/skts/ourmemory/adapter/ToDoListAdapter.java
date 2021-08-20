package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.todolist.ToDoListData;
import com.skts.ourmemory.model.todolist.ToDoListHeader;
import com.skts.ourmemory.model.todolist.ToDoListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoListAdapter extends RecyclerView.Adapter implements ToDoListItemTouchHelperListener {
    private final int DATE_TYPE = 1;
    private final int CONTENT_TYPE = 2;

    private Context mContext;
    private List<Object> mData;

    private OnItemClickListener mOnItemClickListener = null;
    private OnClickListener mOnClickListener = null;

    public ToDoListAdapter() {
        mData = new ArrayList<>();
    }

    public void setDataList(List<Object> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof String) {
            return DATE_TYPE;
        } else {
            return CONTENT_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 날짜 타입
        if (viewType == DATE_TYPE) {
            return new DateViewHolder(layoutInflater.inflate(R.layout.item_to_do_list_header, parent, false));
        } else {
            // 내용 타입
            return new ContentViewHolder(layoutInflater.inflate(R.layout.item_to_do_list_all, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        // 날짜 타입
        if (viewType == DATE_TYPE) {
            DateViewHolder viewHolder = (DateViewHolder) holder;
            Object item = mData.get(position);
            ToDoListHeader model = new ToDoListHeader();

            // String type 의 현재시간
            if (item instanceof String) {
                model.setHeader((String) item);
            }
            // view 에 표시
            viewHolder.bind(model);
        } else if (viewType == CONTENT_TYPE) {
            // 내용 타입
            ContentViewHolder viewHolder = (ContentViewHolder) holder;
            Object item = mData.get(position);
            ToDoListData model = new ToDoListData();
            if (item instanceof ToDoListData) {
                model = (ToDoListData) item;
            }
            viewHolder.bind(model);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setChecked(int position) {
        Object item = mData.get(position);
        if (item instanceof ToDoListData) {
            ToDoListData data = (ToDoListData) item;
            boolean state = !data.isFinishState();
            data.setFinishState(state);
            mData.set(position, data);
        }
        notifyItemChanged(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Object item = mData.get(fromPosition);
        if (item instanceof ToDoListData) {
            // 이동할 객체 저장
            ToDoListData data = (ToDoListData) mData.get(fromPosition);
            // 이동할 객체 삭제
            mData.remove(fromPosition);
            // 이동하고 싶은 position 에 추가
            mData.add(toPosition, data);

            // Adapter 에 데이터 이동알림
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelectedChange(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackground(null);
    }

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

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        // 날짜 타입 ViewHolder
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_item_to_do_list_header_title)
        TextView headerTitle;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ToDoListViewModel model) {
            // 일자 값 가져오기
            String date = ((ToDoListHeader) model).getHeader();

            // header 에 표시
            headerTitle.setText(date);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        // 내용 타입 ViewHolder
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.cb_item_to_do_list_all_check_box)
        CheckBox checkBox;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_item_to_do_list_all_content)
        TextView content;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clickView(itemView, checkBox);
        }

        public void bind(ToDoListViewModel model) {
            // 완료 여부 체크
            boolean checked = ((ToDoListData) model).isFinishState();
            checkBox.setChecked(checked);

            // 내용 표시
            String data = ((ToDoListData) model).getContent();
            content.setText(data);
            if (checked) {
                content.setPaintFlags(content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                content.setTextColor(mContext.getColor(R.color.light_grey));
            } else {
                content.setPaintFlags(0);
                content.setTextColor(mContext.getColor(R.color.default_text_color));
            }
        }

        public void clickView(View itemView, CheckBox checkBox) {
            /*itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });*/
            checkBox.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onClick(view, pos);
                }
            });
        }
    }
}

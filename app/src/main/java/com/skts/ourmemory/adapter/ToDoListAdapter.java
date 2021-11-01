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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Object> getData() {
        return mData;
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

    /**
     * ToDoList 체크 후 데이터 반환
     */
    public ToDoListData setChecked(int position) {
        Object item = mData.get(position);
        ToDoListData data = null;
        if (item instanceof ToDoListData) {
            data = (ToDoListData) item;
            boolean state = !data.isFinishState();
            data.setFinishState(state);
            mData.set(position, data);
        }
        notifyItemChanged(position);
        return data;
    }

    public ToDoListData getItem(int position) {
        Object item = mData.get(position);
        ToDoListData data = null;
        if (item instanceof ToDoListData) {
            data = (ToDoListData) item;
        }
        return data;
    }

    public void addItem(List<Object> data) {
        boolean check = false;

        String date = (String) data.get(0);      // 날짜
        ToDoListData toDoListData = (ToDoListData) data.get(1);     // 데이터

        for (int i = 0; i < mData.size(); i++) {
            Object item = mData.get(i);
            if (item instanceof String) {
                // 날짜
                int diffDay = calcTime(date, (String) item);
                if (diffDay <= -1) {
                    // 하루 전날 일 경우
                    if (!mData.contains(data.get(0))) {
                        // 겹치는 날짜가 없으면
                        mData.add(i, data.get(0));
                        mData.add(i + 1, toDoListData);
                    } else {
                        // 겹치는 날짜가 있으면
                        mData.add(i, toDoListData);
                    }

                    check = true;
                    break;
                }
            }
        }

        if (!check) {
            if (!mData.contains(data.get(0))) {
                // 겹치는 날짜가 없으면
                mData.add(data.get(0));
            }
            mData.add(toDoListData);
        }

        notifyDataSetChanged();
    }

    public void deleteItem(int toDoId) {
        int index = 0;

        // 데이터 삭제
        for (int i = 0; i < mData.size(); i++) {
            Object item = mData.get(i);
            if (item instanceof ToDoListData) {
                if (((ToDoListData) item).getToDoListId() == toDoId) {
                    // 삭제
                    mData.remove(i);
                    // 인덱스 저장
                    index = i;
                    break;
                }
            }
        }

        // 삭제한 데이터가 리스트의 마지막일 경우
        if (mData.size() == index) {
            Object item = mData.get(index - 1);
            // 앞의 데이터가 날짜값이 오면
            // 즉, 해당 날짜의 데이터가 없다는 뜻이므로
            // 날짜 데이터 삭제
            if (item instanceof String) {
                mData.remove(index - 1);
            }
        } else {
            Object prevItem = mData.get(index - 1);
            Object nextItem = mData.get(index);
            // 데이터 삭제한 자리와 앞의 데이터가 날짜값이 오면
            // 즉, 해당 날짜의 데이터가 없다는 뜻이므로
            // 날짜 데이터 삭제
            if ((prevItem instanceof String) && (nextItem instanceof String)) {
                mData.remove(index - 1);
            }
        }

        notifyDataSetChanged();
    }

    public int calcTime(String dateStr1, String dateStr2) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = format.parse(dateStr1);
            date2 = format.parse(dateStr2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert date1 != null;
        assert date2 != null;

        long diffSec = (date1.getTime() - date2.getTime()) / 1000;
        return (int) (diffSec / (24 * 60 * 60));        // 일자 수 차이
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
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });

            checkBox.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onClick(view, pos);
                }
            });
        }
    }
}

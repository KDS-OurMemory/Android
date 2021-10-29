package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class ToDoListPostResult extends BasePostResult {
    @SerializedName("response")
    private List<ResponseValue> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public List<ResponseValue> getResponse() {
        return response;
    }

    public static class ResponseValue {
        @SerializedName("todoId")
        private int todoId;
        @SerializedName("writerId")
        private int writerId;
        @SerializedName("contents")
        private String contents;
        @SerializedName("todoDate")
        private String todoDate;

        public int getTodoId() {
            return todoId;
        }

        public int getWriterId() {
            return writerId;
        }

        public String getContents() {
            return contents;
        }

        public String getTodoDate() {
            return todoDate;
        }
    }
}

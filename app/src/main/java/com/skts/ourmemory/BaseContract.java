package com.skts.ourmemory;

import android.content.Context;

public class BaseContract {

    // View 와 Presenter 는 1:1로 연결되기 때문에 뷰의 연결과 해제를 위한 함수를 정해줍니다.
    public interface Presenter<T> {
        void setView(T View);       // 뷰 연결
        void releaseView();         // 뷰 해제
        boolean isDuplicate();      // 중복 클릭 제거
    }

    // View 의 공용 함수가 필요한 경우 정해줍니다.
    public interface View {
    }
}

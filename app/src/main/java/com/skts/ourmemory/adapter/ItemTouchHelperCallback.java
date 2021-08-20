package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ButtonsState buttonsState = ButtonsState.GONE;
    private static final float BUTTON_WIDTH = 200;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private Canvas mCanvas;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            listener.onItemSelectedChange(viewHolder);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        listener.onItemClear(viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // 아이템이 스와이프 됐을 경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        mCanvas = canvas;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (buttonsState != ButtonsState.GONE) {
                if (buttonsState == ButtonsState.LEFT_VISIBLE) {
                    dX = Math.max(dX, BUTTON_WIDTH);
                }
                if (buttonsState == ButtonsState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -BUTTON_WIDTH);
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            if (buttonsState == ButtonsState.GONE) {
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currentItemViewHolder = viewHolder;

        // 버튼을 그려주는 함수
        drawButtons(canvas, currentItemViewHolder);
    }

    /**
     * 버튼을 그려주는 함수
     */
    private void drawButtons(Canvas canvas, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithOutPadding = BUTTON_WIDTH - 10;
        float corners = 5;

        View itemView = viewHolder.itemView;
        Paint paint = new Paint();

        buttonInstance = null;

        if (buttonsState == ButtonsState.LEFT_VISIBLE) {
            // 오른쪽으로 스와이프 했을 때 (왼쪽에 버튼이 보여지게 될 경우)
            RectF leftButton = new RectF(itemView.getLeft() + 10, itemView.getTop() + 10, itemView.getLeft() + buttonWidthWithOutPadding, itemView.getBottom() - 10);
            paint.setColor(Color.BLUE);
            //canvas.drawRoundRect(leftButton, corners, corners, paint);
            mCanvas.drawRoundRect(leftButton, corners, corners, paint);
            //drawText("고정", canvas, leftButton, paint);
            drawText("고정", mCanvas, leftButton, paint);
            buttonInstance = leftButton;
        } else if (buttonsState == ButtonsState.RIGHT_VISIBLE) {
            // 왼쪽으로 스와이프 했을 때 (오른쪽에 버튼이 보여지게 될 경우)
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithOutPadding, itemView.getTop() + 10, itemView.getRight() - 10, itemView.getBottom() - 10);
            paint.setColor(Color.RED);
            canvas.drawRoundRect(rightButton, corners, corners, paint);
            drawText("삭제", canvas, rightButton, paint);
            buttonInstance = rightButton;
        }
    }

    /**
     * 버튼의 텍스트 그려주기
     */
    private void drawText(String text, Canvas canvas, RectF button, Paint paint) {
        float textSize = 25;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        float textWidth = paint.measureText(text);
        canvas.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), paint);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas canvas, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((view, motionEvent) -> {
            swipeBack = motionEvent.getAction() == MotionEvent.ACTION_CANCEL || motionEvent.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack) {
                if (dX < -BUTTON_WIDTH) {
                    buttonsState = ButtonsState.RIGHT_VISIBLE;
                } else if (dX > BUTTON_WIDTH) {
                    buttonsState = ButtonsState.LEFT_VISIBLE;
                }

                if (buttonsState != ButtonsState.GONE) {
                    setTouchDownListener(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas canvas, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas canvas, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((view, motionEvent) -> {
            ItemTouchHelperCallback.super.onChildDraw(canvas, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
            recyclerView.setOnTouchListener((view1, motionEvent1) -> false);

            setItemsClickable(recyclerView, true);
            swipeBack = false;

            if (listener != null & buttonInstance != null && buttonInstance.contains(motionEvent.getX(), motionEvent.getY())) {
                if (buttonsState == ButtonsState.LEFT_VISIBLE) {
                    listener.onLeftClick(viewHolder.getAdapterPosition(), viewHolder);
                } else if (buttonsState == ButtonsState.RIGHT_VISIBLE) {
                    listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);
                }
            }

            buttonsState = ButtonsState.GONE;
            currentItemViewHolder = null;
            return false;
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }
}

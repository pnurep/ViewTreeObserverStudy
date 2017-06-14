package com.android.duse.gold.viewtreeobserverstudy;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mEditText;

    /*
     ViewTreeObserve의 유용한 리스너
     interface	ViewTreeObserver.OnDrawListener	뷰를 그릴 때
     interface	ViewTreeObserver.OnGlobalFocusChangeListener	전체 뷰의 포커스가 바뀔 때
     interface	ViewTreeObserver.OnGlobalLayoutListener	전체 뷰가 그려질 때
     interface	ViewTreeObserver.OnPreDrawListener	뷰가 그려지기 전
     interface	ViewTreeObserver.OnScrollChangedListener	스크로 상태의 변경시
     interface	ViewTreeObserver.OnTouchModeChangeListener	터치 모드 변경시
     interface	ViewTreeObserver.OnWindowAttachListener	뷰의 계층구조에 붙을 때와 떨어져 나갈때
     interface	ViewTreeObserver.OnWindowFocusChangeListener	윈도우 포커스 변경시
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.et_1);

        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkSoftKeyboardOnActivity(this, new OnKeyboardVisibility() {
            @Override
            public void onKeyboardShown(int keyboardHeight) {
                Toast.makeText(MainActivity.this, "onKeyboardShown", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onKeyboardHidden() {
                Toast.makeText(MainActivity.this, "onKeyboardHidden", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnKeyboardVisibility {
        void onKeyboardShown(int keyboardHeight);

        void onKeyboardHidden();
    }

    private void checkSoftKeyboardOnActivity(Activity activity, final OnKeyboardVisibility visibilityListener) {

        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        final View decorView = activity.getWindow().getDecorView();

         //ViewTree의 View가 그려질때 마다...
        mEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            // 보여지고 있는 window의 크기를 사각형 Rect객체로 가져 온다.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {

                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                // 보여지고 있는 높이의 계산 결과에 따라 키보드의 등장 유무를 확인 한다.
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        // 키보드 높이 계산(전체화면 모드일 때 네비게이션 바 높이 계산하는 것도 포함)
                        int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                        visibilityListener.onKeyboardShown(currentKeyboardHeight);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        visibilityListener.onKeyboardHidden();
                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight;

//                mEditText.getWidth();
//                mEditText.getHeight();
//                mEditText.getX();
//                mEditText.getY();

                // 단발성의 작업이 필요해서 리스너를 붙였을 때에는 작업의 마지막에 리스너를 해제해 주어 호출을 받아오지 않게 하자.
                // mEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


         //ViewTree의 포커스 변경이 있을 경우
        mEditText.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

                // 리스너해제
                // mEditText.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
            }
        });
    }
}

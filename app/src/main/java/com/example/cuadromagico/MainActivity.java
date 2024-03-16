package com.example.cuadromagico;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button[][] buttons = new Button[3][3];
    private int emptyRow = 2;
    private int emptyCol = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + (i * 3 + j + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnTouchListener(new TouchListener(i, j));
            }
        }
    }

    private class TouchListener implements View.OnTouchListener {
        private int row;
        private int col;
        private GestureDetector gestureDetector;

        TouchListener(int row, int col) {
            this.row = row;
            this.col = col;
            gestureDetector = new GestureDetector(MainActivity.this, new GestureListener());
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            moveButton(row, col - 1);
                        } else {
                            moveButton(row, col + 1);
                        }
                        return true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            moveButton(row - 1, col);
                        } else {
                            moveButton(row + 1, col);
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private void moveButton(int newRow, int newCol) {
        if (isValidPosition(newRow, newCol)) {
            Button buttonToMove = buttons[newRow][newCol];
            Button emptyButton = buttons[emptyRow][emptyCol];
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) buttonToMove.getLayoutParams();
            params.rowSpec = GridLayout.spec(emptyRow);
            params.columnSpec = GridLayout.spec(emptyCol);
            buttonToMove.setLayoutParams(params);
            emptyRow = newRow;
            emptyCol = newCol;
            buttons[newRow][newCol] = emptyButton;
            buttons[emptyRow][emptyCol] = buttonToMove;
            checkForWin();
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    private void checkForWin() {
        // Implementa la lógica para verificar si el jugador ha ganado
        // Esto implica comparar el contenido de los botones con el cuadro mágico
        // Si todos los números están en el lugar correcto, el jugador gana
    }
}

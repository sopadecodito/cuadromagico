package com.example.cuadromagico;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

public class DraggableButton extends AppCompatButton {
    private float lastX;
    private float lastY;
    private boolean isDragging = false;
    private float initialX;
    private float initialY;

    public DraggableButton(Context context) {
        super(context);
    }

    public DraggableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getRawX();
        float currentY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = currentX;
                lastY = currentY;
                initialX = getX();
                initialY = getY();
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float deltaX = currentX - lastX;
                    float deltaY = currentY - lastY;

                    float newX = getX() + deltaX;
                    float newY = getY() + deltaY;

                    // Limitar la posición del botón para que no se salga del grid
                    if (getParent() instanceof ViewGroup) {
                        ViewGroup parent = (ViewGroup) getParent();
                        newX = Math.max(0, Math.min(newX, parent.getWidth() - getWidth()));
                        newY = Math.max(0, Math.min(newY, parent.getHeight() - getHeight()));
                    }

                    setX(newX);
                    setY(newY);

                    lastX = currentX;
                    lastY = currentY;
                }
                break;

            case MotionEvent.ACTION_UP:
                isDragging = false;
                // Verificar si el botón está dentro del grid
                if (getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) getParent();
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View child = parent.getChildAt(i);
                        if (child instanceof DraggableButton && child != this) {
                            Rect rect = new Rect();
                            child.getHitRect(rect);
                            if (rect.contains((int) (initialX + getWidth() / 2), (int) (initialY + getHeight() / 2))) {
                                // Intercambiar posiciones con el botón encontrado
                                float tempX = getX();
                                float tempY = getY();
                                setX(child.getX());
                                setY(child.getY());
                                child.setX(tempX);
                                child.setY(tempY);
                                break;
                            }
                        }
                    }
                }
                break;

            default:
                return super.onTouchEvent(event);
        }
        return true;
    }
}

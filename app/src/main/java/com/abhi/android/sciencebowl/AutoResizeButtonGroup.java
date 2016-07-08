package com.abhi.android.sciencebowl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tanuj on 7/7/2016.
 */
public class AutoResizeButtonGroup extends LinearLayout{

    List<AutoResizeButton> buttons;
    static int height = 1000;
    static int width = 1000;

    public AutoResizeButtonGroup(Context context) {
        super(context);
        buttons = new LinkedList<AutoResizeButton>();
        int n = this.getChildCount();
        for(int i = 0;i<n;i++){
            View v = this.getChildAt(i);
            if(v.getClass().equals(AutoResizeButton.class)){
                buttons.add((AutoResizeButton)v);
            }
        }
    }

    public AutoResizeButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buttons = new LinkedList<AutoResizeButton>();
        int n = this.getChildCount();
        for(int i = 0;i<n;i++){
            View v = this.getChildAt(i);
            if(v.getClass().equals(AutoResizeButton.class)){
                buttons.add((AutoResizeButton)v);
            }
        }
    }

    /*public AutoResizeButtonGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, List<AutoResizeButton> buttons) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.buttons = buttons;
    }*/

    public AutoResizeButtonGroup(Context context, AttributeSet s) {
        super(context,s);
        buttons = new LinkedList<AutoResizeButton>();
        int n = this.getChildCount();
        for(int i = 0;i<n;i++){
            View v = this.getChildAt(i);
            if(v.getClass().equals(AutoResizeButton.class)){
                buttons.add((AutoResizeButton)v);
            }
        }
    }


   void update(){
       for(AutoResizeButton b : buttons){
           b.resizeText(width,height);
       }
   }
}

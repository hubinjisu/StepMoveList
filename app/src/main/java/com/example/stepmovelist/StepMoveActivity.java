package com.example.stepmovelist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class StepMoveActivity extends Activity implements OnClickListener
{
    private ListView listView;
    // arrow up
    private ImageView arrowUp;
    // arrow down
    private ImageView arrowDown;
    private Button upBtn;
    private Button downBtn;

    private int currentPosition;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listSrc = new ArrayList<String>();
    private List<String> listItems;
    private int moveOffset = 0;
    // max line of the displayed list
    private int maxRow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.step_move_layout);
        listView = (ListView) findViewById(R.id.list_call_log_detail);
        arrowUp = (ImageView) findViewById(R.id.arrow_up);
        arrowDown = (ImageView) findViewById(R.id.arrow_down);
        upBtn = (Button) findViewById(R.id.up_btn);
        downBtn = (Button) findViewById(R.id.down_btn);
        upBtn.setOnClickListener(this);
        downBtn.setOnClickListener(this);

        initData();
    }

    private void initData()
    {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels; // the screen height (pixel)
        // current list item height is 48px, count the max line to fit the screen height
        maxRow = height / 48;
        maxRow = 11;
        Log.i("maxRow", "maxRow: " + maxRow);

        for (int i = 0; i < 50; i++)
        {
            listSrc.add("test" + i);
        }
        listItems = new ArrayList<String>();
        for (String item : (listSrc.size() >= maxRow ? listSrc.subList(0, maxRow) : listSrc))
        {
            listItems.add(item);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, listItems);
        listView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                listView.requestFocusFromTouch();
                listView.setSelection(currentPosition);
            }
        }, 500);
        updateUpDownFlag(moveOffset, maxRow, listSrc.size(), arrowUp, arrowDown);
    }

    private void updateUpDownFlag(int moveOffset, int maxLine, int listSize, ImageView arrowUp, ImageView arrowDown)
    {
        if (moveOffset > 0)
        {
            arrowUp.setVisibility(View.VISIBLE);
        }
        else
        {
            arrowUp.setVisibility(View.GONE);
        }

        if (listSize > moveOffset + maxLine)
        {
            arrowDown.setVisibility(View.VISIBLE);
        }
        else
        {
            arrowDown.setVisibility(View.GONE);
        }
    }

    private void moveUp()
    {
        if (currentPosition > 0)
        {
            listView.requestFocusFromTouch();
            listView.setSelection(--currentPosition);
        }
        else
        {
            if (moveOffset > 0)
            {
                moveOffset--;
                listItems = new ArrayList<String>();
                for (String item : listSrc.subList(moveOffset, maxRow + moveOffset))
                {
                    listItems.add(item);
                }
                adapter.clear();
                adapter.addAll(listItems);
                updateUpDownFlag(moveOffset, maxRow, listSrc.size(), arrowUp, arrowDown);
            }
            listView.requestFocusFromTouch();
            listView.setSelection(currentPosition);
        }
    }

    private void moveDown()
    {
        if (currentPosition < adapter.getCount() - 1)
        {
            listView.requestFocusFromTouch();
            listView.setSelection(++currentPosition);
        }
        else
        {
            if (maxRow + moveOffset < listSrc.size())
            {
                moveOffset++;
                listItems = new ArrayList<String>();
                for (String item : listSrc.subList(moveOffset, maxRow + moveOffset))
                {
                    listItems.add(item);
                }
                adapter.clear();
                adapter.addAll(listItems);
                updateUpDownFlag(moveOffset, maxRow, listSrc.size(), arrowUp, arrowDown);
            }
            listView.requestFocusFromTouch();
            listView.setSelection(currentPosition);
        }
    }

    @Override
    public void onClick(View v)
    {
        Log.i("onClick", "position:" + currentPosition);
        switch (v.getId())
        {
            case R.id.up_btn:
                moveUp();
                break;
            case R.id.down_btn:
                moveDown();
                break;
            default:
                break;
        }
    }
}

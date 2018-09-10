package com.turvey.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.turvey.cbs.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    //LineChart
    private LineChart statistics;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homedrawercontent, container, false);
        statistics = (LineChart) view.findViewById(R.id.statisticschart);
        drawChart();
        return view;
    }

    private void drawChart() {
        //LineChart
        statistics.setDragEnabled(true);
        statistics.setScaleEnabled(true);
        //y Values
        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 140f));
        yValues.add(new Entry(1, 135f));
        yValues.add(new Entry(2, 145f));
        yValues.add(new Entry(3, 150f));
        yValues.add(new Entry(4, 175f));
        yValues.add(new Entry(5, 190f));
        LineDataSet set1 = new LineDataSet(yValues, "LF6");
        set1.setFillAlpha(860);
        set1.setColor(Color.BLUE);
        set1.setLineWidth(2f);
        set1.setValueTextSize(12f);
        set1.setCircleColor(Color.BLUE);
        set1.setCircleSize(5f);
        //
        ArrayList<Entry> yValues1 = new ArrayList<>();
        yValues1.add(new Entry(0, 130f));
        yValues1.add(new Entry(1, 125f));
        yValues1.add(new Entry(2, 115f));
        yValues1.add(new Entry(3, 120f));
        yValues1.add(new Entry(4, 145f));
        yValues1.add(new Entry(5, 150f));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        statistics.setDescription(null);
        statistics.setData(data);
        statistics.getAxisRight().setDrawLabels(false);
        statistics.getXAxis().setDrawLabels(false);
    }
}

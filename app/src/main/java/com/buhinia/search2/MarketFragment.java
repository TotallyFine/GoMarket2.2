package com.buhinia.search2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/*
* 此fragment为超市界面的fragment
* */

public class MarketFragment extends Fragment {


    private List<MarketListItem> marketList=new ArrayList<>();
    private MarketAdapter marketAdapter1;

    //下拉刷新layout
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_market,container,false);

        //初始化数据库
        LitePal.getDatabase();
        marketList= DataSupport.findAll(MarketListItem.class);


        RecyclerView recyclerView1=(RecyclerView)view.findViewById(R.id.market_fragment1_recycler_view);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        marketAdapter1=new MarketAdapter(marketList);
        recyclerView1.setAdapter(marketAdapter1);

        //下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.market_fragment_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh(){
                Refresh();
            }
        });


        return view;
    }

    //刷新数据
    private void Refresh(){
        //示例开启一个线程假装刷新
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

        Toast.makeText(getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
        //刷新完成
        swipeRefreshLayout.setRefreshing(false);
    }


}

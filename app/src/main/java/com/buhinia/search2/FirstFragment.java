package com.buhinia.search2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.telephony.CellSignalStrengthWcdma;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;



/*
* 此fragment为首页的fragment
* */
public class FirstFragment extends Fragment {


    //这个为猜你喜欢的商品
    private List<GoodListItem> goodList=new ArrayList<>();
    private GoodAdapter goodAdapter;


    //这个为推荐商家
    private List<MarketListItem> marketList=new ArrayList<>();
    private MarketAdapter marketAdapter;

    //下拉刷新layout
    private SwipeRefreshLayout swipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //初始化数据库
        LitePal.getDatabase();
        goodList= DataSupport.findAll(GoodListItem.class);
        marketList=DataSupport.findAll(MarketListItem.class);


        //这个为猜你喜欢的商品
        View view=inflater.inflate(R.layout.fragment_first,container,false);
        RecyclerView recyclerView1=(RecyclerView)view.findViewById(R.id.first_market_recycler_view);
        LinearLayoutManager layoutManager1=new LinearLayoutManager(view.getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        marketAdapter=new MarketAdapter(marketList);
        recyclerView1.setAdapter(marketAdapter);


        //这个为推荐商家
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.first_good_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        goodAdapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(goodAdapter);

        //下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.first_swipe_refresh);
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
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Toast.makeText(getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
        //刷新完成
        swipeRefreshLayout.setRefreshing(false);
    }


}

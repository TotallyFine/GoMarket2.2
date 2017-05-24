package com.buhinia.search2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * 此fragment为浏览历史的fragment
 */

public class HistoryFragment extends Fragment {
    /*示例数据*/

    private List<GoodListItem> goodList=new ArrayList<>();
    private List<GoodListItem> temList=new ArrayList<>();
    private List<BrowseHistory> historyList=new ArrayList<>();
    private GoodAdapter adapter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,container,false);

        //获得浏览历史
        LitePal.getDatabase();
        historyList=DataSupport.findAll(BrowseHistory.class);
        if (historyList!=null){
            for (BrowseHistory history:historyList){
                temList=DataSupport.where("goodId==?",String.valueOf(history.getGoodId())).find(GoodListItem.class);
                if (temList.size()!=0){
                    goodList.add(temList.get(0));
                }
            }
        }



        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        /*getActivity();??*/
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);

        //清空列表
        goodList=new ArrayList<>();

        return view;
    }



}

package com.buhinia.search2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;



/*recycler view的点击事件是在adapter中设置的*/
public class SearchGood extends AppCompatActivity {


    private List<GoodListItem> goodList=new ArrayList<>();
    private List<GoodListItem> temgoodList=new ArrayList<>();
    private String keyWords=null;
    private GoodAdapter adapter;
    private boolean fromMarket=false;
    private long marketId;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);

        Intent intent=getIntent();
        marketId=intent.getLongExtra("marketId",-1);
        if (marketId==-1){
            fromMarket=false;
        }else {
            fromMarket=true;

        }

        //载入数据库
        LitePal.getDatabase();
        if (fromMarket){
            goodList=DataSupport.where("marketId==?",String.valueOf(marketId)).find(GoodListItem.class);
        }else{
            goodList=DataSupport.findAll(GoodListItem.class);
        }



/*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*用于筛选的三个按钮*/
        Button bt_hot=(Button) findViewById(R.id.hot);
        Button bt_near=(Button) findViewById(R.id.near);
        Button bt_cheap=(Button)findViewById(R.id.cheap);


        editText=(EditText)findViewById(R.id.text_search);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);


        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        final ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*下面执行筛选*/
        bt_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).order("goodClickTimes desc").find(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).where("goodName like ?","%"+keyWords+"%").order("goodClickTimes desc").find(GoodListItem.class));
                    }

                }else {
                    if (keyWords==null){
                        goodList.addAll(DataSupport.order("goodClickTimes desc").find(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("goodName like ?","%"+keyWords+"%").order("goodClickTimes desc").find(GoodListItem.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_cheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).order("goodPrice").find(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).where("goodName like ?","%"+keyWords+"%").order("goodPrice").find(GoodListItem.class));
                    }

                }else {
                    if (keyWords==null){
                        goodList.addAll(DataSupport.order("goodPrice").find(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("goodName like ?","%"+keyWords+"%").order("goodPrice").find(GoodListItem.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodList.clear();
                if (fromMarket){
                    if (keyWords==null){
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).find(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).where("goodName like ?","%"+keyWords+"%").find(GoodListItem.class));
                    }

                }else {
                    if (keyWords==null){
                        goodList.addAll(DataSupport.findAll(GoodListItem.class));
                    }else{
                        goodList.addAll(DataSupport.where("goodName like ?","%"+keyWords+"%").find(GoodListItem.class));
                    }

                }
                distanceSort();
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void distanceSort(){
        for (GoodListItem good:goodList){
            MarketListItem market=DataSupport.where("marketId==?",String.valueOf(good.getMarketId())).find(MarketListItem.class).get(0);
            good.setGoodDistance(Distance.getDistance(MainActivity.userLocation,market.getMarketLatitude(),market.getMarketLongitude()));
        }
        goodList.sort(new Comparator<GoodListItem>() {
            @Override
            public int compare(GoodListItem goodListItem, GoodListItem t1) {
                if (goodListItem.getGoodDistance()>t1.getGoodDistance()) return 1;
                else if (goodListItem.getGoodDistance()==t1.getGoodDistance()) return 0;
                else return -1;
            }
        });

    }





    /*执行搜索,在onOptionsItemSelected方法中调用此函数*/
    private void searchGood(String query){
        goodList.clear();
        if (fromMarket){
            goodList.addAll(DataSupport.where("marketId==?",String.valueOf(marketId)).where("goodName like ?","%"+query+"%").find(GoodListItem.class));
        }else{
            goodList.addAll(DataSupport.where("goodName like ?","%"+query+"%").find(GoodListItem.class));
        }
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_common,menu);
        return true;
    }

    /*设置toolbar的点击事件，即左上角的返回按钮，其id是系统的资源id，不会变化*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search_icon:
                String text=editText.getText().toString();
                if (!TextUtils.isEmpty(text)){
                    text.replaceAll(" ","");
                    keyWords=text;
                    searchGood(keyWords);
                }
                break;
        }
        return true;
    }
}



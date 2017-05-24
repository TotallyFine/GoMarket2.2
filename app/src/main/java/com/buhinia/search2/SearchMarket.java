package com.buhinia.search2;

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

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



/*recycler view的点击事件是在adapter中设置的*/
public class SearchMarket extends AppCompatActivity {

    private List<MarketListItem> marketList=new ArrayList<>();
    private MarketAdapter adapter;
    private EditText editText;
    private String keyWords=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_market);

        LitePal.getDatabase();
        marketList= DataSupport.findAll(MarketListItem.class);


/*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_market);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*用于筛选的三个按钮*/
        Button bt_hot=(Button)findViewById(R.id.hot_market);
        Button bt_near=(Button)findViewById(R.id.near_market);
        Button bt_cheap=(Button)findViewById(R.id.cheap_market);

        editText=(EditText)findViewById(R.id.text_search_market);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view_market);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MarketAdapter(marketList);
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
                marketList.clear();
                if (keyWords==null){
                    marketList.addAll(DataSupport.order("marketClickTimes desc").find(MarketListItem.class));
                }else{
                    marketList.addAll(DataSupport.where("marketName like ?","%"+keyWords+"%").order("marketClickTimes desc").find(MarketListItem.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
        bt_cheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bt_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketList.clear();
                if (keyWords==null){
                    marketList.addAll(DataSupport.findAll(MarketListItem.class));
                }else{
                    marketList.addAll(DataSupport.where("marketName like ?","%"+keyWords+"%").find(MarketListItem.class));
                }
                distanceSort();
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void distanceSort(){
        for (MarketListItem market:marketList){
            market.setMarketDistance(Distance.getDistance(MainActivity.userLocation,market.getMarketLatitude(),market.getMarketLongitude()));
        }
        marketList.sort(new Comparator<MarketListItem>() {
            @Override
            public int compare(MarketListItem marketListItem, MarketListItem t1) {
                if (marketListItem.getMarketDistance()>t1.getMarketDistance()) return 1;
                else if (marketListItem.getMarketDistance()==t1.getMarketDistance()) return 0;
                else return 0;
            }
        });
    }

    /*执行搜索,在onOptionsItemSelected方法中调用此函数*/
    private void searchMarket(String query){
        marketList.clear();
        marketList.addAll(DataSupport.where("marketName like ?","%"+query+"%").find(MarketListItem.class));
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
                    searchMarket(text);
                }
                break;
        }
        return true;
    }

}

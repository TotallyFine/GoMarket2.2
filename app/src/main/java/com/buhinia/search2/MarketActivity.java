package com.buhinia.search2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarketActivity extends AppCompatActivity {



    private List<GoodListItem> goodList=new ArrayList<>();
    private List<MarketListItem> marketList=new ArrayList<>();
    private GoodAdapter adapter;
    private long marketId;

    //店铺图片
    private ImageView marketImage;
    //店铺位置
    private TextView marketSite;
    //店铺距离
    private TextView marketDistance;
    //店铺电话
    private TextView marketPhone;
    //店铺名称
    private TextView marketName;
    //电话图标
    private ImageButton phoneIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        //获得数据
        Intent intent=getIntent();
        LitePal.getDatabase();
        marketId=intent.getLongExtra("marketId", 0);

        goodList= DataSupport.where("marketId==?",String.valueOf(marketId)).find(GoodListItem.class);
        marketList=DataSupport.where("marketId==?",String.valueOf(marketId)).find(MarketListItem.class);
        MarketListItem market=marketList.get(0);

        //对点击量进行记录
        market.setMarketClickTimes(market.getMarketClickTimes()+1);
        market.save();

        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用EditText来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_market);
        toolbar.setTitle("店铺详情");
        setSupportActionBar(toolbar);

        marketName=(TextView)findViewById(R.id.market_text);
        marketDistance=(TextView)findViewById(R.id.market_distance);
        marketImage=(ImageView)findViewById(R.id.market_image);
        marketPhone=(TextView)findViewById(R.id.market_phone);
        marketSite=(TextView)findViewById(R.id.market_site);
        phoneIcon=(ImageButton)findViewById(R.id.market_phone_icon);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_view_market_good);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GoodAdapter(goodList);
        recyclerView.setAdapter(adapter);


        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //点击电话按钮打电话
        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+marketPhone.getText().toString()));
                startActivity(intent);
            }
        });

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
                /*在进行此次搜索时应考虑到是在本超市内搜索*/
                Intent intent=new Intent(this,SearchGood.class);
                intent.putExtra("marketId",marketId);
                startActivity(intent);
                break;
        }
        return true;
    }
}

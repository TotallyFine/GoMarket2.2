package com.buhinia.search2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


/*当直接在首页或者搜索界面中点击某个商品时就会跳转到此界面，同时传入一个Intent，或是其他的提取数据的方法
*
* 在本活动中goodMarketSite与进入超市按钮时是可以点击的
*
* goodMarketSite点击后进入地图
* */



public class GoodActivity extends AppCompatActivity {

    ImageView goodImage;
    TextView goodText;
    TextView goodPrice;
    TextView goodDistance;
    TextView goodMarketName;
    TextView goodMarketSite;
    TextView goodMarketPhone;
    ImageButton phoneIcon;
    //TextView enterMarket; 改成了点击超市名称进入超市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

        //获得goodId与marketId
        Intent intent=getIntent();
        long goodId=intent.getLongExtra("goodId", 0);
        long marketId=intent.getLongExtra("marketId",0);

        //访问数据库获得数据
        LitePal.getDatabase();
        List<GoodListItem> goodList=DataSupport.where("goodId==?",String.valueOf(goodId)).find(GoodListItem.class);
        List<MarketListItem> marketList=DataSupport.where("marketId==?",String.valueOf(marketId)).find(MarketListItem.class);
        GoodListItem good=goodList.get(0);
        final MarketListItem market=marketList.get(0);

        //对浏览历史和点击量进行记录
        BrowseHistory history=new BrowseHistory(good.getGoodId());
        if (DataSupport.where("goodId==?",String.valueOf(history.getGoodId())).find(BrowseHistory.class).size()==0){
            history.save();
        }
        good.setGoodClickTimes(good.getGoodClickTimes()+1);
        good.save();


        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用Text来代替*/
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_good);
        toolbar.setTitle("商品详情");
        setSupportActionBar(toolbar);

        /*得到actionbar的实例并通过设置将左上角的返回按钮显示出来*/
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*初始化数据*/
        goodImage=(ImageView)findViewById(R.id.good_image);
        goodText=(TextView)findViewById(R.id.good_text);
        goodPrice=(TextView)findViewById(R.id.good_price);
        goodDistance=(TextView)findViewById(R.id.good_distance);
        goodMarketName=(TextView)findViewById(R.id.good_market_name);
        goodMarketPhone=(TextView)findViewById(R.id.good_market_phone);
        goodMarketSite=(TextView)findViewById(R.id.good_market_site);
        phoneIcon=(ImageButton)findViewById(R.id.phone_icon);
        //enterMarket=(TextView)findViewById(R.id.enter_market);

        Glide.with(this).load(good.getGoodImageUrl()).into(goodImage);
        goodText.setText(good.getGoodName());
        goodPrice.setText(String.valueOf(good.getGoodPrice()));
        goodDistance.setText(String.valueOf(Distance.getDistance(MainActivity.userLocation,market.getMarketLatitude(),market.getMarketLongitude())).substring(0,4).concat("km"));
        goodMarketSite.setText(market.getMarketLocation());
        goodMarketPhone.setText(String.valueOf(market.getMarketPhoneNumber()));
        goodMarketName.setText(market.getMarketName());

       goodMarketSite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(GoodActivity.this,"你点击了超市位置",Toast.LENGTH_SHORT).show();
           }
       });

        goodMarketName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GoodActivity.this,MarketActivity.class);
                intent.putExtra("marketId",market.getMarketId());
                startActivity(intent);
            }
        });

        //点击电话按钮打电话
        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+goodMarketPhone.getText().toString()));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_good,menu);
        return true;
    }

    /*设置toolbar的点击事件，即左上角的返回按钮，其id是系统的资源id，不会变化*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}

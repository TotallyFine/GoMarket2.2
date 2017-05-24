package com.buhinia.search2;

import android.Manifest;
import android.content.Intent;


import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {



    /*点点滴滴*/
    Fragment mCurrentFragment;
    //用int来代替fragment节省开销 1为first 2为market 3为history
    private int currentFragmrnt;
    private List<String> permissions=new ArrayList<>();

    protected LocationClient locationClient;
    protected static BDLocation userLocation;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化数据库
        LitePal.getDatabase();
        initDatabase();

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()){
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[permissions.size()]),1);

        }

        //初始化定位和地图客户端，开始定位
        locationClient=new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        locationClient.registerLocationListener(new userLocationListener());
        initLocationOption();
        locationClient.start();


        /*下面的两行代码为设置toolbar为actionbar的模式,并把标题设置为空，方便在xml中用TextView来代替*/
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //title
        title=(TextView)findViewById(R.id.title);


        /*下面为注册底部导航栏的实例*/
        ImageView first=(ImageView)findViewById(R.id.first_image);
        ImageView market=(ImageView)findViewById(R.id.market_image);
        ImageView history =(ImageView)findViewById(R.id.history_image);

        final Fragment first_fragment=new FirstFragment();
        final Fragment market_fragment=new MarketFragment();
        final Fragment history_fragment=new HistoryFragment();

        /*设置初始时底部导航栏的pressed状态*/
        replaceFragment(first_fragment);
        mCurrentFragment=first_fragment;
        currentFragmrnt=1;
        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);

        /*为底部导航栏各按钮设置点击切换fragment*/
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=1) {
                    title.setText("GO");
                    if(currentFragmrnt==2){
                        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_normal);
                    }
                    else if(currentFragmrnt==3){
                        first.setImageResource(R.drawable.main_bottom_tab_home_pressed);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_normal);
                    }
                    currentFragmrnt=1;
                    replaceFragment(first_fragment);
                }
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=2) {
                    title.setText("附近超市");
                    if(currentFragmrnt==1){
                        first.setImageResource(R.drawable.main_bottom_tab_home_normal);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_pressed);
                    }
                    else if(currentFragmrnt==3){
                        market.setImageResource(R.drawable.main_bottom_tab_cart_pressed);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_normal);
                    }
                    currentFragmrnt=2;
                    replaceFragment(market_fragment);
                }
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragmrnt!=3) {
                    title.setText("浏览历史");
                    if(currentFragmrnt==1){
                        first.setImageResource(R.drawable.main_bottom_tab_home_normal);
                        history.setImageResource(R.drawable.main_bottom_tab_personal_pressed);
                    }
                    else if(currentFragmrnt==2){
                        history.setImageResource(R.drawable.main_bottom_tab_personal_pressed);
                        market.setImageResource(R.drawable.main_bottom_tab_cart_normal);
                    }
                    currentFragmrnt=3;
                    replaceFragment(history_fragment);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"拒绝授予权限，退出应用",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    /*下面两个函数为加载toolbar的布局及设置点击事件*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_common,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search_icon:
                if(mCurrentFragment instanceof FirstFragment){
                    Intent intent=new Intent(this,SearchGood.class);
                    startActivity(intent);
                    break;
                }
                else if(mCurrentFragment instanceof MarketFragment){
                    Intent intent=new Intent(this,SearchMarket.class);
                    startActivity(intent);
                    break;
                }

        }
        return true;
    }

    /*我设想成按一次在主界面按一次返回键，有toast提示再按一次，待实现*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*下面的函数用于响应点击事件来切换fragment*/
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        /*开启事务，转换fragment*/
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    //定位回调监听器
    private class userLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            userLocation=bdLocation;
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    private void initLocationOption(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);
        locationClient.setLocOption(option);
    }


    //初始化数据库
    private void initDatabase(){

        GoodListItem goodItem=new GoodListItem(1,1,"富士红苹果","水果",25.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627467245&di=fda145d5ec9d0b40ac53c99ed4b8fe5a&imgtype=0&src=http%3A%2F%2Fpic40.nipic.com%2F20140412%2F9885883_212844866000_2.jpg","香甜可口");
        GoodListItem goodItem2=new GoodListItem(2,1,"新鲜香蕉","水果",23.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494222237&di=a606bcddb7def2536afd782d19d07d55&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.fx2.cn%2FUploads%2Fimage%2F20150417%2F20150417152334_91770.png","新鲜");
        GoodListItem goodItem3=new GoodListItem(3,2,"妙脆角","零食",3.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627618845&di=be7e3d45a994928a68b468d6711d96df&imgtype=0&src=http%3A%2F%2Fd6.yihaodianimg.com%2FN06%2FM0B%2F2F%2F27%2FCgQIzlRcWXaAXWemAAWtZjnhkHE59601.jpg","味道合口");
        GoodListItem goodItem4=new GoodListItem(4,2,"乐事薯片","零食",5.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627711668&di=4356841352ff5dbd3e1cf1d379714d65&imgtype=0&src=http%3A%2F%2Fimgqn.koudaitong.com%2Fupload_files%2F2015%2F07%2F03%2FFgEhn04Mvo97gxSf1bdNmfvpnjRJ.jpg%2521730x0.jpg","嘎嘣脆");
        GoodListItem goodItem5=new GoodListItem(5,2,"百世可乐","饮料",3.5,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627766239&di=e68636ea1868582b60a4934c6e97efeb&imgtype=0&src=http%3A%2F%2Fimg01.feiniu.com%2FC%2Fshow%2Fdetail%2Fimage%2F20150428%2F24f83c3a09683f895f1a56b19bd656b2.jpg","冰凉一夏");
        GoodListItem goodItem6=new GoodListItem(6,3,"烤肠","熟食",2.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627848229&di=bd374fcf7ea244205ece9d5b8b6943f2&imgtype=0&src=http%3A%2F%2Fwww.xzdcw.cn%2Fproduct_pic%2F2011631504847191.jpg","肉多实惠");
        GoodListItem goodItem7=new GoodListItem(7,3,"关东煮","熟食",5.0,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493627927004&di=73c705e40ac01c05f683a347cf2d6512&imgtype=0&src=http%3A%2F%2Fwww.websbook.com%2Fsc%2Fupimg%2Fuserup%2F1407%2F221921512544.jpg","便宜");
        if(DataSupport.where("goodId==?",String.valueOf(goodItem.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem2.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem2.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem3.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem3.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem4.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem4.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem5.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem5.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem6.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem6.save();
        }
        if(DataSupport.where("goodId==?",String.valueOf(goodItem7.getGoodId())).find(GoodListItem.class).size()==0){
            goodItem7.save();
        }
        MarketListItem marketItem1=new MarketListItem(1,"朝阳水果超市","四川省成都市武侯区新智一路8号附34号",Long.valueOf("15223542650"),"http://hiphotos.baidu.com/map/pic/item/1c950a7b02087bf4997fef97f1d3572c11dfcf39.jpg","便宜好吃",1.93827,30.762221);
        MarketListItem marketItem2=new MarketListItem(2,"校园超市11号","四川省成都市郫县S9(成灌高速)",Long.valueOf("1322355453"),"http://hiphotos.baidu.com/map/pic/item/aa64034f78f0f736f3b6ed420d55b319ebc4132c.jpg","品种齐全",10.938939,30.761584);
        MarketListItem marketItem3=new MarketListItem(3,"红砖房", "电子科技大学清水河校区游泳池附近",Long.valueOf("18723642621"),"http://hiphotos.baidu.com/lbsugc/pic/item/e850352ac65c1038f1174774b1119313b07e891f.jpg","体育课必经",23.943035,30.756041);

        if (DataSupport.where("marketId==?",String.valueOf(marketItem1.getMarketId())).find(MarketListItem.class).size()==0){
            marketItem1.save();
        }
        if (DataSupport.where("marketId==?",String.valueOf(marketItem2.getMarketId())).find(MarketListItem.class).size()==0){
            marketItem2.save();
        }
        if (DataSupport.where("marketId==?",String.valueOf(marketItem3.getMarketId())).find(MarketListItem.class).size()==0){
            marketItem3.save();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
}

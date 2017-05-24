package com.buhinia.search2;

import org.litepal.crud.DataSupport;

public class GoodListItem extends DataSupport {

    //原价
    private double goodOldPrice;
    //位置
    private String goodSite;

    private long goodId;
    private long marketId;
    private String goodName;
    private String goodType;
    private double goodPrice;
    private String goodImageUrl;
    private String goodDescription;
    private long goodClickTimes;

    //非映射的distance,只用来排序
    protected double goodDistance;

    public double getGoodDistance() {
        return goodDistance;
    }

    public void setGoodDistance(double goodDistance) {

        this.goodDistance = goodDistance;
    }



    public long getGoodClickTimes() {
        return goodClickTimes;
    }

    public void setGoodClickTimes(long goodClickTimes) {
        this.goodClickTimes = goodClickTimes;
    }


    //增加了goodSite 和 goodOldPrice 但这个我没有改 你注意一下
    public GoodListItem(long goodId, long marketId, String goodName, String goodType, double goodPrice, String goodImageUrl, String goodDescription) {
        this.goodId = goodId;
        this.marketId = marketId;
        this.goodName = goodName;
        this.goodType = goodType;
        this.goodPrice = goodPrice;
        this.goodImageUrl = goodImageUrl;
        this.goodDescription = goodDescription;
    }

    public GoodListItem(long goodId, long marketId, String goodName, String goodType, double goodPrice, String goodImageUrl) {
        this.goodId = goodId;
        this.marketId = marketId;
        this.goodName = goodName;
        this.goodType = goodType;
        this.goodPrice = goodPrice;
        this.goodImageUrl = goodImageUrl;
    }

    public long getGoodId() {
        return goodId;
    }

    public long getMarketId() {
        return marketId;
    }

    public String getGoodName() {
        return goodName;
    }

    public String getGoodType() {
        return goodType;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public String getGoodImageUrl() {
        return goodImageUrl;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public String getGoodSite(){return goodSite;}

    public double getGoodOldPrice(){return goodOldPrice;}
}


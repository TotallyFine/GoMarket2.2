package com.buhinia.search2;


import org.litepal.crud.DataSupport;


/*此为超市在搜索超市的界面中展示的子项的数据*/
public class MarketListItem extends DataSupport{
    private long marketId;
    private String marketName;
    private String marketLocation;
    private long marketPhoneNumber;
    private String marketImageUrl;
    private String marketDescription;
    private double marketLatitude;
    private double marketLongitude;
    private long marketClickTimes;

    //非映射的distance,只用来排序
    protected double marketDistance;

    public double getMarketDistance() {
        return marketDistance;
    }

    public void setMarketDistance(double marketDistance) {
        this.marketDistance = marketDistance;
    }



    public long getMarketClickTimes() {
        return marketClickTimes;
    }

    public void setMarketClickTimes(long marketClickTimes) {
        this.marketClickTimes = marketClickTimes;
    }

    public MarketListItem(long marketId, String marketName, String marketLocation, long marketPhoneNumber, String marketImageUrl, String marketDescription, double marketLatitude, double marketLongitude) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.marketLocation = marketLocation;
        this.marketPhoneNumber = marketPhoneNumber;
        this.marketImageUrl = marketImageUrl;
        this.marketDescription = marketDescription;
        this.marketLatitude = marketLatitude;
        this.marketLongitude = marketLongitude;
    }

    public MarketListItem(long marketId, String marketName, String marketLocation, long marketPhoneNumber, String marketImageUrl, String marketDescription) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.marketLocation = marketLocation;
        this.marketPhoneNumber = marketPhoneNumber;
        this.marketImageUrl = marketImageUrl;
        this.marketDescription = marketDescription;
    }

    public long getMarketId() {
        return marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public String getMarketLocation() {
        return marketLocation;
    }

    public long getMarketPhoneNumber() {
        return marketPhoneNumber;
    }

    public String getMarketImageUrl() {
        return marketImageUrl;
    }

    public String getMarketDescription() {
        return marketDescription;
    }

    public double getMarketLatitude() {
        return marketLatitude;
    }

    public double getMarketLongitude() {
        return marketLongitude;
    }
}

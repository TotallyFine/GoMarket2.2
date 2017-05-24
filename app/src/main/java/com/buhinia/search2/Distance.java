package com.buhinia.search2;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * Created by sus on 2017/5/7.
 */

public class Distance {
    public static double getDistance(BDLocation location,double marketLatitude,double marketLongitude){
        LatLng userLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        LatLng marketLatLng=new LatLng(marketLatitude,marketLongitude);
        return DistanceUtil.getDistance(userLatLng,marketLatLng);
    }
}

package com.buhinia.search2;

import org.litepal.crud.DataSupport;

/**
 * Created by sus on 2017/5/1.
 */

public class BrowseHistory extends DataSupport {
    private long goodId;

    BrowseHistory(long goodId) {
        this.goodId = goodId;
    }

    long getGoodId() {

        return goodId;
    }
}

package com.example.dwl.menuapplication

import com.weimu.universalib.OriginAppData

/**
 * Author:你需要一台永动机
 * Date:2018/1/17 13:59
 * Description:
 */
class AppData : OriginAppData() {
    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
    }


}

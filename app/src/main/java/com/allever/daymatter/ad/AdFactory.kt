package com.allever.daymatter.ad

import com.allever.lib.ad.AdBusiness
import com.allever.lib.ad.admob.AdMobBusiness
import com.allever.lib.ad.chain.IAdBusiness
import com.allever.lib.ad.chain.IAdBusinessFactory

class AdFactory: IAdBusinessFactory {
    override fun getAdBusiness(businessName: String): IAdBusiness? {
        return when(businessName) {
            AdBusiness.A -> {
                AdMobBusiness
            }
            else -> {
                null
            }
        }
    }

}
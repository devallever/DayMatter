package com.allever.daymatter.ad

import com.allever.lib.ad.ADType
import com.allever.lib.ad.AdBusiness

object AdConstants {

    val AD_NAME_INSERT = "AD_NAME_INSERT"


    val AD_NAME_BANNER = "AD_NAME_BANNER"


    val AD_NAME_VIDEO = "AD_NAME_VIDEO"

    val adData = "{\n" +
            "  \"business\": [\n" +
            "    {\n" +
            "      \"name\": \"${AdBusiness.A}\",\n" +
            "      \"appId\": \"\",\n" +
            "      \"appKey\": \"\",\n" +
            "      \"token\": \"\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"adConfig\": [\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"ca-app-pub-8815582923430605/4136486096\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"ca-app-pub-8815582923430605/5449567767\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_VIDEO\",\n" +
            "      \"type\": \"${ADType.VIDEO}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"ca-app-pub-8815582923430605/1510322757\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"
}

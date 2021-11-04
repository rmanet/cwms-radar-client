/*
 * Copyright (c) 2021
 * United States Army Corps of Engineers - Hydrologic Engineering Center (USACE/HEC)
 * All Rights Reserved.  USACE PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval from HEC
 */

package mil.army.usace.hec.cwms.radar.client;

import java.io.IOException;
import java.time.Instant;
import mil.army.usace.hec.cwms.http.client.OkHttpUtil;
import mil.army.usace.hec.cwms.radar.client.model.TimeSeries;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class TimeSeriesController extends AbstractController {

    public TimeSeries retrieveTimeSeries(HttpUrlProvider radarUrlProvider, String timeSeriesId, String officeId, String units, String datum,
                                         Instant start, Instant end, String page) throws IOException {
        OkHttpClient client = OkHttpUtil.getClient();
        HttpUrl httpUrl = radarUrlProvider.buildHttpUrl("/timeseries")
            .newBuilder()
            .addQueryParameter("name", timeSeriesId)
            .addQueryParameter("office", officeId)
            .addQueryParameter("unit", units)
            .addQueryParameter("datum", datum)
            .addQueryParameter("begin", start.toString())
            .addQueryParameter("end", end.toString())
            .addQueryParameter("page", page)
            .addQueryParameter("timezone", "UTC")
            .build();
        Request build = new Request.Builder()
            .url(httpUrl)
            .addHeader("accept", "application/json;version=2")
            .build();
        return extractValueFromBody(timeSeriesId, client, build, TimeSeries.class);
    }
}

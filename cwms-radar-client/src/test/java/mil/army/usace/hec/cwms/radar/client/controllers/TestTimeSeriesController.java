/*
 * Copyright (c) 2021
 * United States Army Corps of Engineers - Hydrologic Engineering Center (USACE/HEC)
 * All Rights Reserved.  USACE PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval from HEC
 */

package mil.army.usace.hec.cwms.radar.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import mil.army.usace.hec.cwms.radar.client.ClientNotFoundException;
import mil.army.usace.hec.cwms.radar.client.NoDataFoundException;
import mil.army.usace.hec.cwms.radar.client.model.TimeSeries;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

class TestTimeSeriesController extends TestController {

    @Test
    void testRetrieveTimeSeries() throws IOException {
        MockWebServer server = new MockWebServer();
        String collect = readJsonFile(server, "radar/json/timeseries.json");
        server.enqueue(new MockResponse().setBody(collect));
        server.start();
        Instant start = ZonedDateTime.of(2018, 1, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        Instant end = ZonedDateTime.of(2018, 2, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        TimeSeries timeSeries = new TimeSeriesController().retrieveTimeSeries(server::url,
            "arbu.Elev.Inst.1Hour.0.Ccp-Rev", "SWT", "SI", "NAVD88", start, end, null);
        assertEquals(500, timeSeries.getValues().size());
        assertEquals(745, timeSeries.getTotal());
        assertEquals("SWT", timeSeries.getOfficeId());
        assertEquals("m", timeSeries.getUnits());
        assertEquals(Duration.ZERO, timeSeries.getInterval());
        assertEquals("ARBU.Elev.Inst.1Hour.0.Ccp-Rev", timeSeries.getName());
        assertEquals(start, timeSeries.getBegin().toInstant());
        Instant lastTime = Instant.ofEpochMilli(timeSeries.getValues().get(timeSeries.getValues().size() - 1).getDateTime());
        assertTrue(end.isAfter(lastTime));
    }

    @Test
    void testRetrieveTimeSeriesPagination() throws IOException {
        MockWebServer server = new MockWebServer();
        String page1Body = readJsonFile(server, "radar/json/timeseries_page1.json");
        String page2Body = readJsonFile(server, "radar/json/timeseries_page2.json");
        server.enqueue(new MockResponse().setBody(page1Body));
        server.enqueue(new MockResponse().setBody(page2Body));
        server.start();
        Instant start = ZonedDateTime.of(2018, 1, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        Instant end = ZonedDateTime.of(2018, 2, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        TimeSeries timeSeries = new TimeSeriesController().retrieveTimeSeries(server::url,
            "arbu.Elev.Inst.1Hour.0.Ccp-Rev", "SWT", "SI", "NAVD88", start, end, null);
        assertEquals(500, timeSeries.getValues().size());
        assertEquals(745, timeSeries.getTotal());
        assertEquals("SWT", timeSeries.getOfficeId());
        assertEquals("m", timeSeries.getUnits());
        assertEquals(Duration.ZERO, timeSeries.getInterval());
        assertEquals("ARBU.Elev.Inst.1Hour.0.Ccp-Rev", timeSeries.getName());
        assertEquals(start, timeSeries.getBegin().toInstant());
        Instant firstTime = Instant.ofEpochMilli(timeSeries.getValues().get(0).getDateTime());
        Instant lastTime = Instant.ofEpochMilli(timeSeries.getValues().get(timeSeries.getValues().size() - 1).getDateTime());
        assertTrue(end.isAfter(lastTime));
        assertEquals(start, firstTime);
        timeSeries = new TimeSeriesController().retrieveTimeSeries(server::url,
            "arbu.Elev.Inst.1Hour.0.Ccp-Rev", "SWT", "SI", "NAVD88",
            start, end, timeSeries.getNextPage());
        assertEquals(245, timeSeries.getValues().size());
        assertEquals(745, timeSeries.getTotal());
        assertEquals("SWT", timeSeries.getOfficeId());
        assertEquals("m", timeSeries.getUnits());
        assertEquals(Duration.ZERO, timeSeries.getInterval());
        assertEquals("ARBU.Elev.Inst.1Hour.0.Ccp-Rev", timeSeries.getName());
        assertEquals(start, timeSeries.getBegin().toInstant());
        Instant newFirstTime = Instant.ofEpochMilli(timeSeries.getValues().get(0).getDateTime());
        Instant newLastTime = Instant.ofEpochMilli(timeSeries.getValues().get(timeSeries.getValues().size() - 1).getDateTime());
        assertEquals(end, newLastTime);
        assertTrue(start.isBefore(newFirstTime));
    }

    @Test
    void testCwmsRadarDown() throws IOException {
        MockWebServer server = new MockWebServer();
        String collect = readJsonFile(server, "radar/json/timeseries.json");
        server.enqueue(new MockResponse().setBody(collect));
        server.start();
        Instant start = ZonedDateTime.of(2018, 1, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        Instant end = ZonedDateTime.of(2018, 2, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        TimeSeriesController timeSeriesController = new TimeSeriesController();
        assertThrows(ClientNotFoundException.class, () -> timeSeriesController.retrieveTimeSeries(s -> HttpUrl.parse("http://localhost:11999" + s),
            "arbu.Elev.Inst.1Hour.0.Bogus", "SWT", "SI", "NAVD88", start, end, null));
    }

    @Test
    void testTimeSeriesNotFound() throws IOException {
        MockWebServer server = new MockWebServer();
        String collect = readJsonFile(server, "radar/json/timeseries_notfound.json");
        server.enqueue(new MockResponse().setResponseCode(404).setBody(collect));
        server.start();
        Instant start = ZonedDateTime.of(2018, 1, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        Instant end = ZonedDateTime.of(2018, 2, 5, 0, 0, 0, 0, ZoneId.of("UTC")).toInstant();
        TimeSeriesController timeSeriesController = new TimeSeriesController();
        assertThrows(NoDataFoundException.class, () -> timeSeriesController.retrieveTimeSeries(server::url,
            "arbu.Elev.Inst.1Hour.0.Bogus", "SWT", "SI", "NAVD88", start, end, null));
    }
}

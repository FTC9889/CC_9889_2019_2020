package com.team9889.lib.network;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by joshua9889 on 6/16/2020.
 */
public class ProvideHTML extends NanoHTTPD {
    public ProvideHTML() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map header = session.getHeaders();
        String fetch_mode = (String) header.get("sec-fetch-mode");
        String referer = (String) header.get("referer");

        if (fetch_mode != null && fetch_mode.equals("no-cors")) {
            String data = referer.split("8080/")[1];
            System.out.println(Arrays.toString(data.split("/")));
        }

        return newFixedLengthResponse("Hello world");
    }


    public static void main(String... args) throws IOException {
        new ProvideHTML();
    }
}

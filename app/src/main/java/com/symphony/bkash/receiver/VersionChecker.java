package com.symphony.bkash.receiver;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by monir.sobuj on 12/23/2018.
 */

public class VersionChecker extends AsyncTask<String, String, String> {
    private String newVersion;

    @Override
    protected String doInBackground(String... params) {

        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=com.bKash.customerapp")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                            Log.e("Version Checker", "doInBackground: version"+newVersion);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }
}
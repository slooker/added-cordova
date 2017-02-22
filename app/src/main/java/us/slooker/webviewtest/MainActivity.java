package us.slooker.webviewtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    // This gets the data back from the webview
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void sendData(String data) {
        //Get the string value to process
        System.out.println("Got data from webview: "+data);
    }

    @JavascriptInterface
    public void logSomething() {
        System.out.println("Logged something");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup webview
        final WebView myWebView = (WebView) findViewById(R.id.myWebView);
        WebSettings webSettings = myWebView.getSettings();
        // This makes an "Android" object in our webview that we can use to pass data back.
        myWebView.addJavascriptInterface(this, "Native");


        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                System.out.println("Overriding webview: "+url);
                view.loadUrl(url);
                return true;
            }
        });

        // This enables javascript, which webviews have disabled by default
        webSettings.setJavaScriptEnabled(true);
        myWebView.setBackgroundColor(Color.BLUE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This calls the javascript function in our webview when we click our button
                String jsString = "javascript:updateLocation('this is a new location')";
                myWebView.loadUrl(jsString);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                gotoCordova();

            }
        });


        // This is obviously our ugly html string
        String html = "<html><head><title>Test Title</title>" +
                "</head><body>this is a test<br /><div id='location'>No location</div>" +
                " <a href=\"http://test.slooker.us/\">Link to external site</a>" +
                "<script>" +
                "document.addEventListener(\"deviceready\", onDeviceReady, false);\n" +
                "function onDeviceReady() {\n" +
                "    window.open = cordova.InAppBrowser.open;\n" +
                "}" +
                "function updateLocation(location) {" +
                "  document.getElementById('location').innerHTML = location;" +
                "  alert('new location');" +
                "  console.log('new location is here!');" +
                "  console.log(location);" +
                "  Native.sendData(location)" +
                "}" +
                "</script>" +
                "</body></html>";
        // This loads the data locally.
        myWebView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    }

    public void gotoCordova() {
        System.out.println("going to cordova?");
        Intent intent = new Intent(this, LaunchCordova.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

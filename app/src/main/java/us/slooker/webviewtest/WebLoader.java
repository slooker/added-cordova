package us.slooker.webviewtest;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebLoader {
    WebView view;

    @JavascriptInterface
    public void javaLog(String logData) {
        System.out.println("Java logger: " + logData);
    }

    @JavascriptInterface
    public void updateLocation(String location) {
        String jsString = "javascript:updateLocation('" + location + "')";
        this.callJavascript(jsString);
    }

    /**
     * This creates the webview given the id of a resource (R.id.webView) and the activity
     * @param id - Id of the webview to create this in
     * @param activity - Activity of the caller
     */
    public void createWebView(int id, Activity activity) {
        view = (WebView) activity.findViewById(id);
        WebSettings webSettings = view.getSettings();
        // This makes a "Native" object in our webview that we can use to pass data back.
        view.addJavascriptInterface(this, "Native");

        // This keeps it from opening in its own browser
        // TODO: Figure out the non-deprecated way to do this.
        view.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                System.out.println("Overriding webview: "+url);
                view.loadUrl(url);
                return true;
            }
        });

        // This enables javascript, which webviews have disabled by default
        webSettings.setJavaScriptEnabled(true);
    }


    /**
     * Returns the created webview.  Usually shouldn't be used, since you can do most everything
     * inside of this class itself.
     *
     * @return Webview view
     */
    public WebView getWebView() {
        return view;
    }

    /**
     * Calls a javascript string in the currently loaded page
     * @param jString - Javascript string
     */
    public void callJavascript(String jString) {
        view.loadUrl(jString);
    }

    /**
     * Loads a url in the current webview
     * @param urlString - String of URL
     */
    public void loadUrl(String urlString) {
        view.loadUrl(urlString);
    }

    /**
     * Loads local html data
     * @param localHtml - local html string
     */
    public void loadLocal(String localHtml) {
        view.loadData(localHtml, "text/html", "UTF-8");  // string, mimetype, encoding
    }
}

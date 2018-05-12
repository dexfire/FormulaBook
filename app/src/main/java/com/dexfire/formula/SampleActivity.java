package com.dexfire.formula;

import android.app.*;
import android.content.ClipboardManager;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.webkit.*;
import android.text.method.*;
import android.text.*;
import android.content.*;

public class SampleActivity extends Activity
        implements View.OnClickListener {

    private String doubleEscapeTeX(String s) {
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') t += '\\';
            if (s.charAt(i) != '\n') t += s.charAt(i);
            if (s.charAt(i) == '\\') t += "\\";
        }
        return t;
    }

    private int exampleIndex = 0;
    private boolean mmltoggle = false;

    private String getExample(int index) {
        return getResources().getStringArray(R.array.tex_examples)[index];
    }

    public void onClick(View v) {
        if (v == findViewById(R.id.button_show)) {
            WebView w = findViewById(R.id.webview);
            EditText e = findViewById(R.id.edit);
            mmltoggle = false;
            w.evaluateJavascript("javascript:document.getElementById('mmlout').innerHTML='';",null);
            w.evaluateJavascript("javascript:document.getElementById('math').innerHTML='\\\\["
                    + doubleEscapeTeX(e.getText().toString()) + "\\\\]';",null);
            w.evaluateJavascript("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);",null);
        } else if (v == findViewById(R.id.button_clear)) {
            WebView w = findViewById(R.id.webview);
            EditText e = findViewById(R.id.edit);
            mmltoggle = false;
            e.setText("");
            w.evaluateJavascript("javascript:document.getElementById('mmlout').innerHTML='';",null);
            w.evaluateJavascript("javascript:document.getElementById('math').innerHTML='';",null);
        } else if (v == findViewById(R.id.button_example)) {
            WebView w = findViewById(R.id.webview);
            EditText e = findViewById(R.id.edit);
            mmltoggle = false;
            e.setText(getExample(exampleIndex++));
            if (exampleIndex > getResources().getStringArray(R.array.tex_examples).length - 1)
                exampleIndex = 0;
            w.evaluateJavascript("javascript:document.getElementById('mmlout').innerHTML='';",null);
            w.evaluateJavascript("javascript:document.getElementById('math').innerHTML='\\\\["
                    + doubleEscapeTeX(e.getText().toString())
                    + "\\\\]';",null);
            w.evaluateJavascript("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);",null);
        } else if (v == findViewById(R.id.button_cpmml)) {
            WebView w = findViewById(R.id.webview);
            EditText e = findViewById(R.id.edit);
            mmltoggle = !mmltoggle;
            w.evaluateJavascript("javascript:document.getElementById('mmlout').innerHTML='';",null);
            // need 2 versions of the MathML
            // showMML() returns literal MathML, getMML() returns &-escaped for HTML display
            // put getMML() into innerHTML of mmlout span
            // use JS call to clipMML() method in injected Java object
            // to put showMML() into system clipboard
            if (mmltoggle) {
                // &-escaped MathML enclosed in <pre> tags in "mmlout" span for HTML display
                w.evaluateJavascript("javascript:document.getElementById('mmlout').innerHTML = window.getEscapedMML();",null);
                w.evaluateJavascript("javascript:injectedObject.clipMML(window.getLiteralMML());",null);
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        WebView w = findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
        w.getSettings().setBuiltInZoomControls(false);
        w.loadDataWithBaseURL("http://bar/", "<script type='text/x-mathjax-config'>"
                + "MathJax.Hub.Config({ "
                + "showMathMenu: false, "
                + "jax: ['input/TeX','output/HTML-CSS'], " // output/SVG
                + "showProcessingMessages: false,"
                + "tex2jax: {"
                    + "preview: [''],"
                + "},"
                + "'HTML-CSS': {"
                    + "fonts: ['TeX'],"
                    + "EqnChunk: 1,"
                    + "EqnChunkFactor: 1,"
                    + "EqnChunkDelay: 10,"
                    + "showMathMenu: false,"
                + "},"

                + "extensions: ['tex2jax.js','toMathML.js'], "
                + "TeX: { extensions: ['AMSmath.js','AMSsymbols.js',"
                + "'noErrors.js','noUndefined.js','color.js','autobold.js','bbox.js','fast-preview.js'] }, "
                //+"'SVG' : { blacker: 30, "
                // +"styles: { path: { 'shape-rendering': 'crispEdges' } } } "
                + "});</script>"
                + "<script type='text/javascript' "
                + "src='file:///android_asset/MathJax/MathJax.js'"
                + "></script>"
                + "<script type='text/javascript'>getLiteralMML = function() {"
                + "math=MathJax.Hub.getAllJax('math')[0];"
                // below, toMathML() rerurns literal MathML string
                + "mml=math.root.toMathML(''); return mml;"
                + "}; getEscapedMML = function() {"
                + "math=MathJax.Hub.getAllJax('math')[0];"
                // below, toMathMLquote() applies &-escaping to MathML string input
                + "mml=math.root.toMathMLquote(getLiteralMML()); return mml;}"
                + "</script>"
                + "<span id='math'></span><pre><span id='mmlout'></span></pre>", "text/html", "utf-8", "");
        w.addJavascriptInterface(
                new Object() {
                    @JavascriptInterface
                    public void clipMML(String s) {
                        WebView ww = findViewById(R.id.webview);
                        //uses android.text.ClipboardManager for compatibility with pre-Honeycomb
                        //for HC or later, use android.content.ClipboardManager
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        //next 2 comment lines have HC or later code, can also try newHtmlText()
                        //ClipData clip = ClipData.newPlainText("MJ MathML text",s);//,s);
                        //clipboard.setPrimaryClip(clip);
                        // literal MathML (in parameter s) placed on system clipboard
                        clipboard.setPrimaryClip(ClipData.newPlainText("copied MML code", s));
                        Toast.makeText(getApplicationContext(), "MathML copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                }, "injectedObject");
        EditText e = findViewById(R.id.edit);
        e.setBackgroundColor(Color.LTGRAY);
        e.setTextColor(Color.BLACK);
        e.setText("");
        Button b = findViewById(R.id.button_show);
        b.setOnClickListener(this);
        b = findViewById(R.id.button_clear);
        b.setOnClickListener(this);
        b = findViewById(R.id.button_example);
        b.setOnClickListener(this);
        b = findViewById(R.id.button_cpmml);
        b.setOnClickListener(this);
        TextView t = findViewById(R.id.textview3);
        t.setMovementMethod(LinkMovementMethod.getInstance());
        t.setText(Html.fromHtml(t.getText().toString()));

    }
}


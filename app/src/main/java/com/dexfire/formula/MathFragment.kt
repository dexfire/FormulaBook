package com.dexfire.formula

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper
import com.manuelpeinado.fadingactionbar.FadingActionBarHelperBase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.main.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MathFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MathFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MathFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mWebView: WebView? = null;
    private var mListener: OnFragmentInteractionListener? = null
    private var TAG:String = "MathFragment"
    private val DEBUG = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fab = FadingActionBarHelper();
        fab.actionBarBackground<FadingActionBarHelper>(R.drawable.design_snackbar_background);
        return inflater!!.inflate(R.layout.fragment_formula_editor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        verifyPermissions()
        mWebView = view!!.findViewById(R.id.fragment_formula_editor_previewer)
        var mWebSettings = mWebView!!.settings;
        mWebSettings.javaScriptEnabled = true
        mWebSettings.allowFileAccess = true
        mWebSettings.builtInZoomControls = false
        mWebSettings.allowContentAccess = true
        mWebSettings.allowFileAccessFromFileURLs = true
        mWebSettings.allowUniversalAccessFromFileURLs = true
        if(DEBUG) Log.i(TAG,"onActivityCreated() - ExternalStorageDirectory = "+Environment.getExternalStorageDirectory().absolutePath)
        mWebView!!.loadUrl("file://"+Environment.getExternalStorageDirectory().absolutePath+"/Formula/index.html")
        var fab = activity.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            if(mWebView==null) Log.e("Formula - dexfire","webview is null!")
            if(DEBUG)Log.w("Formula - dexfire","loading webpage...")
            mWebView!!.loadUrl("content://com.dexfire.formula.localhtmlprovider/sdcard/Formula/index.html")
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    fun verifyPermissions() {
        try{
            var permit:Int = ActivityCompat.checkSelfPermission(context,"android.permission.WRITE_EXTERNAL_STORAGE")
            var result = 0
            if(permit != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity,
                        Array<String>(2){"android.permission.WRITE_EXTERNAL_STORAGE";
                            "android.permission.READ_EXTERNAL_STORAGE"},result)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MathFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): MathFragment {
            val fragment = MathFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

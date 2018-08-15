package com.dexfire.formula

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.daquexian.flexiblerichtextview.FlexibleRichTextView
import org.scilab.forge.jlatexmath.core.AjLatexMath

class FlexibleRichTextFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var mTestRichText: String? = null
    private var mFlexibleRichTextView:FlexibleRichTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view:View = inflater!!.inflate(R.layout.rich_text_test,container,false)
        AjLatexMath.init(context)
        mFlexibleRichTextView = view.findViewById(R.id.test_text)
        mFlexibleRichTextView!!.setText("[h][center]hi![/center][/h]" +
                "[quote]This is quote[/quote]" +
                "[code]print(\"Hello FlexibleRichTextView!\")[/code]" +
                "Hello FlexibleRichTextView!\n" +
                "This is LaTeX:\n" +
                "\$e^{\\pi i} + 1 = 0\$")
        return mFlexibleRichTextView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
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

}// Required empty public constructor

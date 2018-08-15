package com.dexfire.formula

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.SlidingDrawer

/**
 * TODO: document your custom view class.
 */
@SuppressWarnings("Deprecated")
class FormulaInputKeyboard : SlidingDrawer {
    private var mExampleString: String? = null
    private var mExampleColor = Color.RED
    private var mExampleDimension = 0f
    private var mTextPaint: TextPaint? = null
    private var mTextWidth: Float = 0f
    private var mTextHeight: Float = 0f


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.FormulaInputKeyboard, defStyle, 0)

        mExampleString = a.getString(
                R.styleable.FormulaInputKeyboard_exampleString)
        mExampleColor = a.getColor(
                R.styleable.FormulaInputKeyboard_exampleColor,
                mExampleColor)
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.FormulaInputKeyboard_exampleDimension,
                mExampleDimension)

        a.recycle()

        // Set up a default TextPaint object
        mTextPaint = TextPaint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint!!.textAlign = Paint.Align.LEFT

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        mTextPaint!!.textSize = mExampleDimension
        mTextPaint!!.color = mExampleColor
        mTextWidth = mTextPaint!!.measureText(mExampleString)

        val fontMetrics = mTextPaint!!.fontMetrics
        mTextHeight = fontMetrics.bottom
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        // Draw the text.
        canvas.drawText(mExampleString!!,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint!!)

    }
}

package com.example.misohe.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.example.misohe.R
import java.lang.Exception

/** -------------------Created by Ankit----------------------**/

/** Custom Rule View for getting height*/

class RuleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var TOUCH_SLOP = 0
    private var MIN_FLING_VELOCITY = 0
    private var MAX_FLING_VELOCITY = 0
    private var bgColor = 0
    private var gradationColor = 0
    private var shortGradationColor = 0
    private var longGradationColor = 0
    private var shortLineWidth = 0f
    private var longLineWidth = 0f
    private var shortGradationLen = 0f
    private var longGradationLen = 0f
    private var textColor = 0
    private var textSize = 0f
    private var indicatorLineColor = 0
    private var indicatorLineWidth = 0f
    private var indicatorLineLen = 0f
    private var minValue = 0f
    private var maxValue = 0f
    private var currentValue = 0f
    private var gradationUnit = 0f
    private var numberPerCount = 0
    private var gradationGap = 0f
    private var gradationNumberGap = 0f
    private var mMinNumber = 0
    private var mMaxNumber = 0
    private var mCurrentNumber = 0
    private var mNumberRangeDistance = 0f
    private var mNumberUnit = 0
    private var mCurrentDistance = 0f
    private var mWidthRangeNumber = 0
    private var mPaint: Paint? = null
    private var mTextPaint: TextPaint? = null
    private var mScroller: Scroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mWidth = 0
    private var mHalfWidth: Int = 0
    private var mHeight: Int = 0

    private var mDownX = 0
    private var mLastX = 0
    private var mLastY: Int = 0

    private var isMoved = false

    private var mValueChangedListener: OnValueChangedListener? = null

    interface OnValueChangedListener {
        fun onValueChanged(value: Float)
    }

    init {
        initAttrs(context, attrs)
        val viewConfiguration = ViewConfiguration.get(context)
        TOUCH_SLOP = viewConfiguration.scaledTouchSlop
        MIN_FLING_VELOCITY = viewConfiguration.scaledMinimumFlingVelocity
        MAX_FLING_VELOCITY = viewConfiguration.scaledMaximumFlingVelocity

        convertValue2Number()
        initPaint(context)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RuleView)
        bgColor = ta.getColor(R.styleable.RuleView_bgColor, Color.parseColor("#f5f8f5"))
        gradationColor =
            ta.getColor(R.styleable.RuleView_shortGradationColor, Color.LTGRAY)
        shortGradationColor =
            ta.getColor(R.styleable.RuleView_shortGradationColor, Color.LTGRAY)
        longGradationColor =
            ta.getColor(R.styleable.RuleView_longGradationColor, Color.LTGRAY)
        shortLineWidth =
            ta.getDimension(R.styleable.RuleView_shortLineWidth, dp2px(1f).toFloat())
        shortGradationLen =
            ta.getDimension(R.styleable.RuleView_shortGradationLen, dp2px(16f).toFloat())
        longGradationLen =
            ta.getDimension(R.styleable.RuleView_longGradationLen, shortGradationLen * 2)
        longLineWidth = ta.getDimension(R.styleable.RuleView_longLineWidth, shortLineWidth * 2)
        textColor = ta.getColor(R.styleable.RuleView_textColor, Color.BLACK)
        textSize = ta.getDimension(R.styleable.RuleView_textSize, sp2px(14f).toFloat())
        indicatorLineColor = ta.getColor(
            R.styleable.RuleView_indicatorLineColor,
            Color.parseColor("#48b975")
        )
        indicatorLineWidth =
            ta.getDimension(R.styleable.RuleView_indicatorLineWidth, dp2px(3f).toFloat())
        indicatorLineLen =
            ta.getDimension(R.styleable.RuleView_indicatorLineLen, dp2px(35f).toFloat())
        minValue = ta.getFloat(R.styleable.RuleView_minValue, 0f)
        maxValue = ta.getFloat(R.styleable.RuleView_maxValue, 100f)
        currentValue = ta.getFloat(R.styleable.RuleView_currentValue, 50f)
        gradationUnit = ta.getFloat(R.styleable.RuleView_gradationUnit, .1f)
        numberPerCount = ta.getInt(R.styleable.RuleView_numberPerCount, 10)
        gradationGap = ta.getDimension(R.styleable.RuleView_gradationGap, dp2px(10f).toFloat())
        gradationNumberGap =
            ta.getDimension(R.styleable.RuleView_gradationNumberGap, dp2px(8f).toFloat())
        ta.recycle()
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    private fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        ).toInt()
    }

    private fun convertValue2Number() {
        mMinNumber = minValue.toInt() * 10
        mMaxNumber = maxValue.toInt() * 10
        mCurrentNumber = currentValue.toInt() * 10
        mNumberUnit = gradationUnit.toInt() * 10
        if (mNumberUnit != 0) {
            mCurrentDistance = (mCurrentNumber - mMinNumber) / mNumberUnit * gradationGap
            mNumberRangeDistance = (mMaxNumber - mMinNumber) / mNumberUnit * gradationGap
        }
        if (mWidth != 0) {
            mWidthRangeNumber = (mWidth / gradationGap * mNumberUnit).toInt()
        }
    }

    private fun initPaint(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.strokeWidth = shortLineWidth
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint?.textSize = (textSize)
        mTextPaint?.color = (textColor)

        mScroller = Scroller(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = calculateSize(true, widthMeasureSpec)
        mHeight = calculateSize(false, heightMeasureSpec)
        mHalfWidth = mWidth shr 1
        if (mWidthRangeNumber == 0) {
            mWidthRangeNumber = (mWidth / gradationGap * mNumberUnit).toInt()
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun calculateSize(isWidth: Boolean, spec: Int): Int {
        val mode = MeasureSpec.getMode(spec)
        val size = MeasureSpec.getSize(spec)
        var realSize = size
        when (mode) {
            MeasureSpec.EXACTLY -> {
            }
            MeasureSpec.AT_MOST -> if (!isWidth) {
                val defaultContentSize = dp2px(80f)
                realSize = Math.min(realSize, defaultContentSize)
            }
            MeasureSpec.UNSPECIFIED -> {
            }
            else -> {
            }
        }
        //logD("isWidth=%b, mode=%d, size=%d, realSize=%d", isWidth, mode, size, realSize)
        return realSize
    }

    /*private fun logD(format: String, vararg args: Any) {
        if (RuleView.LOG_ENABLE) {
            Log.d("GradationView", String.format("zjun@$format", *args))
        }
    }*/

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        //logD("onTouchEvent: action=%d", action)
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mScroller!!.forceFinished(true)
                mDownX = x
                isMoved = false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mLastX

                if (!isMoved) {
                    val dy = y - mLastY
                    if (Math.abs(dx) < Math.abs(dy) || Math.abs(x - mDownX) < TOUCH_SLOP) {
                        return true
                    }
                    isMoved = true
                }
                mCurrentDistance += -dx.toFloat()
                calculateValue()
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker!!.computeCurrentVelocity(1000, MAX_FLING_VELOCITY.toFloat())
                val xVelocity = mVelocityTracker!!.xVelocity.toInt()
                if (Math.abs(xVelocity) >= MIN_FLING_VELOCITY) {
                    mScroller!!.fling(
                        mCurrentDistance.toInt(), 0, -xVelocity, 0,
                        0, mNumberRangeDistance.toInt(), 0, 0
                    )
                    invalidate()
                } else {
                    scrollToGradation()
                }
            }
            else -> {
            }
        }
        mLastX = x
        mLastY = y
        return true
    }

    private fun calculateValue() {
        mCurrentDistance =
            Math.min(Math.max(mCurrentDistance, 0f), mNumberRangeDistance)
        mCurrentNumber = mMinNumber + (mCurrentDistance / gradationGap).toInt() * mNumberUnit
        currentValue = mCurrentNumber / 10f
        /*logD(
            "calculateValue: mCurrentDistance=%f, mCurrentNumber=%d, currentValue=%f",
            mCurrentDistance, mCurrentNumber, currentValue
        )*/
        mValueChangedListener?.onValueChanged(currentValue)
        invalidate()
    }

    private fun scrollToGradation() {
        mCurrentNumber =
            mMinNumber + Math.round(mCurrentDistance / gradationGap) * mNumberUnit
        mCurrentNumber =
            Math.min(Math.max(mCurrentNumber, mMinNumber), mMaxNumber)
        mCurrentDistance = (mCurrentNumber - mMinNumber) / mNumberUnit * gradationGap
        currentValue = mCurrentNumber / 10f
        /*logD(
            "scrollToGradation: mCurrentDistance=%f, mCurrentNumber=%d, currentValue=%f",
            mCurrentDistance, mCurrentNumber, currentValue
        )*/
        mValueChangedListener?.onValueChanged(currentValue)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            if (mScroller!!.currX != mScroller!!.finalX) {
                mCurrentDistance = mScroller!!.currX.toFloat()
                calculateValue()
            } else {
                scrollToGradation()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(bgColor)
        drawGradation(canvas)
        drawIndicator(canvas)
    }

    private fun drawGradation(canvas: Canvas) {
        val bottomPadding = 5F
        var startNum =
            (mCurrentDistance.toInt() - mHalfWidth) / gradationGap.toInt() * mNumberUnit + mMinNumber
        val expendUnit = mNumberUnit shl 1
        startNum -= expendUnit
        if (startNum < mMinNumber) {
            startNum = mMinNumber
        }
        var rightMaxNum = startNum + expendUnit + mWidthRangeNumber + expendUnit
        if (rightMaxNum > mMaxNumber) {
            rightMaxNum = mMaxNumber
        }
        var distance =
            mHalfWidth - (mCurrentDistance - (startNum - mMinNumber) / mNumberUnit.toFloat() * gradationGap)
        val perUnitCount = mNumberUnit * numberPerCount
        while (startNum <= rightMaxNum) {
            try {
                if (startNum % perUnitCount == 0) {
                    mPaint?.strokeWidth = longLineWidth
                    mPaint?.color = longGradationColor
                    mPaint?.strokeCap = Paint.Cap.ROUND
                    canvas.drawLine(distance, bottomPadding, distance, longGradationLen, mPaint!!)

                    val fNum = startNum / 10f
                    var text = java.lang.Float.toString(fNum)
                    if (text.endsWith(".0")) {
                        text = text.substring(0, text.length - 2)
                    }
                    val textWidth = mTextPaint!!.measureText(text)
                    canvas.drawText(
                        text,
                        distance - textWidth * .5f,
                        longGradationLen + gradationNumberGap + textSize,
                        mTextPaint!!
                    )
                } else {
                    val shortGradientBottomGap = (longGradationLen-shortGradationLen)
                    mPaint?.strokeWidth = shortLineWidth
                    mPaint?.color = shortGradationColor
                    mPaint?.strokeCap = Paint.Cap.ROUND
                    canvas.drawLine(distance, shortGradientBottomGap, distance, shortGradationLen, mPaint!!)
                }
                startNum += mNumberUnit
                distance += gradationGap
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        mPaint?.color = indicatorLineColor
        mPaint?.strokeWidth = indicatorLineWidth
        mPaint?.strokeCap = Paint.Cap.ROUND

        canvas.drawLine(
            mHalfWidth.toFloat(), 5F, mHalfWidth.toFloat(), indicatorLineLen,
            mPaint ?: Paint()
        )
        mPaint?.strokeCap = Paint.Cap.BUTT
    }

    fun setCurrentValue(currentValue: Float) {
        require(!(currentValue < minValue || currentValue > maxValue)) {
            String.format(
                "The currentValue of %f is out of range: [%f, %f]",
                currentValue, minValue, maxValue
            )
        }
        if (!mScroller!!.isFinished) {
            mScroller?.forceFinished(true)
        }
        this.currentValue = currentValue
        mCurrentNumber = (this.currentValue * 10).toInt()
        val newDistance = (mCurrentNumber - mMinNumber) / mNumberUnit * gradationGap
        val dx = (newDistance - mCurrentDistance).toInt()
        val duration = dx * 2000 / mNumberRangeDistance.toInt()
        mScroller?.startScroll(mCurrentDistance.toInt(), 0, dx, duration)
        postInvalidate()
    }

    fun getMinValue(): Float {
        return minValue
    }

    fun getMaxValue(): Float {
        return maxValue
    }

    fun getCurrentValue(): Float {
        return currentValue
    }

    fun setValue(
        minValue: Float,
        maxValue: Float,
        curValue: Float,
        unit: Float,
        perCount: Int
    ) {
        require(!(minValue > maxValue || curValue < minValue || curValue > maxValue)) {
            String.format(
                "The given values are invalid, check firstly: " +
                        "minValue=%f, maxValue=%f, curValue=%s", minValue, maxValue, curValue
            )
        }
        if (!mScroller!!.isFinished) {
            mScroller!!.forceFinished(true)
        }
        this.minValue = minValue
        this.maxValue = maxValue
        currentValue = curValue
        gradationUnit = unit
        numberPerCount = perCount
        convertValue2Number()
        mValueChangedListener?.onValueChanged(currentValue)
        postInvalidate()
    }

    fun setOnValueChangedListener(listener: OnValueChangedListener?) {
        mValueChangedListener = listener
    }
}
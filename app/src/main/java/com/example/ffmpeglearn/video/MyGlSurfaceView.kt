package com.example.ffmpeglearn.video

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/7/31 14:55
　　*/
class MyGlSurfaceView : GLSurfaceView, ScaleGestureDetector.OnScaleGestureListener {
    private val TAG = "MySurfaceView"
    private var mRatioWidth = 0
    private var mRatioHeight = 0
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    private var mPreviousY = 0f
    private var mPreviousX = 0f
    private var mXAngle = 0
    private var mYAngle = 0
    private var mPreScale = 1.0f
    private var mCurScale = 1.0f
    private var mLastMultiTouchTime: Long = 0
    private var mOnGestureCallback: OnGestureCallback? = null

    constructor(context: Context) : super(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mScaleGestureDetector = ScaleGestureDetector(context, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth)
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height)
            }
        }
    }


    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val preSpan: Float = detector.previousSpan
        val curSpan: Float = detector.currentSpan
        mCurScale = if (curSpan < preSpan) {
            mPreScale - (preSpan - curSpan) / 200
        } else {
            mPreScale + (curSpan - preSpan) / 200
        }
        mCurScale = 0.05f.coerceAtLeast(mCurScale.coerceAtMost(80.0f))
        mOnGestureCallback?.onGesture(mXAngle, mYAngle, mCurScale)
        return false
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {
        mPreScale = mCurScale
        mLastMultiTouchTime = System.currentTimeMillis()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.pointerCount == 1) {
            consumeClickEvent(e)
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - mLastMultiTouchTime > 200) {
                val y = e.y
                val x = e.x
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> {
                        val dy = y - mPreviousY
                        val dx = x - mPreviousX
                        mYAngle += (dx * TOUCH_SCALE_FACTOR).toInt()
                        mXAngle += (dy * TOUCH_SCALE_FACTOR).toInt()
                    }
                }
                mPreviousY = y
                mPreviousX = x
                mOnGestureCallback?.onGesture(
                    mXAngle,
                    mYAngle,
                    mCurScale
                )
            }
        } else {
            mScaleGestureDetector?.onTouchEvent(e)
        }
        return true
    }

    fun setAspectRatio(width: Int, height: Int) {
        Log.d(
            TAG,
            "setAspectRatio() called with: width = [$width], height = [$height]"
        )
        require(!(width < 0 || height < 0)) { "Size cannot be negative." }
        mRatioWidth = width
        mRatioHeight = height
        requestLayout()
    }

    fun addOnGestureCallback(callback: OnGestureCallback) {
        mOnGestureCallback = callback
    }

    private fun consumeClickEvent(event: MotionEvent) {
        var touchX = -1f
        var touchY = -1f
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                touchX = event.x
                touchY = event.y
                run {
                    //点击
                    mOnGestureCallback?.onTouchLoc(touchX, touchY)
                }
            }
            else -> {
            }
        }
    }


    interface OnGestureCallback {
        fun onGesture(xRotateAngle: Int, yRotateAngle: Int, scale: Float)
        fun onTouchLoc(touchX: Float, touchY: Float)
    }


}
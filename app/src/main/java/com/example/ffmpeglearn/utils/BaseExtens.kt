package com.example.ffmpeglearn.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*





/**
 * 页面描述：一些扩展
 *
 */
private var toast: Toast? = null

fun Context.getCompactColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    toast ?: let {
        toast = Toast.makeText(this.applicationContext, null, duration)
    }
    toast?.apply {
        setText(msg)
        show()
    }
}

@SuppressLint("ShowToast")
fun <T : Fragment> T.toast(text: CharSequence) {
    context?.toast(text)
}

/**
 * @param resId 字符串资源
 */
fun <T : Fragment> T.toast(@StringRes resId: Int) {
    toast(getString(resId))
}


/**
 * 比较内容是否一致
 */
infix fun Any.sameAs(other: Any) = this == other

/**
 * 随机数
 */
fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start


/**
 * 使用内联函数的泛型参数 reified 特性来实现
使用：Context.startActivity<MainActivity>()
 */
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}


/**
 * 屏幕宽度(px)
 */
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度(px)
 */
inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * 屏幕的密度
 */
inline val Context.density: Float
    get() = resources.displayMetrics.density

/**
 * dp 转为 px
 */
fun Context.dp2px(value: Int): Int = (density * value).toInt()

/**
 * dp 转为 px
 */
fun Context.dp2px(value: Float): Int = (density * value).toInt()

/**
 * px 转为 dp
 */
fun Context.px2dp(value: Int): Float = value.toFloat() / density

/**
 * 判断字符串是否为空
 */
fun CharSequence?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun CharSequence?.isNotNullNotEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}


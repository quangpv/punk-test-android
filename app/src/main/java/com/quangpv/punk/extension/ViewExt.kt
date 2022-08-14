package com.quangpv.punk.extension

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.quangpv.punk.R


val Context.activity: FragmentActivity
    get() {
        if (this is FragmentActivity) return this
        if (this is ContextWrapper) return this.baseContext.activity
        error("Not found activity for this context")
    }

val View.activity: FragmentActivity
    get() {
        return context.activity
    }


private fun getViewFragment(view: View): Fragment? {
    val tag = view.getTag(R.id.fragment_container_view_tag)
    return if (tag is Fragment) {
        tag
    } else null
}

fun View.findFragment(): Fragment? {
    var view: View? = this
    while (view != null) {
        val fragment = getViewFragment(view)
        if (fragment != null) {
            return fragment
        }
        val parent = view.parent
        view = if (parent is View) (parent as View) else null
    }
    return null
}

fun <T : View> T.show(isShow: Boolean, block: T.() -> Unit = {}) {
    this.visibility = if (isShow) {
        View.VISIBLE.also { block(this) }
    } else {
        View.GONE
    }
}

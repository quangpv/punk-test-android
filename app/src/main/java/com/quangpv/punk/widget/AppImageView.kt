package com.quangpv.punk.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.quangpv.punk.model.ui.IImage
import com.quangpv.punk.model.ui.ResImage
import com.quangpv.punk.model.ui.UrlImage

class AppImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatImageView(context, attrs) {
    fun setImage(image: IImage) {
        val glide = Glide.with(this)
        when (image) {
            is UrlImage -> glide.load(image.url).into(this)
            is ResImage -> glide.load(image.resId).into(this)
            else -> error("Not support $image yet")
        }
    }
}
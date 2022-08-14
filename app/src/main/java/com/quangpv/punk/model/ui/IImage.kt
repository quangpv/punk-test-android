package com.quangpv.punk.model.ui

import androidx.annotation.DrawableRes

interface IImage

class UrlImage(val url: String) : IImage
class ResImage(@DrawableRes val resId: Int) : IImage
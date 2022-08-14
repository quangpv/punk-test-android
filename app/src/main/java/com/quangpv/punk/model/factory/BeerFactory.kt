package com.quangpv.punk.model.factory

import android.support.di.InjectBy
import android.support.di.Injectable
import android.support.di.ShareScope
import com.quangpv.punk.R
import com.quangpv.punk.model.dto.BeerDTO
import com.quangpv.punk.model.ui.IBeer
import com.quangpv.punk.model.ui.IImage
import com.quangpv.punk.model.ui.ResImage
import com.quangpv.punk.model.ui.UrlImage

@InjectBy(BeerFactoryImpl::class, ShareScope.Activity)
interface BeerFactory : Injectable {
    fun create(beer: BeerDTO): IBeer
}

class BeerFactoryImpl : BeerFactory {
    override fun create(beer: BeerDTO): IBeer {
        return object : IBeer {
            override val image: IImage = if (beer.imageUrl.isNullOrBlank())
                ResImage(R.drawable.ic_baseline_image_24)
            else UrlImage(beer.imageUrl)
            override val tagLine: String get() = beer.tagLine.orEmpty()
            override val name: String get() = beer.name.orEmpty()
            override val description: String get() = beer.description.orEmpty()
        }
    }
}
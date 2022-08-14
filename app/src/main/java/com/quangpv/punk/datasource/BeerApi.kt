package com.quangpv.punk.datasource

import android.support.di.InjectBy
import android.support.di.Injectable
import android.support.di.ShareScope
import com.quangpv.punk.helper.network.Async
import com.quangpv.punk.helper.network.MockAsync
import com.quangpv.punk.model.dto.BeerDTO
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.QueryMap

@InjectBy(BeerApiImpl::class, ShareScope.Singleton)
interface BeerApi : Injectable {
    @GET("beers")
    fun getList(
        @QueryMap queries: Map<String, @JvmSuppressWildcards Any>,
    ): Async<List<BeerDTO>>
}

class MockBeerApiImpl : BeerApi {
    override fun getList(queries: Map<String, Any>): Async<List<BeerDTO>> {
        val page = queries["page"] as? Int ?: 1
        val pageSize = queries["per_page"] as? Int ?: 10

        if (page == 4) return MockAsync(emptyList())
        return MockAsync(
            (page * 100 until page * 100 + pageSize).map {
                BeerDTO(it.toLong(),
                    "Beer $it",
                    "Tag $it",
                    "")
            }
        )
    }
}

class BeerApiImpl(private val retrofit: Retrofit) : BeerApi by retrofit.create()
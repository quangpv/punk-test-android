package com.quangpv.punk.feature

import android.support.core.view.RecyclerHolder
import android.support.core.view.bindingOf
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quangpv.punk.R
import com.quangpv.punk.databinding.ItemBeerBinding
import com.quangpv.punk.extension.show
import com.quangpv.punk.model.ui.IBeer
import com.quangpv.punk.widget.pagination.PaginationAdapter

class BeerAdapter : PaginationAdapter<IBeer>() {
    override fun onCreateItemHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = parent.bindingOf(ItemBeerBinding::inflate)

        return object : RecyclerHolder<IBeer>(binding) {
            override fun bind(item: IBeer) {
                super.bind(item)
                binding.txtName.text = item.name
                binding.txtTagLine.text = item.tagLine
                binding.imgBeer.setImage(item.image)
                binding.txtDescription.show(item.description.isNotBlank()) {
                    text = item.description
                }
            }
        }
    }

    override fun onCreateLoadingHolder(parent: ViewGroup): LoadingHolder {
        return LoadingHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading, parent, false))
    }
}
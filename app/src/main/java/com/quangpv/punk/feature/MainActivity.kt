package com.quangpv.punk.feature

import android.os.Bundle
import android.support.core.view.viewBinding
import android.support.viewmodel.viewModel
import com.quangpv.punk.R
import com.quangpv.punk.app.AppActivity
import com.quangpv.punk.databinding.ActivityMainBinding
import com.quangpv.punk.extension.show

class MainActivity : AppActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = BeerAdapter()

        binding.btnToDate.setOnClickListener(PickDateAction {
            viewModel.setToDate(it)
        })

        binding.btnFromDate.setOnClickListener(PickDateAction {
            viewModel.setFromDate(it)
        })

        binding.rvRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        adapter.onLoadMoreListener = {
            viewModel.fetchNext()
        }

        adapter.onEmptyChangedListener = {
            binding.txtEmpty.show(it)
        }

        viewModel.beers.bind {
            adapter.submit(it)
        }

        viewModel.dateRange.bind {
            binding.btnToDate.text = it.to
            binding.btnFromDate.text = it.from
        }

        viewModel.refreshLoading.bind {
            binding.rvRefresh.isRefreshing = it
        }

        viewModel.viewLoading.bind {
            binding.viewLoading.show(it)
        }

        binding.rvList.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        viewModel.tryRefresh()
    }
}

package com.pasukanlangit.id.melijo.presentation.main.promo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.PromoResultItem
import com.pasukanlangit.id.melijo.databinding.ActivityAllPromoBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPromoActivity : AppCompatActivity(R.layout.activity_all_promo) {

    private var promoMain: PromoResultItem?= null
    private val binding: ActivityAllPromoBinding by viewBinding()
    private val viewModel: AllPromoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window,this)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnResetPromo.setOnClickListener {
            viewModel.deletePromoSelected()
            finish()
        }

        setUpWithRv()
        observePromo()
        observePromoSelected()
    }

    private fun observePromoSelected() {
        viewModel.promoMain.observe(this){
            promoMain = it
        }
    }

    private fun observePromo() {
        viewModel.promo.observe(this){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    it.data?.result?.let { promos ->
                        if(promos.isNotEmpty()){
                            binding.rvPromo.adapter = AllPromoAdapter(it.data.result.map { promObj -> promObj.copy(isMain = promObj.id == promoMain?.id) }){ promo ->
                                viewModel.insertPromoSelected(promo)
                                this.finish()
                            }
                            return@observe
                        }
                    }
                    binding.tvEmpty.isVisible = true
                }
                is MyResponse.Error -> { Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show() }
                else -> {}
            }
        }
    }

    private fun setUpWithRv() {
        binding.rvPromo.layoutManager = LinearLayoutManager(this)
    }
}
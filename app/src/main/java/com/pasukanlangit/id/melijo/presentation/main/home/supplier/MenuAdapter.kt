package com.pasukanlangit.id.melijo.presentation.main.home.supplier

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pasukanlangit.id.melijo.databinding.ItemMenuSupplierBinding
import com.pasukanlangit.id.melijo.presentation.main.home.supplier.product.AllProductMenuActivity

class MenuAdapter(private val menus: List<DataMenuSupplier>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    class MenuViewHolder(val binding: ItemMenuSupplierBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = ItemMenuSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        with(holder.binding) {
            val currentMenu = menus[position]
            ivIcon.setImageResource(currentMenu.icon)
            tvTitle.text = currentMenu.title

            root.setOnClickListener {
                val context = root.context
                with(context){
                    startActivity(Intent(this, AllProductMenuActivity::class.java))
                }
            }
        }
    }

    override fun getItemCount(): Int = menus.size

}
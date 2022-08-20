package com.example.zeroerror.ui.CheckProduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zeroerror.R
import com.example.zeroerror.data.model.Order
import com.example.zeroerror.databinding.ItemProductBinding

class ProductListAdapter :
    ListAdapter<Order, ProductListAdapter.ProductViewHolder>(CurrentProductComparator) {

    private lateinit var productBinding: ItemProductBinding

    inner class ProductViewHolder(private val productBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(productBinding.root) {
        @SuppressLint("ResourceType", "SetTextI18n")
        fun bind(data: Order){
            if(data.isChecked)
                productBinding.layoutItem.setBackgroundResource(R.drawable.product_item_border_checked)
            else
                productBinding.layoutItem.setBackgroundResource(R.drawable.product_item_border_unchecked)
            productBinding.tvProductBarcodeId.text = "상품 바코드 ID : " + data.itemId
            productBinding.tvProductName.text = data.itemName
            productBinding.tvProductBrand.text = data.brandName
            productBinding.tvProductCount.text = data.checkCount.toString()+"/"+data.totalCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        productBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(productBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object CurrentProductComparator: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.itemId == newItem.itemId
        }
    }
}
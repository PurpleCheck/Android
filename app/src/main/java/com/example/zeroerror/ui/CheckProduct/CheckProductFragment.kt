package com.example.zeroerror.ui.CheckProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.zeroerror.databinding.FragmentCheckproductBinding
import com.google.android.material.tabs.TabLayoutMediator

class CheckProductFragment: Fragment() {
    private lateinit var binding: FragmentCheckproductBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 1. View Binding 설정
        binding = FragmentCheckproductBinding.inflate(inflater, container, false)

        // 2. View Pager의 FragmentStateAdapter 설정
        binding.vpCheckProduct.adapter = activity?.let { ViewPagerFragmentStateAdapter(it) }

        // 3. View Pager의 Orientation 설정
        binding.vpCheckProduct.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 4. TabLayout + ViewPager2 연동 (ViewPager2에 Adapter 연동 후에)
        TabLayoutMediator(binding.tabLayout, binding.vpCheckProduct){ tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        return binding.root
    }

    // TabLayout의 tab position에 따라 title 설정
    private fun getTabTitle(position:Int):String?{
        return when(position){
            0 -> "SCAN PRODUCT"
            1 -> "ORDER LIST"
            else -> null
        }
    }
}
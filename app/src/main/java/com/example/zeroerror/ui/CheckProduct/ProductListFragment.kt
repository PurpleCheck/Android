package com.example.zeroerror.ui.CheckProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zeroerror.databinding.FragmentProductlistBinding

class ProductListFragment: Fragment() {

    private lateinit var binding: FragmentProductlistBinding
    private lateinit var viewModel: CheckProductViewModel
    private lateinit var adapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 1. View Binding 설정
        binding = FragmentProductlistBinding.inflate(inflater, container, false)

        // 2. View Model 설정
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            CheckProductViewModel::class.java)

        // 3. Adapter 설정
        adapter = ProductListAdapter()
        adapter.setHasStableIds(true)
        adapter.submitList(viewModel.productList.value?.toList())
        binding.rvProductList.adapter = adapter
        binding.rvProductList.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    // 4. View Model Item Observe
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.productList.observe(viewLifecycleOwner, Observer{
            binding.rvProductList.post(Runnable { adapter.submitList(it.toList()) })
        })
    }
}
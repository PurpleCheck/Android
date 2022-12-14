package com.example.zeroerror.ui.CheckProduct

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.zeroerror.R
import com.example.zeroerror.databinding.ActivityCheckproductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckProductActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCheckproductBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // 1. View Binding 설정
        binding = ActivityCheckproductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Main Fragment 설정
        transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, CheckProductFragment())
        transaction.commit()

        // 3. ToolBar Setting & Change ToolBar title
        setSupportActionBar(binding.tbCheckProduct)
        supportActionBar?.apply {
            title = "Scan Product Id"
            setVisible(true)
        }
    }
}
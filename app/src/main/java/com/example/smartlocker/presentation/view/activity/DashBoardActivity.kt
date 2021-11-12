package com.example.smartlocker.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartlocker.databinding.ActivityDashboardBinding

class DashBoardActivity: AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cancleButton.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        val intent = Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }
}
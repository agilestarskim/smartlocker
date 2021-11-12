package com.example.smartlocker.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.smartlocker.data.state.AdminMode
import com.example.smartlocker.databinding.DialogAdminMenuBinding
import com.example.smartlocker.presentation.view.activity.DashBoardActivity
import com.example.smartlocker.presentation.view.activity.MainActivity

class AdminMenuDialog(context: Context):Dialog(context),View.OnClickListener {

    private val binding = DialogAdminMenuBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window!!.setLayout(800, 1000)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        initButtonListener()
    }
    private fun initButtonListener(){
        binding.openOneButton.setOnClickListener(this)
        binding.fixButton.setOnClickListener(this)
        binding.staticButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            (binding.openOneButton)->{
                AdminMode.state = true
                AdminMode.mode = 1
                dismiss()
            }

            (binding.fixButton)->{
                AdminMode.state =true
                AdminMode.mode = 2
                dismiss()
            }

            (binding.staticButton)->{
                dismiss()
                val intent = Intent(context, DashBoardActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
}
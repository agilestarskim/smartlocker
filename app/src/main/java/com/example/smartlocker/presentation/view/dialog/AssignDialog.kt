package com.example.smartlocker.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.smartlocker.databinding.DialogAssignBinding
import com.example.smartlocker.presentation.view.activity.AssignActivity


class AssignDialog(context: Context, val id:Int):Dialog(context){

    private val binding by lazy { DialogAssignBinding.inflate(layoutInflater) }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window!!.setLayout(1000,550)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        binding.textView.text = "${id}번 자리를\n 배정받으시겠습니까?"
        binding.okButton.setOnClickListener {
            val intent = Intent(context, AssignActivity::class.java)
            intent.putExtra("id",id)
            startActivity(context,intent,null)
        }
        binding.noButton.setOnClickListener {
            dismiss()
        }

    }



}
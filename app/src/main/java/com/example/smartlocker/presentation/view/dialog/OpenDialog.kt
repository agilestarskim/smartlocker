package com.example.smartlocker.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.smartlocker.databinding.DialogOpenBinding
import com.example.smartlocker.presentation.logic.Logic
import com.example.smartlocker.presentation.viewmodel.LiveNode

class OpenDialog(context: Context):Dialog(context), View.OnClickListener {
    private val binding by lazy { DialogOpenBinding.inflate(layoutInflater) }
    private val liveNode by lazy { LiveNode(context.applicationContext as Application)}

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window!!.setLayout(1000, 600)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        binding.textView3.text = "${Logic.getSelectedId()}번 문이 열렸습니다.\n 사용이 끝났으면 반납해 주세요."
        binding.freeButton.setOnClickListener(this)
        binding.keepUsingButton.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v){
            (binding.keepUsingButton)-> dismiss()
            (binding.freeButton)-> {
                liveNode.delete(Logic.getSelectedId())
                Toast.makeText(context,"${Logic.getSelectedId()}번이 반납되었습니다.",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}
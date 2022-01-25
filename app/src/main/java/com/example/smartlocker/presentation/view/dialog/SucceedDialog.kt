package com.example.smartlocker.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.smartlocker.databinding.DialogSucceedBinding
import com.example.smartlocker.presentation.view.activity.MainActivity

class SucceedDialog (context: Context, id:Int): Dialog(context) {
    private val id = id
    private val binding by lazy { DialogSucceedBinding.inflate(layoutInflater) }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window!!.setLayout(950,600)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        binding.textView.text = "${id}번 자리가 \n 배정되었습니다.\n 사용 후 꼭 반납해주세요"
        binding.okButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            ContextCompat.startActivity(context, intent, null)
            dismiss()
        }

    }
}
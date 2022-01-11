package com.example.smartlocker.presentation.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.smartlocker.databinding.DialogCheckBinding


class CheckAdminDialog(context: Context):Dialog(context), View.OnClickListener {

    private val binding by lazy { DialogCheckBinding.inflate(layoutInflater) }

    companion object{
        const val ADMIN_PASSWORD:String = "000000"
    }

    private val inputPassword = mutableListOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView2.text ="관리자가 맞으십니까?\n 관리자 비밀번호를 입력해 주세요"
        window!!.setLayout(800, 1300)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        initButtonListener()
    }

    private fun initButtonListener(){
        binding.button0.setOnClickListener(this)
        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
        binding.button5.setOnClickListener(this)
        binding.button6.setOnClickListener(this)
        binding.button7.setOnClickListener(this)
        binding.button8.setOnClickListener(this)
        binding.button9.setOnClickListener(this)
        binding.deleteAllButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.button0->add('0')
            binding.button1->add('1')
            binding.button2->add('2')
            binding.button3->add('3')
            binding.button4->add('4')
            binding.button5->add('5')
            binding.button6->add('7')
            binding.button7->add('8')
            binding.button8->add('9')
            binding.deleteAllButton->deleteAll()
            binding.cancelButton->dismiss()

        }

    }

    private fun add(num:Char){
       inputPassword.add(num)
       binding.editText.setText(inputPassword.joinToString(""))
       if(inputPassword.size == 6){
           check()
       }
    }

    private fun deleteAll(){
        inputPassword.clear()
        binding.editText.setText(inputPassword.joinToString(""))
    }

    private fun check(){
        if(inputPassword.joinToString("")== ADMIN_PASSWORD ) {
            Log.d("myTag","관리자 비밀번호가 일치합니다.")
            dismiss()
            val dialog = AdminMenuDialog(context)
            dialog.show()
        }else{
            Log.d("myTag", "관리자 비밀번호가 불일치 합니다.")
            deleteAll()
        }
    }



}
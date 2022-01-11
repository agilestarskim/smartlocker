package com.example.smartlocker.presentation.view.dialog


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.smartlocker.databinding.DialogCheckBinding
import com.example.smartlocker.presentation.logic.Logic
import com.example.smartlocker.presentation.viewmodel.LiveNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CheckDialog(context: Context, val id: Int) : Dialog(context), View.OnClickListener {
    private val binding by lazy { DialogCheckBinding.inflate(layoutInflater) }
    private val liveNode by lazy { ViewModelProvider(context as ViewModelStoreOwner)[LiveNode::class.java] }
    private val answerPassword by lazy {
        CoroutineScope(Dispatchers.IO).async{
        liveNode.get(Logic.getSelectedId())?.password
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textView2.text ="${id}번 사용자가 맞습니까?\n 비밀번호를 입력해 주세요"
        window!!.setLayout(800, 1300)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setCancelable(true)
        initButtonListener()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("myTag",answerPassword.await().toString())
        }

    }



    private fun initButtonListener() {
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



    override fun onClick(view: View?) {
        when (view) {
            (binding.button0) -> add(0)
            (binding.button1) -> add(1)
            (binding.button2) -> add(2)
            (binding.button3) -> add(3)
            (binding.button4) -> add(4)
            (binding.button5) -> add(5)
            (binding.button6) -> add(6)
            (binding.button7) ->add(7)
            (binding.button8) -> add(8)
            (binding.button9) -> add(9)
            (binding.cancelButton) -> { dismiss() }
            (binding.deleteAllButton) -> { deleteAll()}
        }
    }

    private val inputPassword = mutableListOf<Int>()

    private fun add(num:Int){
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
        CoroutineScope(Dispatchers.Main).launch {
            if(inputPassword.joinToString("") == answerPassword.await() ){
                dismiss()
                val openDialog = OpenDialog(context)
                openDialog.show()
            }
            else{
                deleteAll()
                binding.editText.setText(inputPassword.joinToString(""))
                Toast.makeText(context,"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }


}
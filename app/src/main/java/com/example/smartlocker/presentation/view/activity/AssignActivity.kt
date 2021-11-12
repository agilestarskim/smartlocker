package com.example.smartlocker.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smartlocker.R
import com.example.smartlocker.databinding.ActivityAssignBinding
import com.example.smartlocker.presentation.view.dialog.SucceedDialog
import com.example.smartlocker.presentation.viewmodel.Password


class AssignActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityAssignBinding.inflate(layoutInflater) }
    private lateinit var password: Password
    private val assignDialogIntent by lazy { intent }
    val id by lazy{ assignDialogIntent.getIntExtra("id", 0)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        password = ViewModelProvider(this)[Password::class.java]
        observeFirstPassword()
        observeSecondPassword()
        observeEditTextFocusChange()
        observeNextViewState()
        initButtonListener()

        //홈으로
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(this, intent, null)
        }
    }

    //firstPassword UI 갱신
    private fun observeFirstPassword() {
        password.firstPassword.observe(this, {
            binding.firstEditText.setText(it.joinToString(""))

        })
    }

    //secondPassword UI 갱신
    private fun observeSecondPassword() {
        password.secondPassword.observe(this, {
            binding.secondEditText.setText(it.joinToString(""))

        })

    }
    //상태에 따른 editText border 색 변경
    private fun observeEditTextFocusChange(){
        password.state.observe(this, {
            if(it){
                binding.firstEditText.setBackgroundResource(R.drawable.bg_white_radius20)
                binding.secondEditText.setBackgroundResource(R.drawable.bg_white_radius20_border_purple)
            }else{
                binding.firstEditText.setBackgroundResource(R.drawable.bg_white_radius20_border_purple)
                binding.secondEditText.setBackgroundResource(R.drawable.bg_white_radius20)
            }
        })
    }
    //결과에 따른 토스트 메시지 또는 다이얼로그 보여주기
    private fun observeNextViewState(){
        password.nextView.observe(this,{
            when(it){
                (1)->{
                    val dialog = SucceedDialog(this, id = id)
                    dialog.show()
                }
                (2)->{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다. 다시 입력해 주세요.",Toast.LENGTH_SHORT).show()
                }
                (3)->{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다. 처음부터 다시 설정해 주세요", Toast.LENGTH_SHORT).show()
                }
            }

        })
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
        binding.deleteButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.button0 -> password.add('0')
            binding.button1 -> password.add('1')
            binding.button2 -> password.add('2')
            binding.button3 -> password.add('3')
            binding.button4 -> password.add('4')
            binding.button5 -> password.add('5')
            binding.button6 -> password.add('6')
            binding.button7 -> password.add('7')
            binding.button8 -> password.add('8')
            binding.button9 -> password.add('9')
            binding.deleteAllButton -> password.deleteAll()
            binding.deleteButton -> password.delete()
        }
    }


}
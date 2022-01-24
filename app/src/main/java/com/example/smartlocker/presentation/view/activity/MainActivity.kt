package com.example.smartlocker.presentation.view.activity


import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.smartlocker.R
import com.example.smartlocker.data.state.AdminMode
import com.example.smartlocker.databinding.ActivityMainBinding
import com.example.smartlocker.presentation.logic.Logic
import com.example.smartlocker.presentation.view.dialog.CheckAdminDialog
import com.example.smartlocker.presentation.viewmodel.LiveNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var liveNode: LiveNode
    private val logic by lazy { Logic(application) }
    private val nodeViewList by lazy {
        arrayOf(
            null,
            binding.node1,
            binding.node2,
            binding.node3,
            binding.node4,
            binding.node5
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        liveNode = ViewModelProvider(this)[LiveNode::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            liveNode.fetch()
        }
        initClickListener()
        observeNodeList()
        setAdminTextVisible()
    }


    private fun observeNodeList() {
        liveNode.liveNodeList.observe(
            this, { liveNodeList->
                val usingList = mutableListOf<Int>()
                val fixingList = mutableListOf<Int>()

                liveNodeList.forEach { nodeModel ->
                    if(nodeModel.enabled) usingList.add(nodeModel.id)
                    else fixingList.add(nodeModel.id)
                }
                nodeViewList.forEachIndexed { index, _ ->
                    when (index) {
                        0 -> return@forEachIndexed
                        in usingList -> nodeViewList[index]?.setBackgroundResource(R.drawable.node_using)
                        in fixingList -> nodeViewList[index]?.setBackgroundResource(R.drawable.node_fixing)
                        else -> nodeViewList[index]?.setBackgroundResource(R.drawable.node_available)
                    }
                }
            },
        )
    }

    private fun setAdminTextVisible(){
        AdminMode.liveState.observe(this,{
            if(it){
                binding.adminModeText.visibility = View.VISIBLE
            }else{
                binding.adminModeText.visibility = View.INVISIBLE
            }
        })
    }

    private fun initClickListener() {
        binding.node1.setOnClickListener(this)
        binding.node2.setOnClickListener(this)
        binding.node3.setOnClickListener(this)
        binding.node4.setOnClickListener(this)
        binding.node5.setOnClickListener(this)
        binding.adminButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.node1 -> logic.onClick(this, 1)
            binding.node2 -> logic.onClick(this, 2)
            binding.node3 -> logic.onClick(this, 3)
            binding.node4 -> logic.onClick(this, 4)
            binding.node5 -> logic.onClick(this, 5)
            binding.adminButton -> {
                val dialog = CheckAdminDialog(this)
                dialog.show()
            }
        }
    }
}
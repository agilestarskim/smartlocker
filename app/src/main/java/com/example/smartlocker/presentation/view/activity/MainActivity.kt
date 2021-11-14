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
            binding.node5,
            binding.node6,
            binding.node7,
            binding.node8,
            binding.node9,
            binding.node10,
            binding.node11,
            binding.node12,
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
                nodeViewList.filterNotNull().forEachIndexed { index, _ ->
                    if(index in usingList) nodeViewList[index]?.setBackgroundResource(R.drawable.node_using)
                    else if (index in fixingList) nodeViewList[index]?.setBackgroundResource(R.drawable.node_fixing)
                    else nodeViewList[index]?.setBackgroundResource(R.drawable.node_available)
                }
            },
        )
    }

    fun setAdminTextVisible(){
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
        binding.node6.setOnClickListener(this)
        binding.node7.setOnClickListener(this)
        binding.node8.setOnClickListener(this)
        binding.node9.setOnClickListener(this)
        binding.node10.setOnClickListener(this)
        binding.node11.setOnClickListener(this)
        binding.node12.setOnClickListener(this)
        binding.adminButton.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {

            binding.node1 -> logic.onClick(this, 1)
            binding.node2 -> logic.onClick(this, 2)
            binding.node3 -> logic.onClick(this, 3)
            binding.node4 -> logic.onClick(this, 4)
            binding.node5 -> logic.onClick(this, 5)
            binding.node6 -> logic.onClick(this, 6)
            binding.node7 -> logic.onClick(this, 7)
            binding.node8 -> logic.onClick(this, 8)
            binding.node9 -> logic.onClick(this, 9)
            binding.node10 -> logic.onClick(this, 10)
            binding.node11 -> logic.onClick(this, 11)
            binding.node12 -> logic.onClick(this, 12)

            binding.adminButton -> {
                val dialog = CheckAdminDialog(this)
                dialog.show()
            }


        }
    }


}
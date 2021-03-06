package com.example.smartlocker.presentation.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.smartlocker.R
import com.example.smartlocker.databinding.ActivityDashboardBinding
import com.example.smartlocker.presentation.view.activity.DashBoard.AbnormalListViewAdapter
import com.example.smartlocker.presentation.view.activity.DashBoard.StaticDateListViewAdapter
import com.example.smartlocker.presentation.viewmodel.Static
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashBoardActivity: AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val static by lazy {
        ViewModelProvider(this)[Static::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setChartSync()
        setTime()
        initAbnormalListView()
        observe()
        initClickListener()
    }

    private fun setChartSync(){
        CoroutineScope(Dispatchers.IO).launch {
            static.setStatic(3)
            initDateListView()
            initChart()
            initResultView()
        }
    }

    private fun initClickListener(){
        binding.exitButton.setOnClickListener(this)
        binding.timeSettingToggleButton.setOnClickListener(this)
        binding.dateInitButton.setOnClickListener(this)
    }

    private fun observe(){
        static.timeSetting.observe(this) {
            initAbnormalListView()
        }
    }

    private fun initAbnormalListView(){
        CoroutineScope(Dispatchers.Main).launch {
            val abnormalList = static.getAbnormalList()
            val adapter = AbnormalListViewAdapter(abnormalList)
            binding.abnormalListView.adapter = adapter
        }
    }

    private fun initDateListView(){
        CoroutineScope(Dispatchers.Main).launch {
            val list = static.getDateList()
            val adapter = StaticDateListViewAdapter(list)
            binding.staticDateListView.adapter = adapter
        }
    }

    private fun initResultView(){
        CoroutineScope(Dispatchers.Main).launch {
            when(static.setResult()){
                0-> binding.resultTextView.text = "????????? ??????"
                1-> binding.resultTextView.text = "????????? ??????"
                2-> binding.resultTextView.text = "????????? ?????? ??????"
                3-> binding.resultTextView.text = "????????? ??????\n?????? ?????? ??????"
            }
        }
    }


    override fun onClick(v: View?) {
        when(v){
            binding.exitButton -> {
                val intent = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, intent, null)
            }
            binding.timeSettingToggleButton -> {
                showTimePopup(v)
            }

            binding.dateInitButton -> {
                showInitPopup(v)
            }
        }
    }

    private fun showTimePopup(v: View){
        val popup = PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@DashBoardActivity)
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.popup, popup.menu)
        popup.show()
    }

    private fun showInitPopup(v: View) {
        val popup = PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@DashBoardActivity)
        }
        val inflater : MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.date_init_popup, popup.menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.timeSettinAll -> {
                static.setSharedPrefTimeSetting(0,this)
                true
            }
            R.id.timeSetting1 -> {
                static.setSharedPrefTimeSetting(24, this)
                true
            }
            R.id.timeSetting2 -> {
                static.setSharedPrefTimeSetting(48, this)
                true
            }
            R.id.timeSetting3 -> {
                static.setSharedPrefTimeSetting(72, this)
                true
            }

            R.id.dateInit -> {
                static.initStaticDate()
                recreate()
                true
            }
            else -> {false}
        }
    }
    private fun setTime(){
        val t = System.currentTimeMillis()
        val f = SimpleDateFormat("yyyy/MM/dd E", Locale.KOREAN)
        binding.timeTextView.text = f.format(t)
    }

    private fun initChart(){

        CoroutineScope(Dispatchers.Main).launch {
            static.initDataEntries()
            binding.chart.run {
                description.isEnabled = false //?????? ?????? ????????? ???????????? description ??????.
                setMaxVisibleValueCount(14) // ?????? ????????? ????????? ????????? 14?????? ???????????????.
                setPinchZoom(false) // ?????????(?????????????????? ?????? ??? ???????????????) ??????
                setDrawBarShadow(false)//???????????? ?????????
                setDrawGridBackground(false)//???????????? ????????????

                axisLeft.run { //?????? ???. ??? Y?????? ?????? ?????????.
                    axisMaximum = 5f // ????????? 5
                    axisMinimum = 0f // ????????? 0
                    granularity = 1f // 1 ???????????? ??????

                    setDrawLabels(true) // ??? ????????? ?????? (1,2,3,4,5)
                    setDrawGridLines(false) //?????? ?????? ??????
                    setDrawAxisLine(false) // ??? ????????? ??????
                    textColor = ContextCompat.getColor(context,R.color.white) // ?????? ????????? ?????? ??????
                    textSize = 14f //?????? ????????? ??????
                }
                xAxis.run {
                    position = XAxis.XAxisPosition.BOTTOM//X?????? ??????????????? ??????.
                    granularity = 1f // 1 ???????????? ?????? ??????
                    setCenterAxisLabels(false)
                    setLabelCount(14, false)
                    setDrawAxisLine(true) // ??? ??????
                    setDrawGridLines(false) // ??????
                    textColor = ContextCompat.getColor(context,R.color.white) //?????? ??????
                    valueFormatter = MyXAxisFormatter() // ??? ?????? ??? ???????????? ??????
                    textSize = 14f // ????????? ??????
                }
                axisRight.isEnabled = false // ????????? Y?????? ???????????? ??????.
                setTouchEnabled(false) // ????????? ???????????? ?????? ???????????? ??????
                animateY(1000) // ??????????????? ???????????? ??????????????? ??????
                legend.isEnabled = false //?????? ?????? ??????

            }

            val set = BarDataSet(static.setDataEntries(),"DataSet")//???????????? ????????? ??????
            set.color = ContextCompat.getColor(this@DashBoardActivity, R.color.fixing_warning)
            val dataSet :ArrayList<IBarDataSet> = ArrayList()
            dataSet.add(set)
            val data = BarData(dataSet)
            data.barWidth = 0.4f//?????? ?????? ????????????
            binding.chart.run {
                this.data = data //????????? ???????????? data ??? ????????????.
                setFitBars(true)
                invalidate()
            }
        }
    }

    inner class MyXAxisFormatter : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val time = arrayOf("8???", "9???","10???","11???","12???","1???","2???","3???","4???","5???","6???","7???","8???","9???")
            return time.getOrNull(value.toInt() - 1)?:value.toString()
        }
    }




}
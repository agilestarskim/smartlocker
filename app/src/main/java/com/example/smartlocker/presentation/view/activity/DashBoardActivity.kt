package com.example.smartlocker.presentation.view.activity

import android.content.Intent
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
import com.example.smartlocker.presentation.viewmodel.Static
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashBoardActivity: AppCompatActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val static by lazy {
        ViewModelProvider(this)[Static::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
        observeTimeSetting()
        initChart()
    }

    private fun initClickListener(){
        binding.exitButton.setOnClickListener(this)
        binding.timeSettingToggleButton.setOnClickListener(this)
    }

    private fun observeTimeSetting(){
        static.timeSetting.observe(this) {
            initAbnormalListView()
        }
    }

    private fun initAbnormalListView(){
        val abnormalList = static.getAbnormalList()
        val adapter = AbnormalListViewAdapter(abnormalList)
        binding.abnormalListView.adapter = adapter
    }


    override fun onClick(v: View?) {
        when(v){
            binding.exitButton -> {
                val intent = Intent(this, MainActivity::class.java)
                ContextCompat.startActivity(this, intent, null)
            }

            binding.timeSettingToggleButton -> {
                showPopup(v)
            }
        }
    }

    private fun showPopup(v: View){
        val popup = PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@DashBoardActivity)
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.popup, popup.menu)
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
            else -> {false}
        }
    }

    private fun initChart(){
        CoroutineScope(Dispatchers.Main).launch {
            static.initDataEntries()
            binding.chart.run {
                description.isEnabled = false //차트 옆에 별도로 표기되는 description이다. false로 설정하여 안보이게 했다.
                setMaxVisibleValueCount(14) // 최대 보이는 그래프 개수를 14개로 정해주었다.
                setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
                setDrawBarShadow(false)//그래프의 그림자
                setDrawGridBackground(false)//격자구조 넣을건지

                axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                    axisMaximum = 5f // 최댓값 5
                    axisMinimum = 0f // 최소값 0
                    granularity = 1f // 1 단위마다 표시

                    setDrawLabels(true) // 값 적는거 허용 (1,2,3,4,5)
                    setDrawGridLines(false) //격자 라인 활용
                    setDrawAxisLine(false) // 축 그리기 설정
                    textColor = ContextCompat.getColor(context,R.color.white) // 라벨 텍스트 컬러 설정
                    textSize = 14f //라벨 텍스트 크기
                }
                xAxis.run {
                    position = XAxis.XAxisPosition.BOTTOM//X축을 아래에다가 둔다.
                    granularity = 1f // 1 단위만큼 간격 두기
                    setCenterAxisLabels(false)
                    setLabelCount(14, false)
                    setDrawAxisLine(true) // 축 그림
                    setDrawGridLines(false) // 격자
                    textColor = ContextCompat.getColor(context,R.color.white) //라벨 색상
                    valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                    textSize = 14f // 텍스트 크기
                }
                axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
                setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
                animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
                legend.isEnabled = false //차트 범례 설정

            }
            val result = CoroutineScope(Dispatchers.Main).async {
                return@async static.setDataEntries()
            }.await()

            var set = BarDataSet(result,"DataSet")//데이터셋 초기화 하기
            set.color = ContextCompat.getColor(this@DashBoardActivity,R.color.fixing_warning)
            val dataSet :ArrayList<IBarDataSet> = ArrayList()
            dataSet.add(set)
            val data = BarData(dataSet)
            data.barWidth = 0.5f//막대 너비 설정하기
            binding.chart.run {
                this.data = data //차트의 데이터를 data로 설정해줌.
                setFitBars(true)
                invalidate()
            }
        }
    }

    inner class MyXAxisFormatter : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val time = arrayOf("8시", "9시","10시","11시","12시","1시","2시","3시","4시","5시","6시","7시","8시","9시")
            return time.getOrNull(value.toInt() - 1)?:value.toString()
        }
    }




}
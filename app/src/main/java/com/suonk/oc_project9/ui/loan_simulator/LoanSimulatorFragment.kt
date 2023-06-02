package com.suonk.oc_project9.ui.loan_simulator

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.FragmentLoanSimulatorBinding
import com.suonk.oc_project9.ui.main.MainActivity
import com.suonk.oc_project9.ui.map.MapFragment
import com.suonk.oc_project9.ui.real_estates.list.RealEstatesListFragment
import com.suonk.oc_project9.utils.showToast
import com.suonk.oc_project9.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanSimulatorFragment : Fragment(R.layout.fragment_loan_simulator), OnChartValueSelectedListener {

    private val viewModel by viewModels<LoanSimulatorViewModel>()
    private val binding by viewBinding(FragmentLoanSimulatorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculateButton.setOnClickListener {
            viewModel.onMoneyValueValidation(
                binding.moneyNeededContent.text?.toString(),
                binding.interestRateContent.text?.toString(),
                binding.monthsNeededContent.text?.toString()
            )
        }

        viewModel.toastMessageSingleLiveEvent.observe(viewLifecycleOwner) {
            it.showToast(requireContext())
        }
        setupDataFromViewModelToView()
        setupBottomNavigationView()
    }

    private fun setupDataFromViewModelToView() {
        viewModel.loanSimulatorViewStateLiveData.observe(viewLifecycleOwner) {
            binding.pieChartTitle.text = it.result
            setupPieChart(it)
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigation.menu.findItem(R.id.nav_loan_simulator).isChecked = true
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val fragmentToCommit = when (menuItem.itemId) {
                R.id.nav_real_estate -> {
                    RealEstatesListFragment()
                }
                R.id.nav_map -> {
                    MapFragment()
                }
                R.id.nav_loan_simulator -> {
                    LoanSimulatorFragment()
                }
                else -> {
                    RealEstatesListFragment()
                }
            }

            replaceFragment(fragmentToCommit, "RealEstatesListFragment / MapFragment")

            true
        }
    }

    private fun setupPieChart(loanSimulatorViewState: LoanSimulatorViewState) {
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5F, 5F, 5F, 5F)

        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(Color.WHITE)

//        dataPieChart.setTransparentCircleColor(Color.WHITE)
//        dataPieChart.setTransparentCircleAlpha(110)

        binding.pieChart.holeRadius = 58f
        binding.pieChart.transparentCircleRadius = 61f

        binding.pieChart.setDrawCenterText(true)

        binding.pieChart.rotationAngle = 0F
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true

        binding.pieChart.setOnChartValueSelectedListener(this)

//        seekBarX.setProgress(4);
//        seekBarY.setProgress(10);

        binding.pieChart.animateY(100, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        val l = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setEntryLabelTextSize(12f)

        setupDataToPieChart(loanSimulatorViewState)
    }

    private fun setupDataToPieChart(loanSimulatorViewState: LoanSimulatorViewState) {
        val colors: ArrayList<Int> = ArrayList()
        val entries: List<PieEntry> = loanSimulatorViewState.list.map {
            colors.add(it.color)
            PieEntry(it.value.toFloat(), "${it.title} : ${it.value}", resources.getDrawable(R.drawable.ic_add_image))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 1f
        dataSet.selectionShift = 20F

//        for (c in TEST_COLORS) colors.add(c)
//        for (c in JOYFUL_COLORS) colors.add(c)
//        for (c in COLORFUL_COLORS) colors.add(c)
//        for (c in LIBERTY_COLORS) colors.add(c)
//        for (c in PASTEL_COLORS) colors.add(c)

//        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        binding.pieChart.data = data

        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {}

    override fun onNothingSelected() {}

    private fun replaceFragment(fragment: Fragment, tag: String) {
        if (requireActivity() is MainActivity) {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).addToBackStack(null)
                .commit()
        }
    }
}
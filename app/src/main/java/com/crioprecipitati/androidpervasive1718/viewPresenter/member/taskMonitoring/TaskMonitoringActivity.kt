package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.R.id.lifeParametersLinearLayout
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.utils.setHealthParameterValue
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_monitoring.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    private val parametersViews: HashMap<LifeParameters, Pair<TextView, TextView>> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doAsync {
            Thread.sleep(1000)
            uiThread {
                initializeParametersViews()
                createNewTable(LifeParameters.values().toList())
                showEmptyTask()
            }
        }
    }

    override fun showNewTask(augmentedTask: AugmentedTask) {
        activityName.text = augmentedTask.activityName
        createNewTable(augmentedTask.linkedParameters)
        btnEndOperation.isEnabled = true
    }

    override fun showEmptyTask() {
        activityName.text = "In attesa..."
        lifeParametersLinearLayout.removeAllViews()
        btnEndOperation.isEnabled = false
    }

    override fun showAlarmedTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHealthParameterValues(parameter: LifeParameters, value: Double) {
        parametersViews[parameter]!!.second.setHealthParameterValue(value.toString())
    }

    private fun createNewTable(parameters: List<LifeParameters>) {
        lifeParametersLinearLayout.setBackgroundColor(Color.LTGRAY)

        parameters.forEach {

            parametersViews[it]!!.first.height = lifeParametersLinearLayout.height/2
            parametersViews[it]!!.first.width = lifeParametersLinearLayout.width/parameters.size
            parametersViews[it]!!.second.height = lifeParametersLinearLayout.height/2
            parametersViews[it]!!.second.width = lifeParametersLinearLayout.width/parameters.size

            val paramWrapper = LinearLayout(this)
            paramWrapper.layoutParams = ViewGroup.LayoutParams(lifeParametersLinearLayout.width/parameters.size, lifeParametersLinearLayout.height)
            paramWrapper.addView(parametersViews[it]!!.first)
            paramWrapper.addView(parametersViews[it]!!.second)
            lifeParametersLinearLayout.addView(paramWrapper, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun initializeParametersViews() {
        LifeParameters.values().forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun createParameterView(parameter: LifeParameters): Pair<TextView, TextView> {
        val parameterAcronymView = TextView(this)
        parameterAcronymView.setBackgroundColor(Color.GRAY)
        parameterAcronymView.gravity = Gravity.CENTER
        parameterAcronymView.setTextColor(Color.BLACK)
        parameterAcronymView.textSize = 18.0F
        parameterAcronymView.text = parameter.acronym

        val parameterValueView = TextView(this)
        parameterValueView.setBackgroundColor(Color.WHITE)
        parameterValueView.gravity = Gravity.CENTER
        parameterAcronymView.textSize = 25.0F
        parameterValueView.text = ""
        parameterValueView.setTextColor(Color.BLACK)

        return Pair(parameterAcronymView, parameterValueView)
    }
}
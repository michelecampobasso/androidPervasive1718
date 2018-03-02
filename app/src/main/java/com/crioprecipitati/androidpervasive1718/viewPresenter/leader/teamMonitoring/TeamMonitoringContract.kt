package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.*
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView


interface TeamMonitoringContract {

    interface TeamMonitoringView : BaseView {

        fun showAndUpdateMemberList()

        fun showAndUpdateTaskList(member: Member, task: AugmentedTask)

        fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double)

        fun showActivitySelectionActivity()

        fun onSessionClosed()

    }

    interface TeamMonitoringPresenter : BasePresenter<TeamMonitoringView> {

        val memberList: MutableList<AugmentedMember>

        var member: Member?

        fun onTaskDeleted()

        fun onMemberSelected(userIndex: Int)

        fun onSessionCloseRequested()

        fun addTask(member: Member, activity: Activity)
    }
}
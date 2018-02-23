package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import model.*

class ActivitySelectionPresenterImpl : BasePresenterImpl<ActivitySelectionContract.ActivitySelectionView>(), ActivitySelectionContract.ActivitySelectionPresenter, WSObserver {

    override var activityList: List<Activity> = listOf()
    private val taskWebSocketHelper: TaskWSAdapter = TaskWSAdapter
    private val notifierWebSocket: NotifierWSAdapter = NotifierWSAdapter

    override fun onActivityTypeSelected(activityTypeId: Int) {
        val selectedActivities: List<Activity> = activityList.filter { it.activityTypeId == activityTypeId }
        view?.showActivityByActivityType(selectedActivities)

    }

    override fun onActivitySelected(currentMember: Member) {
        taskWebSocketHelper.webSocket.send(PayloadWrapper(Prefs.sessionId, WSOperations.ADD_TASK, TaskAssignment(currentMember, Task.emptyTask()).toJson()).toJson())
        notifierWebSocket.webSocket.send(PayloadWrapper(Prefs.sessionId, WSOperations.SUBSCRIBE, currentMember.toJson()).toJson())

    }

    override fun getActivityByActivityType() {
        val members: List<Member> = listOf(Member(1, "Leader"))
        taskWebSocketHelper.webSocket.send(PayloadWrapper(Prefs.sessionId, WSOperations.GET_ALL_ACTIVITIES, MembersAdditionNotification(members).toJson()).toJson())
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun activityAdditionHandling() {
                val activityAddition: ActivityAdditionNotification = this.objectify(body)
                activityList = activityAddition.activities
            }

            when (payloadWrapper.subject) {
                WSOperations.SET_ALL_ACTIVITIES -> activityAdditionHandling()
                else -> null
            }
        }
    }
}
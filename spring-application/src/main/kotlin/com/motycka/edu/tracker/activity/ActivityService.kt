package com.motycka.edu.tracker.activity

import com.motycka.edu.tracker.activity.model.Activity
import com.motycka.edu.tracker.activity.model.NewActivity
import com.motycka.edu.tracker.authorization.AuthorizationService
import com.motycka.edu.tracker.error.ConflictException
import com.motycka.edu.tracker.error.ForbiddenException
import org.springframework.stereotype.Service

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val authorizationService: AuthorizationService
) {

    fun getActivities(): List<Activity>  {
        return activityRepository.selectActivities(getCurrentUserId())
    }

    fun getActivityById(id: Long): Activity? {
        return activityRepository.selectActivityById(getCurrentUserId(), id)
    }

    fun createActivity(activity: NewActivity): Activity? {
        val userId = getCurrentUserId()

        if (activity.userId != userId) {
            throw ForbiddenException("User is not allowed to create activity for another user")
        }

        if (activityRepository.existsByName(userId, activity.name)) {
            throw ConflictException("Activity with this name already exists")
        }

        return activityRepository.insertActivity(activity)
    }

    fun updateActivity(activity: Activity): Activity? {
        if (!isPermitted(getCurrentUserId(), activity.id)) {
            throw ForbiddenException("User is not allowed to update this activity")
        }
        return activityRepository.updateActivity(getCurrentUserId(), activity)
    }

    fun deleteActivity(id: Long) {
        if (!isPermitted(getCurrentUserId(), id)) {
            throw ForbiddenException("User is not allowed to delete this activity")
        }
        activityRepository.deleteActivity(getCurrentUserId(), id)
    }

    private fun getCurrentUserId(): Long {
        return authorizationService.getCurrentUser().id
    }

    private fun isPermitted(userId: Long, activityId: Long): Boolean {
        return activityRepository.selectActivityById(userId, activityId)?.userId == userId
    }
}

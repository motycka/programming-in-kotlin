package com.motycka.edu.tracker.activity

import com.motycka.edu.tracker.activity.model.Activity
import com.motycka.edu.tracker.activity.model.NewActivity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/activities")
class ActivityController(
    private val activityService: ActivityService
) {

    @GetMapping
    fun getActivityTypes(): List<Activity> {
        return activityService.getActivities()
    }

    @GetMapping("{id}")
    fun getActivityType(@PathVariable(value = "id") id: Long): Activity? {
        return activityService.getActivityById(id)
    }

    @PostMapping
    fun createActivityType(@RequestBody activity: NewActivity): Activity? {
        return activityService.createActivity(activity)
    }

    @PutMapping
    fun updateActivityType(@RequestBody activity: Activity): Activity? {
        return activityService.updateActivity(activity)
    }

    @DeleteMapping("{id}")
    fun deleteActivityType(@PathVariable(value = "id") id: Long) {
        activityService.deleteActivity(id)
    }

}

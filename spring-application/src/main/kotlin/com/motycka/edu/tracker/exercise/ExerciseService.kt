package com.motycka.edu.tracker.exercise

import com.motycka.edu.tracker.authorization.AuthorizationService
import com.motycka.edu.tracker.error.ForbiddenException
import com.motycka.edu.tracker.exercise.model.Exercise
import com.motycka.edu.tracker.exercise.model.NewExercise
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
    private val authorizationService: AuthorizationService
) {

    fun getExercises(): List<Exercise> {
        return exerciseRepository.selectAll(getCurrentUserId())
    }

    fun getExerciseById(id: Long): Exercise {
        return exerciseRepository.selectById(getCurrentUserId(), id)
    }

    fun createExercise(exercise: NewExercise): Exercise {

        if (exercise.userId != getCurrentUserId()) {
            throw ForbiddenException("User is not allowed to create exercise for another user")
        }

        return exerciseRepository.insert(exercise)
    }

    fun updateExercise(exercise: Exercise): Exercise {
        if (exercise.userId != getCurrentUserId()) {
            throw ForbiddenException("User is not allowed to update exercise for another user")
        }

        return exerciseRepository.update(getCurrentUserId(), exercise)
    }

    fun deleteExercise(id: Long) {
//        if (exercise.userId() != getCurrentUserId()) {
//            throw new AuthorizationException("User is not allowed to create exercise for another user")
//        }

        exerciseRepository.delete(getCurrentUserId(), id)
    }

    private fun getCurrentUserId(): Long {
        return authorizationService.getCurrentUser().id
    }

}

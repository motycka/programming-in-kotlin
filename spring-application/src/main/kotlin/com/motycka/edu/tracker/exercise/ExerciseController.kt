package com.motycka.edu.tracker.exercise

import com.motycka.edu.tracker.exercise.model.Exercise
import com.motycka.edu.tracker.exercise.model.NewExercise
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exercises")
class ExerciseController(
    private val exerciseService: ExerciseService
) {

    @GetMapping
    fun getExercises(): List<Exercise>  {
        return exerciseService.getExercises()
    }

    @GetMapping("/{id}")
    fun getExercise(@PathVariable("id") id: Long): Exercise {
        return exerciseService.getExerciseById(id)
    }

    @PostMapping
    fun createExercise(@RequestBody exercise: NewExercise): Exercise {
        return exerciseService.createExercise(exercise)
    }

    @PutMapping("/{id}")
    fun updateExercise(
        @PathVariable("id") id: Long,
        @RequestBody exercise: Exercise
    ): Exercise {
        return exerciseService.updateExercise(exercise)
    }

    @DeleteMapping("/{id}")
    fun deleteExercise(@PathVariable("id") id: Long) {
        exerciseService.deleteExercise(id)
    }

}

package zachinio.choreograper

import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import zachinio.choreograper.animation.Animation
import java.lang.ref.WeakReference

class Choreographer {

    companion object {
        private val instance: Choreographer = Choreographer()

        fun get(): Choreographer {
            return instance
        }
    }

    private val animations = ArrayList<Animation>()

    fun addAnimation(view: View, direction: Direction, animationType: AnimationType, duration: Long): Choreographer {
        animations.add(Animation.create(WeakReference(view), direction, animationType, duration))
        return this
    }

    fun addAnimationAsync(
        view: View,
        direction: Direction,
        animationType: AnimationType,
        duration: Long
    ): Choreographer {
        val animation = Animation.create(WeakReference(view), direction, animationType, duration)
        animation.async = true
        animations.add(animation)
        return this
    }

    fun animate() {
        if (animations.isEmpty()) {
            return
        }
        var completable = animations[0].animate()
        animations.removeAt(0)
        while (animations.isNotEmpty()) {
            completable = if (animations[0].async) {
                completable.mergeWith(animations[0].animate())
            } else {
                completable.concatWith(animations[0].animate())
            }
            animations.removeAt(0)
        }
        completable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    enum class Direction {
        TOP, RIGHT, BOTTOM, LEFT, UP, DOWN
    }

    enum class AnimationType {
        ENTER, SCALE
    }
}
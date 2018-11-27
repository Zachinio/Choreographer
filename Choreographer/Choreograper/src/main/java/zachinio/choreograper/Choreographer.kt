package zachinio.choreograper

import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import zachinio.choreograper.animation.Animation
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

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

    fun wait(mills: Long) : Choreographer {
        if (animations.size == 0) {
            throw IllegalStateException("wait must be used after addAnimation")
        }
        animations.last().wait = mills
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
            animations[0].wait?.let {
                completable = completable.delay(it, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            }
            animations.removeAt(0)
        }
        completable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    enum class Direction {
        TOP, RIGHT, BOTTOM, LEFT, UP, DOWN, OUT, IN
    }

    enum class AnimationType {
        ENTER, SCALE, FADE
    }
}
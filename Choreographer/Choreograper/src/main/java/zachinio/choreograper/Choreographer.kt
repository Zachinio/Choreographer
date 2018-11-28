package zachinio.choreograper

import io.reactivex.android.schedulers.AndroidSchedulers
import zachinio.choreograper.animation.Animation
import java.util.concurrent.TimeUnit

class Choreographer {

    companion object {
        private val instance: Choreographer = Choreographer()

        fun get(): Choreographer {
            return instance
        }
    }

    private val animations = ArrayList<Animation>()

    fun addAnimation(animation: Animation): Choreographer {
        animations.add(animation)
        return this
    }

    fun addAnimationAsync(animation: Animation): Choreographer {
        animation.async = true
        return addAnimation(animation)
    }

    fun wait(mills: Long): Choreographer {
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
}
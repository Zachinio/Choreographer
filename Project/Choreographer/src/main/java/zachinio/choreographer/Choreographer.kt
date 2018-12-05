package zachinio.choreographer

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import zachinio.choreographer.animation.Animation

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

    fun clearAll() {
        animations.clear()
    }

    fun removeLast() {
        if (!animations.isEmpty()) {
            animations.removeAt(animations.size - 1)
        }
    }

    fun animate() {
        animate(null)
    }

    fun animate(endAction: (() -> Unit)?) {
        if (animations.isEmpty()) {
            return
        }

        var completable =
            getAsyncAnimations()?.let {
                it
            } ?: run {
                animations.removeAt(0).getAnimation()
            }
        while (animations.isNotEmpty()) {
            val animationCompletable = getAsyncAnimations()?.let {
                it
            } ?: run {
                animations.removeAt(0).getAnimation()
            }

            completable = animationCompletable.startWith(completable)

        }
        completable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                endAction?.invoke()
            }
    }

    private fun getAsyncAnimations(): Completable? {
        if (animations.size < 2 || !animations[1].async) {
            return null
        }
        var completable = animations.removeAt(0).getAnimation()
        while (animations.isNotEmpty() && animations[0].async) {
            completable = completable.mergeWith(animations.removeAt(0).getAnimation())
        }
        return completable
    }
}
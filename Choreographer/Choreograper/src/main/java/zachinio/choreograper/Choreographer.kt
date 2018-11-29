package zachinio.choreograper

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import zachinio.choreograper.animation.Animation

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

        var completable =
            getAsyncAnimations()?.let {
                it
            } ?: run {
                animations.removeAt(0).animate()
            }
        while (animations.isNotEmpty()) {
            val animation = animations[0]
            val animationCompletable = getAsyncAnimations()?.let {
                it
            } ?: run {
                animations.removeAt(0).animate()
            }
            completable = animationCompletable.startWith(completable)

//            animation.wait?.let {
//                completable = completable.delay(it, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//            }
        }
        completable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun getAsyncAnimations(): Completable? {
        if (animations.size < 2 || !animations[1].async) {
            return null
        }
        var completable = animations.removeAt(0).animate()
        while (animations.isNotEmpty() && animations[0].async) {
            completable = completable.mergeWith(animations.removeAt(0).animate())
        }
        return completable
    }
}
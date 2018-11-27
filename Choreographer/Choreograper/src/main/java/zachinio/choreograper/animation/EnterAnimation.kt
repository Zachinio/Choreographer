package zachinio.choreograper.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import zachinio.choreograper.Choreographer
import java.lang.ref.WeakReference

internal class EnterAnimation(
    private val viewWeak: WeakReference<View>,
    private val direction: Choreographer.Direction,
    private val animationType: Choreographer.AnimationType,
    private val duration: Long
) : Animation() {

    init {
        getViewPosition(viewWeak)
    }

    override fun animate(): Completable {
        return viewPositionSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.computation())
            .flatMapCompletable {
                return@flatMapCompletable Completable.create { emitter ->
                    if (viewWeak.get() == null) {
                        emitter.onComplete()
                    }
                    setStartingPosition()
                    viewWeak.get()?.visibility = View.VISIBLE
                    val objectAnimatorX = ObjectAnimator.ofFloat(viewWeak.get(), "x", it.x.toFloat())
                    val objectAnimatorY = ObjectAnimator.ofFloat(viewWeak.get(), "y", it.y.toFloat())
                    val animatorSet = AnimatorSet()
                    animatorSet.duration = duration
                    animatorSet.removeAllListeners()
                    animatorSet.playTogether(objectAnimatorX, objectAnimatorY)
                    animatorSet.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                        override fun onAnimationCancel(p0: Animator?) {
                        }

                        override fun onAnimationStart(p0: Animator?) {
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            emitter.onComplete()
                        }
                    })
                    animatorSet.start()
                }
            }
    }

    private fun setStartingPosition() {
        val displayMetrics = viewWeak.get()?.resources?.displayMetrics
        when (direction) {
            Choreographer.Direction.TOP -> viewWeak.get()?.y = 0 - viewWeak.get()?.height?.toFloat()!!
            Choreographer.Direction.RIGHT -> viewWeak.get()?.x = displayMetrics?.widthPixels!! +
                    viewWeak.get()?.width?.toFloat()!!
            Choreographer.Direction.BOTTOM -> viewWeak.get()?.y = displayMetrics?.heightPixels!! +
                    viewWeak.get()?.height?.toFloat()!!
            Choreographer.Direction.LEFT -> viewWeak.get()?.x = 0 - viewWeak.get()?.width?.toFloat()!!
            else -> throw IllegalStateException(direction.name + " Can't be used with " + animationType.name)
        }
    }
}
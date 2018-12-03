package zachinio.choreographer.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class EnterAnimation(view: View, private val direction: Direction, private val duration: Long) : Animation() {

    private var viewWeak: WeakReference<View> = WeakReference(view)

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
                    animatorSet.addListener(object : AnimationListener(){
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
            Direction.TOP -> viewWeak.get()?.y = 0 - viewWeak.get()?.height?.toFloat()!!
            Direction.RIGHT -> viewWeak.get()?.x = displayMetrics?.widthPixels!! +
                    viewWeak.get()?.width?.toFloat()!!
            Direction.BOTTOM -> viewWeak.get()?.y = displayMetrics?.heightPixels!! +
                    viewWeak.get()?.height?.toFloat()!!
            Direction.LEFT -> viewWeak.get()?.x = 0 - viewWeak.get()?.width?.toFloat()!!
        }
    }

    enum class Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }
}
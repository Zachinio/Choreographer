package zachinio.choreograper.animation

import android.view.View
import android.view.animation.AnimationUtils
import io.reactivex.Completable
import zachinio.choreograper.R
import java.lang.ref.WeakReference

class FadeAnimation(
    view: View,
    private val direction: Direction,
    private val duration: Long
) : Animation() {

    private val viewWeak = WeakReference(view)

    override fun animate(): Completable {
        setVisibilityState(false)
        return Completable.create {
            val scaleAnimation = getFadeAnimation()
            scaleAnimation?.duration = duration
            scaleAnimation?.setAnimationListener(object : AnimationListener() {
                override fun onAnimationEnd(p0: android.view.animation.Animation?) {
                    it.onComplete()
                }
            })
            viewWeak.get()?.startAnimation(scaleAnimation)
            setVisibilityState(true)
        }
    }

    private fun getFadeAnimation(): android.view.animation.Animation? {
        return when (direction) {
            Direction.IN -> {
                viewWeak.get()?.visibility = View.INVISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.fade_in)
            }
            Direction.OUT -> {
                viewWeak.get()?.visibility = View.VISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.fade_out)
            }
        }
    }

    private fun setVisibilityState(isEndingState: Boolean) {
        when (direction) {
            Direction.IN -> viewWeak.get()?.visibility =
                    if (isEndingState) View.VISIBLE else View.INVISIBLE
            Direction.OUT -> viewWeak.get()?.visibility =
                    if (isEndingState) View.INVISIBLE else View.VISIBLE
        }
    }

    enum class Direction {
        OUT, IN
    }
}
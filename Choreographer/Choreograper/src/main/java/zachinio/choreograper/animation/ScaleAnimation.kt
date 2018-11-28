package zachinio.choreograper.animation

import android.view.View
import android.view.animation.AnimationUtils
import io.reactivex.Completable
import zachinio.choreograper.R
import java.lang.ref.WeakReference

class ScaleAnimation(
    view: View,
    private val direction: Direction,
    private val duration: Long
) : Animation() {

    private var viewWeak: WeakReference<View> = WeakReference(view)

    override fun animate(): Completable {
        setVisibilityState(false)
        return Completable.create {
            val scaleAnimation = getScaleAnimation()
            scaleAnimation?.duration = duration
            scaleAnimation?.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationRepeat(p0: android.view.animation.Animation?) {

                }

                override fun onAnimationStart(p0: android.view.animation.Animation?) {

                }

                override fun onAnimationEnd(p0: android.view.animation.Animation?) {
                    it.onComplete()
                }
            })
            viewWeak.get()?.startAnimation(scaleAnimation)
            setVisibilityState(true)
        }
    }

    private fun getScaleAnimation(): android.view.animation.Animation? {
        return when (direction) {
            Direction.UP -> {
                viewWeak.get()?.visibility = View.INVISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.scale_up)
            }
            Direction.DOWN -> {
                viewWeak.get()?.visibility = View.VISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.scale_down)
            }
        }
    }

    private fun setVisibilityState(isEndingState: Boolean) {
        when (direction) {
            Direction.UP -> viewWeak.get()?.visibility =
                    if (isEndingState) View.VISIBLE else View.INVISIBLE
            Direction.DOWN -> viewWeak.get()?.visibility =
                    if (isEndingState) View.INVISIBLE else View.VISIBLE
        }
    }

    enum class Direction {
        UP, DOWN
    }
}
package zachinio.choreograper.animation

import android.view.View
import android.view.animation.AnimationUtils
import io.reactivex.Completable
import zachinio.choreograper.Choreographer
import zachinio.choreograper.R
import java.lang.ref.WeakReference

internal class ScaleAnimation(
    private val viewWeak: WeakReference<View>,
    private val direction: Choreographer.Direction,
    private val animationType: Choreographer.AnimationType,
    private val duration: Long
) : Animation() {

    override fun animate(): Completable {
        setVisibilityState(false)
        return Completable.create {
            val scaleAnimation = getScaleAnimation()
            scaleAnimation?.duration = duration
            viewWeak.get()?.startAnimation(scaleAnimation)
            setVisibilityState(true)
        }
    }

    private fun getScaleAnimation(): android.view.animation.Animation? {
        return when (direction) {
            Choreographer.Direction.UP -> {
                viewWeak.get()?.visibility = View.INVISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.scale_up)
            }
            Choreographer.Direction.DOWN -> {
                viewWeak.get()?.visibility = View.VISIBLE
                AnimationUtils.loadAnimation(viewWeak.get()?.context, R.anim.scale_down)
            }
            else -> throw IllegalStateException(direction.name + " can't be used with " + animationType.name)
        }
    }

    private fun setVisibilityState(isEndingState: Boolean) {
        when (direction) {
            Choreographer.Direction.UP -> viewWeak.get()?.visibility =
                    if (isEndingState) View.VISIBLE else View.INVISIBLE
            Choreographer.Direction.DOWN -> viewWeak.get()?.visibility =
                    if (isEndingState) View.INVISIBLE else View.VISIBLE
            else -> {
                throw IllegalStateException(direction.name + " can't be used with " + animationType.name)
            }
        }
    }
}
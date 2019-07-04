package zachinio.choreographer.animation

import android.graphics.drawable.ClipDrawable
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import io.reactivex.Completable
import io.reactivex.CompletableEmitter

class ProgressImageLoader(private val imageView: ImageView, private val step: Int, private val direction: Direction) :
    Animation() {

    var level = 0
    var drawable: ClipDrawable? = null
    var handler = Handler()
    var emitter: CompletableEmitter? = null

    private val animateImage = Runnable { animateLevelRunnable() }

    override fun animate(): Completable {
        return Completable.create {
            if (step < 0 || step > 10000) {
                it.onComplete()
                return@create
            }
            emitter = it
            drawable = ClipDrawable(imageView.drawable, getDirection(direction), ClipDrawable.HORIZONTAL)
            imageView.setImageDrawable(drawable)
            imageView.visibility = View.VISIBLE

            drawable?.level = level
            handler.post(animateImage)
        }
    }

    private fun getDirection(direction: Direction): Int {
        return when (direction) {
            Direction.TOP -> Gravity.TOP
            Direction.BOTTOM -> Gravity.BOTTOM
            Direction.LEFT -> Gravity.LEFT
            else -> Gravity.RIGHT
        }
    }

    private fun animateLevelRunnable() {
        level += step
        if (level > 10000) {
            stopHandler()
        }
        drawable?.level = level
        if (level <= 10000) {
            handler.postDelayed(animateImage, 10)
        } else {
            stopHandler()
        }
    }

    private fun stopHandler() {
        emitter?.onComplete()
        handler.removeCallbacks(animateImage)
    }
}
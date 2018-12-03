package zachinio.choreographer.animation

import android.graphics.Point
import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.SingleSubject
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

abstract class Animation {

    internal val viewPositionSingle: SingleSubject<Point> = SingleSubject.create()
    internal var async = false
    internal var wait: Long? = null

    internal abstract fun animate(): Completable

    internal fun getViewPosition(viewWeak: WeakReference<View>) {
        viewWeak.get()?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewWeak.get()?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                viewPositionSingle.onSuccess(Point(viewWeak.get()?.x?.toInt()!!, viewWeak.get()?.y?.toInt()!!))
            }
        })
    }

    internal fun getAnimation(): Completable {
        return wait?.let {
            animate().delay(it, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
        } ?: run {
            animate()
        }
    }
}
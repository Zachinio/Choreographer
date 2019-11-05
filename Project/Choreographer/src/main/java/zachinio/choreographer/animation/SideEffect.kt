package zachinio.choreographer.animation

import io.reactivex.Completable

class SideEffect(val runnable: Runnable) : Animation() {

    override fun animate(): Completable {
        return Completable.create{
            runnable.run()
            it.onComplete()
        }
    }
}
package zachinio.sample.choreographer.ui

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import zachinio.choreograper.Choreographer
import zachinio.sample.choreographer.R


class SampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Choreographer.get()
            .addAnimation(helloWorld, Choreographer.Direction.TOP, Choreographer.AnimationType.ENTER, 560)
            .addAnimationAsync(childView, Choreographer.Direction.UP, Choreographer.AnimationType.SCALE, 2560)
            .animate()
    }
}
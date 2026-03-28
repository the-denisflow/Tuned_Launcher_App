package com.example.tuned_launcher_app.presentation.mainpage.components.scoped

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

enum class DragPhase { IDLE, TENSION, SNAPPING, FREE_DRAG }

internal class HorizontalGestureHandler(
    private val scope: CoroutineScope,
    private val density: Density,
    private val offsetAnimatable: Animatable<Float, AnimationVector1D>,
    private val screenWidthPx: Float
) {
    private var dragPhase: DragPhase = DragPhase.IDLE
    private var accumulatedDragX: Float = 0f

    fun onDragStarted() {
        dragPhase = DragPhase.TENSION
        accumulatedDragX = 0f
        scope.launch { offsetAnimatable.stop() }
    }

    fun onDrag(dragAmount: Float) {
        accumulatedDragX += dragAmount
        val snapThresholdPx = 100f * density.density
        if (abs(accumulatedDragX) < snapThresholdPx) {
            val maxTensionOffsetPx = 30f * density.density
            val dragFraction = (abs(accumulatedDragX) / snapThresholdPx).coerceIn(0f, 1f)
            val tensionOffset = lerp(0f, maxTensionOffsetPx, dragFraction)
            scope.launch {
                offsetAnimatable.snapTo(tensionOffset * accumulatedDragX.sign)
            }
        }

    }

    fun onDragEnd() {
        dragPhase = DragPhase.IDLE
    }
}

@Composable
internal fun rememberHorizontalGestureHandler(
    scope: CoroutineScope,
    density: Density,
    offsetAnimatable: Animatable<Float, AnimationVector1D>,
    screenWidthPx: Float
) = HorizontalGestureHandler(
    scope,
    density,
    offsetAnimatable,
    screenWidthPx
)

internal fun Modifier.manageHorizontalGesture(
    enabled : Boolean = true,
    handler: HorizontalGestureHandler
): Modifier {
    if (!enabled) return this
    return this.pointerInput(enabled, handler) {
        detectHorizontalDragGestures (
            onDragStart = {
                println("Denis -> drag started")
                handler.onDragStarted() },
            onHorizontalDrag = { change, dragAmount ->
                println("Denis -> onDrag amount: $dragAmount")
                change.consume()
                handler.onDrag(dragAmount)
            },
            onDragEnd = {
                println("Denis -> onDragEnd")
                handler.onDragEnd()
            }
        )
    }
}

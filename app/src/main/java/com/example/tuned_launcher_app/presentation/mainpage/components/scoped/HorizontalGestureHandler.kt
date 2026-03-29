package com.example.tuned_launcher_app.presentation.mainpage.components.scoped

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

enum class DragPhase { IDLE, TENSION, SNAPPING, FREE_DRAG }


// TODO:
// - dismissing if drag exceed 40% of [screenWidthPx]
// - otherwise back to 0

/**
 * Handles the horizontal drag gesture for a UI element
 * Drives [offsetAnimatable] through three phases:
 * - Tension: resist the drag, capped at 30dp
 * - Snapping: springs to the finger position once a threshold is crossed
 * - Free drag: follows the finger freely
 *
 * [hapticFeedback] triggers a vibration at the snap transition.
 */
internal class HorizontalGestureHandler(
    private val scope: CoroutineScope,
    private val density: Density,
    private val hapticFeedback: HapticFeedback, // todo understand what is it
    private val offsetAnimatable: Animatable<Float, AnimationVector1D>,
    private val screenWidthPx: Float
) {
    private companion object {
        const val SNAP_THRESHOLD_DP = 100f
        const val MAX_TENSION_OFFSET_DP = 30f
        const val DISMISS_THRESHOLD_FRACTION = 0.4f
        const val SNAPPING_DAMPING_RATIO = 0.8f
    }

    private var dragPhase: DragPhase = DragPhase.IDLE
    private var accumulatedDragX: Float = 0f

    fun onDragStarted() {
        dragPhase = DragPhase.TENSION
        accumulatedDragX = 0f
        scope.launch { offsetAnimatable.stop() }
    }

    fun onDrag(dragAmount: Float) {
        accumulatedDragX += dragAmount

        when (dragPhase) {
            DragPhase.TENSION -> {
                val snapThresholdPx = SNAP_THRESHOLD_DP * density.density
                if (abs(accumulatedDragX) < snapThresholdPx) {

                    val tensionOffset = computeTensionOffset(accumulatedDragX, snapThresholdPx)

                    scope.launch {
                        offsetAnimatable.snapTo(tensionOffset * accumulatedDragX.sign)
                    }
                } else {
                    dragPhase = DragPhase.SNAPPING
                }
            }

            DragPhase.SNAPPING -> {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                scope.launch {
                    offsetAnimatable.animateTo(
                        targetValue = accumulatedDragX,
                        animationSpec = spring(
                            dampingRatio = SNAPPING_DAMPING_RATIO,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                dragPhase = DragPhase.FREE_DRAG
            }

            DragPhase.FREE_DRAG -> {
                scope.launch {
                    offsetAnimatable.animateTo(
                        targetValue = accumulatedDragX,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    )
                }
            }

            DragPhase.IDLE -> Unit
        }
    }

    fun onDragEnd() {
        dragPhase = DragPhase.IDLE
            scope.launch {
                offsetAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
        }


    fun computeTensionOffset(accumulatedDragX: Float, snapThresholdPx: Float): Float {
        val maxTensionOffsetPx = MAX_TENSION_OFFSET_DP * density.density
        val dragFraction = (abs(accumulatedDragX) / snapThresholdPx).coerceIn(0f, 1f)
        return lerp(start = 0f, stop = maxTensionOffsetPx, dragFraction)
    }
}

@Composable
internal fun rememberHorizontalGestureHandler(
    scope: CoroutineScope,
    density: Density,
    hapticFeedback: HapticFeedback,
    offsetAnimatable: Animatable<Float, AnimationVector1D>,
    screenWidthPx: Float
) = HorizontalGestureHandler(
    scope,
    density,
    hapticFeedback,
    offsetAnimatable,
    screenWidthPx
)

internal fun Modifier.manageHorizontalGesture(
    enabled: Boolean = true,
    handler: HorizontalGestureHandler
): Modifier {
    if (!enabled) return this
    return this.pointerInput(enabled, handler) {
        detectHorizontalDragGestures(
            onDragStart = {
                println("Denis -> drag started")
                handler.onDragStarted()
            },
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

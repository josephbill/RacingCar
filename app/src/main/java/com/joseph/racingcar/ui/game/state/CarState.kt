package com.joseph.racingcar.ui.game.state

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import com.joseph.racingcar.ui.models.AccelerationData
import com.joseph.racingcar.ui.models.CarPosition
import com.joseph.racingcar.ui.models.RotationDirection
import com.joseph.racingcar.utils.Constants.ACCELERATION_X_Y_OFFSET_TRIGGER
import com.joseph.racingcar.utils.Constants.CAR_POSITION_PERCENTAGE_FROM_BOTTOM
import com.joseph.racingcar.utils.Constants.CAR_SIZE
import com.joseph.racingcar.utils.Constants.LANE_COUNT
import com.joseph.racingcar.utils.Constants.STREET_SIDE_PERCENTAGE_EACH

data class CarState(
    private val image: ImageBitmap,
    private var position: CarPosition = CarPosition.Middle
) {

    private var rotationDirection: RotationDirection? = null

    fun draw(
        drawScope: DrawScope,
        offsetIndex: Float
    ): Rect { //todo calculate `size` once per init
        drawScope.apply {
            val initialOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT
            val carOffsetX =
                initialOffsetX +
                        (laneSize / 2) - (CAR_SIZE / 2) +
                        (laneSize * offsetIndex).toInt()

            val dstOffset = IntOffset(
                x = carOffsetX,
                y = size.height.toInt() - (size.height.toInt() * CAR_POSITION_PERCENTAGE_FROM_BOTTOM / 100)
            )
            val dstSize = IntSize(width = CAR_SIZE, height = CAR_SIZE)
            val rect = Rect(offset = dstOffset.toOffset(), size = dstSize.toSize())

            withTransform({
                val rotationDegrees = when (rotationDirection) {
                    RotationDirection.Right -> 90
                    RotationDirection.Left -> -90
                    else -> 0
                }
                val progress = offsetIndex.rem(1.0)
                val degrees = if (progress < 0.5f)
                    rotationDegrees * progress
                else
                    rotationDegrees * (1 - progress)

                rotate(
                    degrees = degrees.toFloat(),
                    pivot = rect.center
                )
            }) {
                drawImage(
                    image = image,
                    dstOffset = dstOffset,
                    dstSize = dstSize
                )
            }
            return rect
        }
    }

    fun moveWithTapGesture(position: CarPosition) {
        val currentPositionIndex = this.position.fromLeftOffsetIndex()
        val nextPositionIndex = position.fromLeftOffsetIndex()

        if (nextPositionIndex == currentPositionIndex)
            return
        else if (nextPositionIndex > currentPositionIndex)
            moveRight()
        else
            moveLeft()

    }

    fun moveWithSwipeGesture(swipeDirection: com.joseph.racingcar.ui.models.SwipeDirection) {
        when (swipeDirection) {
            com.joseph.racingcar.ui.models.SwipeDirection.Right -> moveRight()
            com.joseph.racingcar.ui.models.SwipeDirection.Left -> moveLeft()
        }
    }

    private fun moveRight() {
        rotationDirection = RotationDirection.Right
        position = when (position) {
            CarPosition.Right -> CarPosition.Right
            CarPosition.Middle -> CarPosition.Right
            CarPosition.Left -> CarPosition.Middle
        }
    }

    private fun moveLeft() {
        rotationDirection = RotationDirection.Left
        position = when (position) {
            CarPosition.Right -> CarPosition.Middle
            CarPosition.Middle -> CarPosition.Left
            CarPosition.Left -> CarPosition.Left
        }
    }

    fun moveWithAcceleration(acceleration: AccelerationData) {
        //todo not working when phone is on a surface (x=0?)
        val ratio = acceleration.x * acceleration.y
        position = if (ratio > ACCELERATION_X_Y_OFFSET_TRIGGER) {
            rotationDirection = RotationDirection.Left
            CarPosition.Left
        } else if (ratio < -ACCELERATION_X_Y_OFFSET_TRIGGER) {
            rotationDirection = RotationDirection.Right
            CarPosition.Right
        } else {
            rotationDirection = when (position) {
                CarPosition.Right -> RotationDirection.Left
                CarPosition.Middle -> null
                CarPosition.Left -> RotationDirection.Right
            }
            CarPosition.Middle
        }
    }

    fun getPosition() = position

}
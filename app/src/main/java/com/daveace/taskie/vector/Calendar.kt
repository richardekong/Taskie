package com.daveace.taskie.vector

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector


public val Icons.Filled.Calendar: ImageVector
    get() {
        if (_calendar != null) return _calendar!!
        _calendar = materialIcon(name = "Filled.Calendar") {
            materialPath {
                moveTo(19f, 4f)
                horizontalLineToRelative(-1f)
                verticalLineTo(2f)
                horizontalLineToRelative(-2f)
                verticalLineTo(4f)
                horizontalLineTo(8f)
                verticalLineTo(2f)
                horizontalLineTo(6f)
                verticalLineTo(4f)
                horizontalLineTo(5f)
                curveTo(3.89f, 4f, 3.01f, 4.9f, 3.01f, 6f)
                lineTo(3f, 20f)
                curveToRelative(0f, 1.1f, 0.89f, 2f, 2f, 2f)
                horizontalLineToRelative(14f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                verticalLineTo(6f)
                curveTo(21f, 4.9f, 20.1f, 4f, 19f, 4f)

                moveTo(19f, 20f)
                horizontalLineTo(5f)
                verticalLineTo(10f)
                horizontalLineTo(14f)
                horizontalLineTo(19f)
                verticalLineTo(20f)

                moveTo(9f, 14f)
                horizontalLineTo(7f)
                verticalLineTo(12f)
                horizontalLineTo(9f)
                verticalLineTo(14f)

                moveTo(13f, 14f)
                horizontalLineTo(11f)
                verticalLineTo(12f)
                horizontalLineTo(13f)
                verticalLineTo(14f)

                moveTo(17f, 14f)
                horizontalLineTo(15f)
                verticalLineTo(12f)
                horizontalLineTo(17f)
                verticalLineTo(14f)

                moveTo(9f, 18f)
                horizontalLineTo(7f)
                verticalLineTo(16f)
                horizontalLineTo(9f)
                verticalLineTo(18f)

                moveTo(13f, 18f)
                horizontalLineTo(11f)
                verticalLineTo(16f)
                horizontalLineTo(13f)
                verticalLineTo(18f)

                moveTo(17f, 18f)
                horizontalLineTo(15f)
                verticalLineTo(16f)
                horizontalLineTo(17f)
                verticalLineTo(18f)
            }
        }
        return _calendar!!
    }

private var _calendar: ImageVector? = null
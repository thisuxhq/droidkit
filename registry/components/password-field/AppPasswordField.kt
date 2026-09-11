package com.droidkit.registry.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private val PasswordToggleSize = 48.dp
private val PasswordIconSize = 24.dp

// Material Visibility / VisibilityOff paths, inlined so the copied file does not
// need material-icons-extended (icons-core does not ship these).
private val Visibility: ImageVector =
    ImageVector.Builder(
        name = "Filled.Visibility",
        defaultWidth = PasswordIconSize,
        defaultHeight = PasswordIconSize,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.0f, 4.5f)
            curveTo(7.0f, 4.5f, 2.73f, 7.61f, 1.0f, 12.0f)
            curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
            reflectiveCurveToRelative(9.27f, -3.11f, 11.0f, -7.5f)
            curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
            close()
            moveTo(12.0f, 17.0f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
            reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
            reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
            close()
            moveTo(12.0f, 9.0f)
            curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
            reflectiveCurveToRelative(1.34f, 3.0f, 3.0f, 3.0f)
            reflectiveCurveToRelative(3.0f, -1.34f, 3.0f, -3.0f)
            reflectiveCurveToRelative(-1.34f, -3.0f, -3.0f, -3.0f)
            close()
        }
    }.build()

private val VisibilityOff: ImageVector =
    ImageVector.Builder(
        name = "Filled.VisibilityOff",
        defaultWidth = PasswordIconSize,
        defaultHeight = PasswordIconSize,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.0f, 7.0f)
            curveToRelative(2.76f, 0.0f, 5.0f, 2.24f, 5.0f, 5.0f)
            curveToRelative(0.0f, 0.65f, -0.13f, 1.26f, -0.36f, 1.83f)
            lineToRelative(2.92f, 2.92f)
            curveToRelative(1.51f, -1.26f, 2.7f, -2.89f, 3.43f, -4.75f)
            curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
            curveToRelative(-1.4f, 0.0f, -2.74f, 0.25f, -3.98f, 0.7f)
            lineToRelative(2.16f, 2.16f)
            curveTo(10.74f, 7.13f, 11.35f, 7.0f, 12.0f, 7.0f)
            close()
            moveTo(2.0f, 4.27f)
            lineToRelative(2.28f, 2.28f)
            lineToRelative(0.46f, 0.46f)
            curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1.0f, 12.0f)
            curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
            curveToRelative(1.55f, 0.0f, 3.03f, -0.3f, 4.38f, -0.84f)
            lineToRelative(0.42f, 0.42f)
            lineTo(19.73f, 22.0f)
            lineTo(21.0f, 20.73f)
            lineTo(3.27f, 3.0f)
            lineTo(2.0f, 4.27f)
            close()
            moveTo(7.53f, 9.8f)
            lineToRelative(1.55f, 1.55f)
            curveToRelative(-0.05f, 0.21f, -0.08f, 0.43f, -0.08f, 0.65f)
            curveToRelative(0.0f, 1.66f, 1.34f, 3.0f, 3.0f, 3.0f)
            curveToRelative(0.22f, 0.0f, 0.44f, -0.03f, 0.65f, -0.08f)
            lineToRelative(1.55f, 1.55f)
            curveToRelative(-0.67f, 0.33f, -1.41f, 0.53f, -2.2f, 0.53f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            curveToRelative(0.0f, -0.79f, 0.2f, -1.53f, 0.53f, -2.2f)
            close()
            moveTo(11.84f, 9.02f)
            lineToRelative(3.15f, 3.15f)
            lineToRelative(0.02f, -0.16f)
            curveToRelative(0.0f, -1.66f, -1.34f, -3.0f, -3.0f, -3.0f)
            lineToRelative(-0.17f, 0.01f)
            close()
        }
    }.build()

/**
 * Password field that owns show/hide, autofill, IME, and on-field error text.
 *
 * error → typing: [isError] is caller-owned; update it on each keystroke (validate-as-you-type).
 * toggle: [IconButton] flips local visibility; [Icon] contentDescription is "Show password" / "Hide password".
 * rotation: visibility uses [rememberSaveable], so a reveal survives configuration change.
 */
@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    contentType: ContentType = ContentType.Password,
    initialVisible: Boolean = false,
) {
    require(!isError || !supportingText.isNullOrBlank()) {
        "AppPasswordField: isError requires supportingText so the error is not color-only"
    }

    var visible by rememberSaveable { mutableStateOf(initialVisible) }
    val focusManager = LocalFocusManager.current
    val performImeAction = {
        if (onImeAction != null) {
            onImeAction()
        } else {
            when (imeAction) {
                ImeAction.Next -> focusManager.moveFocus(FocusDirection.Next)
                ImeAction.Done, ImeAction.Go, ImeAction.Send, ImeAction.Search ->
                    focusManager.clearFocus()
                else -> Unit
            }
        }
    }

    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        visualTransformation =
            if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { performImeAction() },
                onGo = { performImeAction() },
                onNext = { performImeAction() },
                onSend = { performImeAction() },
                onSearch = { performImeAction() },
            ),
        contentType = contentType,
        trailingIcon = {
            IconButton(
                onClick = { visible = !visible },
                enabled = enabled,
                modifier = Modifier.size(PasswordToggleSize),
            ) {
                Icon(
                    imageVector = if (visible) VisibilityOff else Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password",
                )
            }
        },
    )
}

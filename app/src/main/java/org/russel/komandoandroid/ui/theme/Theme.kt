package org.russel.komandoandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.russel.komandoandroid.R
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,            // main brand color
    onPrimary = Color.White,      // text/icons on primary

    secondary = Green60,          // supporting color
    onSecondary = Color.White,

    tertiary = Gold80,            // accent
    onTertiary = Color.White,

    background = Neutral10,
    onBackground = Neutral90,

    surface = Neutral20,
    onSurface = Neutral90,

    error = Error40,
    onError = Color.White,

    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral90,

    outline = Neutral90.copy(alpha = 0.3f)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)
val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = Typography().titleMedium.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = Typography().titleSmall.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium
    ),
    bodyMedium = Typography().bodyMedium.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = Typography().bodySmall.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = Typography().labelMedium.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = Typography().labelMedium.copy(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium
    )
)
@Composable
fun KomandoandroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
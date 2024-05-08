package com.idapgroup.bluroverflowtext

import androidx.annotation.FloatRange
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

/**
 * BlurOverflowText adds blur overflow effect to the last line of the given text.
 * Blur will take effect only if [maxLines] is set.
 * BlurOverflowText has all the parameters from [Text], except of `overflow` in a case
 * that we override text overflow behavior.
 * @param blurLineWidth - the width of blur for overflow ending line in range (0.0..1.0).
 * For example:
 *  0.0f - no overflowed line blur;
 *  1.0f - blur start from beginning of overflowed line.
 */
@Composable
fun BlurOverflowText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    @FloatRange(0.0, 1.0)
    blurLineWidth: Float = 0.2f,
) {
    require(blurLineWidth in 0.0f..1.0f) {
        "blurLineWidth must be in range from 0f to 1f"
    }
    var textToDraw by remember { mutableStateOf(AnnotatedString(text)) }
    var calculatedTextOverflow by remember { mutableStateOf(TextOverflow.Ellipsis) }
    val blurStartColor = when {
        color != Color.Unspecified -> color
        style.color != Color.Unspecified -> style.color
        else -> LocalContentColor.current
    }
    Text(
        text = textToDraw,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = calculatedTextOverflow,
        minLines = minLines,
        maxLines = maxLines,
        onTextLayout = { result ->
            if (result.hasVisualOverflow) {
                // The text returned without 3 dots. So we have some extra space to draw symbols
                val size = result.getLineEnd(result.lineCount - 1, true) + 2
                val top = result.getLineTop(result.lineCount - 1)
                val bottom = result.getLineBottom(result.lineCount - 1)
                val start = result.getLineStart(result.lineCount - 1)
                val ellipsisText = text.take(size)
                val width = result.size.width.toFloat()

                textToDraw = buildAnnotatedString {
                    append(ellipsisText.take(start))
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    blurStartColor.copy(alpha = blurStartColor.alpha * 0.9f),
                                    blurStartColor.copy(alpha = blurStartColor.alpha * 0.3f),
                                    blurStartColor.copy(alpha = 0.1f),
                                    blurStartColor.copy(alpha = 0.1f),
                                    blurStartColor.copy(alpha = 0f),
                                ),
                                start = Offset(width - width * blurLineWidth, top),
                                end = Offset(width, bottom),
                            ),

                            )
                    ) {
                        append(ellipsisText.takeLast(size - start))
                    }
                }
                calculatedTextOverflow = TextOverflow.Clip
            }
            onTextLayout(result)
        },
        style = style,
    )
}

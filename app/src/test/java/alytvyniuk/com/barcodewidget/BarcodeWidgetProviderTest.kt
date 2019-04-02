package alytvyniuk.com.barcodewidget

import android.widget.RemoteViews
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.stubbing.Answer

class BarcodeWidgetProviderTest {

//    @Test
//    fun setFramePaddings() {
//        val bitmapWidth = 200
//        val bitmapHeight = 200
//        val widgetWidth = 1191
//        val widgetHeight = 472
//        val remoteViews = mock<RemoteViews> {
//            on {
//                setViewPadding(any(), any(), any(), any(), any())
//            } doAnswer(Answer<Unit> { invocation ->
//                val left = invocation.getArgument<Int>(1)
//                val top = invocation.getArgument<Int>(2)
//                val right = invocation.getArgument<Int>(3)
//                val bottom = invocation.getArgument<Int>(4)
//                val ratioBitmap = bitmapWidth.toFloat() / bitmapHeight
//                val ratioWidget = (widgetWidth - left - right).toFloat() /
//                        (widgetHeight - top - bottom)
//                println("ratioBitmap=$ratioBitmap, ratioWidget=$ratioWidget, initialRatioWidget=${widgetWidth.toFloat() / widgetHeight}")
//                println("Paddings left=$left, top=$top, right=$right, bottom=$bottom")
//                assertEquals(ratioBitmap, ratioWidget, 0.1F)
//                assert(Math.abs(left - right) <= 1)
//                assert(Math.abs(top - bottom) <= 1)
//            })
//        }
//        val p = BarcodeWidgetProvider()
//        p.setFramePaddings(remoteViews, bitmapWidth, bitmapHeight, widgetWidth, widgetHeight)
//    }
}
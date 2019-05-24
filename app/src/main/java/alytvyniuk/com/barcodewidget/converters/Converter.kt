package alytvyniuk.com.barcodewidget.converters

interface Converter<FROM, TO> {

    suspend fun convert(from: FROM) : TO
}

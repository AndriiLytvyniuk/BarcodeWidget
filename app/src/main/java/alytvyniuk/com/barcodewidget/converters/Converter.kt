package alytvyniuk.com.barcodewidget.converters

interface Converter<FROM, TO> {

    fun convert(from: FROM) : TO
}
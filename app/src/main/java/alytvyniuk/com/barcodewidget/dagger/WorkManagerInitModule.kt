package alytvyniuk.com.barcodewidget.dagger

import alytvyniuk.com.barcodewidget.WidgetUpdateWorkerFactory
import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import android.app.Application
import androidx.annotation.NonNull
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CodeToImageConverterModule::class, AppContextModule::class, BarcodeDaoModule::class])
class WorkManagerInitModule {

    @Provides
    @Singleton
    @NonNull
    fun providesWorkManager(application: Application, barcodeDao: BarcodeDao,
                            codeToImageConverter: CodeToImageConverter) : WorkManager {
        val widgetUpdateWorkerFactory = WidgetUpdateWorkerFactory(barcodeDao, codeToImageConverter)
        val configuration = Configuration.Builder().setWorkerFactory(widgetUpdateWorkerFactory).build()
        WorkManager.initialize(application, configuration)
        return WorkManager.getInstance()
    }
}

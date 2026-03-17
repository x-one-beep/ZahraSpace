package com.zahra.space.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zahra.space.data.dao.*
import com.zahra.space.data.entity.*

@Database(
    entities = [
        User::class,
        QuranAyat::class,
        Hadist::class,
        Dzikir::class,
        DailyChecklist::class,
        Todo::class,
        FitnessTarget::class,
        Pet::class,
        MonthlyLetter::class,
        HiddenMessage::class,
        Restaurant::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun quranDao(): QuranDao
    abstract fun hadistDao(): HadistDao
    abstract fun dzikirDao(): DzikirDao
    abstract fun dailyChecklistDao(): DailyChecklistDao
    abstract fun todoDao(): TodoDao
    abstract fun fitnessDao(): FitnessDao
    abstract fun petDao(): PetDao
    abstract fun monthlyLetterDao(): MonthlyLetterDao
    abstract fun hiddenMessageDao(): HiddenMessageDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zahra_space.db"
                )
                .createFromAsset("databases/zahra_space.db")
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

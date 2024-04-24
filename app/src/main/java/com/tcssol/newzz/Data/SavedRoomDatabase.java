package com.tcssol.newzz.Data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Utils.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities={News.class},version=1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class SavedRoomDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS=4;
    public static final String DATABASE_NAME="saved_database_new";
    private static volatile SavedRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDatabaseCallback=
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(()->{
                        //invoke Dao, and write
                        SavedDao dao=INSTANCE.savedDao();
                        dao.deleteAll();// clean slate

                        //writing to our table
                    });
                }
            };

    public static SavedRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (SavedRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                                    SavedRoomDatabase.class,DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract SavedDao savedDao();

}

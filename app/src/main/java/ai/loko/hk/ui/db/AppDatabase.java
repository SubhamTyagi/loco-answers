package ai.loko.hk.ui.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ProfileEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfileDAO profileDAO();

    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (AppDatabase.class){
                INSTANCE= Room.databaseBuilder(context,AppDatabase.class,"database-for_profile").build();
            }
        }
        return INSTANCE;
    }
}

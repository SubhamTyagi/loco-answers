package ai.loko.hk.ui.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProfileDAO {

    @Insert
    void insert(ProfileEntity profileEntity);
    @Delete
    void delete(ProfileEntity profileEntity);

    @Query("SELECT * FROM ProfileEntity")
    List<ProfileEntity> getAll();
}

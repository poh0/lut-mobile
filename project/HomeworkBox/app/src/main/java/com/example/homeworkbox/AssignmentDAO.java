package com.example.homeworkbox;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssignmentDAO {
    @Insert(onConflict = REPLACE)
    void insert(Assignment assignment);

    @Query("SELECT * FROM assignments ORDER BY deadlineDate ASC")
    List<Assignment> getAll();

    @Delete
    void delete(Assignment assignment);
}

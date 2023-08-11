package com.example.emergycontactapp.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.emergycontactapp.Entities.EmergyContact;

import java.util.List;

@Dao
public interface EmergyContactDao {

    @Insert
    void insert(EmergyContact nuevo);

    @Update
    void update(EmergyContact actualizar);

    @Query("DELETE FROM emergy_contact_table")
    void deleteAll();

    @Delete
    void delete(EmergyContact eliminar);

    @Query("select * from emergy_contact_table order by nombre")
    LiveData<List<EmergyContact>> getEmergyContacts();
}

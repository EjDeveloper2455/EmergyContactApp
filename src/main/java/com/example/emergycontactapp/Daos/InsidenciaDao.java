package com.example.emergycontactapp.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Entities.Insidencia;

import java.util.List;

@Dao
public interface InsidenciaDao {

    @Insert
    void insert(Insidencia nuevo);

    @Update
    void update(Insidencia actualizar);

    @Query("DELETE FROM insidencia_table")
    void deleteAll();

    @Delete
    void delete(Insidencia eliminar);

    @Query("select * from insidencia_table order by fecha desc")
    LiveData<List<EmergyContact>> getInsidencias();
}

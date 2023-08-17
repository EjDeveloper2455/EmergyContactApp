package com.example.emergycontactapp.DataBase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.emergycontactapp.Daos.EmergyContactDao;
import com.example.emergycontactapp.Daos.InsidenciaDao;
import com.example.emergycontactapp.Entities.EmergyContact;
import com.example.emergycontactapp.Entities.Insidencia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(version = 1, exportSchema = false, entities = {EmergyContact.class, Insidencia.class})
public abstract class DBApp extends RoomDatabase {
    public abstract EmergyContactDao emergyContactDao();
    public abstract InsidenciaDao insidenciaDao();

    private static volatile DBApp INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //GENERANDO UNA INSTANCIA MEDIANTE PATRÓN DE SOFTWARE SINGLETON
    public static DBApp getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (DBApp.class){
                if(INSTANCE == null){

                    Callback miCallback = new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            databaseWriteExecutor.execute(() -> {
                                EmergyContactDao daoEmergy = INSTANCE.emergyContactDao();
                                daoEmergy.deleteAll();

                                InsidenciaDao daoInsidencia = INSTANCE.insidenciaDao();
                                daoInsidencia.deleteAll();
                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DBApp.class, "DBApp").addCallback(miCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}

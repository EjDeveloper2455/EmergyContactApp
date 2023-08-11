package com.example.emergycontactapp.DataBase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.emergycontactapp.Daos.EmergyContactDao;
import com.example.emergycontactapp.Entities.EmergyContact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(version = 1, exportSchema = false, entities = {EmergyContact.class})
public abstract class DBApp extends RoomDatabase {
    public abstract EmergyContactDao emergyContactDao();

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

                                daoEmergy.insert(new EmergyContact("Elmer","85412365","elmerjmejia55@gmail.com"));
                                daoEmergy.insert(new EmergyContact("David","85414178","david55@gmail.com"));
                                daoEmergy.insert(new EmergyContact("Eli","98123365","eli@gmail.com"));
                                Log.d("msg","llega aqui");
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

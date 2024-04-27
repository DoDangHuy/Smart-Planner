package com.ctk43.doancoso.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ctk43.doancoso.Database.DAO.CategoryDAO;
import com.ctk43.doancoso.Database.DAO.JobDAO;
import com.ctk43.doancoso.Database.DAO.JobDetailDAO;
import com.ctk43.doancoso.Database.DAO.NotificationModelDAO;
import com.ctk43.doancoso.Database.DAO.UserDAO;
import com.ctk43.doancoso.Database.Repository.NoticationRepository;
import com.ctk43.doancoso.Library.CalendarExtension;
import com.ctk43.doancoso.Library.Extension;
import com.ctk43.doancoso.Model.Category;
import com.ctk43.doancoso.Model.Job;
import com.ctk43.doancoso.Model.JobDetail;
import com.ctk43.doancoso.Model.NotificationModel;
import com.ctk43.doancoso.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Database(entities = {Category.class, Job.class, JobDetail.class, User.class , NotificationModel.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "JobManagement.db";

    private static final RoomDatabase.Callback CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new SampledData(instance).execute();
        }
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static AppDatabase instance;

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .addCallback(CALLBACK)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CategoryDAO getCategoryDAO();

    public abstract JobDAO getJobDAO();

    public abstract JobDetailDAO getJobDetailDAO();

    public abstract UserDAO getUserDAO();

    public abstract NotificationModelDAO getNotificationModelDAO();

    private static class SampledData extends AsyncTask<Void, Void, Void> {
        private final CategoryDAO categoryDAO;
        private final JobDAO jobDAO;
        private final JobDetailDAO jobDetailDAO;
        private final UserDAO userDAO;

        private SampledData(AppDatabase db) {
            super();
            categoryDAO = db.getCategoryDAO();
            jobDAO = db.getJobDAO();
            userDAO = db.getUserDAO();
            jobDetailDAO = db.getJobDetailDAO();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            String Date = "31/12/2021";
            Date date;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(Date);
                assert date != null;
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.set(Calendar.HOUR_OF_DAY, 6); // for 6 hour
            calendar.set(Calendar.MINUTE, 0); // for 0 min
            calendar.set(Calendar.SECOND, 0); // for 0 sec
            Date start = Calendar.getInstance().getTime();
            Date end = calendar.getTime();
            userDAO.insert(new User("default@example.vn","Người dùng"));

            categoryDAO.insert(new Category("Default","default@example.vn"),
                    new Category("Study","default@example.vn"),
                    new Category("Entertainment","default@example.vn"));

            return null;
        }
    }
}
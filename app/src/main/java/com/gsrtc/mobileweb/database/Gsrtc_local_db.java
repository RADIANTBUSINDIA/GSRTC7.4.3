package com.gsrtc.mobileweb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ajith on 01/13/2020.
 */

public class Gsrtc_local_db extends SQLiteOpenHelper
{
    private static String DB_NAME = "GSRTC_DB.sqlite";
    private static int DB_VERSION = 7;
    private static Gsrtc_local_db mObj;

    private String mCreateTableLastSearch="CREATE TABLE LAST_SEARCH(_id INTEGER primary key autoincrement,_source_city_id varchar,_source_city_code varchar," +
            "_source_city_name varchar,_dest_city_id varchar,_dest_city_code varchar,_dest_city_name varchar," +
            "_journey_date long,_return_date long,_adult INTEGER,_datefordb TIMESTAMP)";

    private String mCreateTableMyAccount="CREATE TABLE MY_ACCOUNT(_tier_status varchar,_total_Points_Earned varchar,_redeemed_points varchar," +
            "_available_points varchar,_val_of_points varchar,_point_to_next_tier varchar,_bi_membership_no varchar, _balance varchar)";

    private String mCreateTableBusTransaction="CREATE TABLE BUS_TRANSACTION(_fromPlace varchar,_toPlace varchar,_dateOfjourney TIMESTAMP," +
            "_pnrNo varchar,_itineraryNo varchar,_hiddenServiceLogoPath varchar,_ticketStatus varchar,_obNo varchar,_travel_name varchar," +
            "_via_place varchar,_passenger_name varchar,_seat_no varchar,_depart_time varchar,_arrival_time varchar,_fare varchar,_datefordb TIMESTAMP,_stuid varchar, _bookingType TEXT)";

    private String mCreateTableDivyangDisability="CREATE TABLE DIVYANG_DISABILITY(_DCD_ID INTEGER,_DCD_DISABILITY varchar," +
            "_CD_D_CONCESS double,_DCD_DA_CONCESS double,_DCD_D_ID INTEGER,_DCD_DA_ID INTEGER)";


    private String mCreateTablePushIds="CREATE TABLE PUSHIDS(_id INTEGER PRIMARY KEY)";

//    private Gsrtc_local_db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
//    {
//        super(context, DB_NAME, factory, DB_VERSION);
//    }

    public Gsrtc_local_db(Context context, String dbName, Object o, int dbVersion) {
        super( context, DB_NAME, null, 7);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub

        db.execSQL(mCreateTableLastSearch);
        db.execSQL(mCreateTablePushIds);
        db.execSQL(mCreateTableDivyangDisability);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS " + "LAST_SEARCH");
            db.execSQL("DROP TABLE IF EXISTS " + "PUSHIDS");
            db.execSQL("DROP TABLE IF EXISTS " + "DIVYANG_DISABILITY");

            onCreate(db);

    }

    public static Gsrtc_local_db GET_DB_Instance(Context con)
    {
        if (mObj == null)
        {
            mObj = new Gsrtc_local_db(con, DB_NAME, null, 7);
            return mObj;
        } else {
            return mObj;
        }
    }

    public void sInsert_LastSearch(String s_city_id, String s_city_code, String s_city_name, String d_city_id,
                                   String d_city_code, String d_city_name, String journeyDate, String returnDate, int adult, String datefordb)
    {
        ContentValues mCv = new ContentValues();
        mCv.put("_source_city_id", s_city_id);
        mCv.put("_source_city_code", s_city_code);
        mCv.put("_source_city_name", s_city_name);
        mCv.put("_dest_city_id", d_city_id);
        mCv.put("_dest_city_code", d_city_code);
        mCv.put("_dest_city_name", d_city_name);
        mCv.put("_journey_date", journeyDate);
        mCv.put("_return_date", returnDate);
        mCv.put("_adult", adult);
        mCv.put("_datefordb",datefordb);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("LAST_SEARCH", null, mCv);
    }

    public boolean insertDivyangMaster() {

        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO DIVYANG_DISABILITY VALUES (1,'MORE THAN 80% BLIND',100,100,218,158),"+
                "(2,'MORE THAN 80% DEAF & DUMB',100,0,218,79),"+
                "(3,'LESS THAN 70% MENTAL DISORDER',100,100,218,158),"+
                "(4,'MENTAL DISORDER',100,0,218,79),"+
                "(5,'40 to 74 % ORTHO HANDICAP',100,0,218,79),"+
                "(6,'MORE THAN 75% ORTHO HANDICAP',100,50,218,219),"+
                "(101,'Blind 40 or more',100,0,218,79),"+
                "(102,'Blind 80 or more',100,100,218,158),"+
                "(103,'Muscular Dystrophy 40 or more',100,0,218,79),"+
                "(104,'Hearing Impairment 40 or more',100,0,218,79),"+
                "(105,'Hearing Impairment 80 or more',100,50,218,219),"+
                "(106,'Chronic Neurological condition 40 or more',100,0,218,79),"+
                "(107,'Chronic Neurological condition 80 or more',100,50,218,219),"+
                "(108,'Hemophilia 40 or more',100,0,218,79),"+
                "(109,'Low Vision 40 or more',100,0,218,79),"+
                "(110,'Low Vision 80 or more',100,100,218,158),"+
                "(111,'Parkinsons Disease 40 or more',100,0,218,79),"+
                "(112,'Parkinsons Disease 80 or more',100,100,218,158),"+
                "(113,'Intellectual Disability 40 or more',100,100,218,158),"+
                "(114,'Thelassemia 40 or more',100,0,218,79),"+
                "(115,'Thelassemia 80 or more',100,100,218,158),"+
                "(116,'Leprosy Cured Person 40 or more',100,0,218,79),"+
                "(117,'Leprosy Cured Person 80 or more',100,100,218,158),"+
                "(118,'Sickle cell Disease 40 or more',100,0,218,79),"+
                "(119,'Acid Attack Victim 40 or more',100,0,218,79),"+
                "(120,'Locomotor Disability 40 or more',100,0,218,79),"+
                "(121,'Locomotor Disability 80 or more',100,100,218,158),"+
                "(122,'Cerebral Palsy 40 or more',100,100,218,158),"+
                "(123,'Dwarfism 40 or more',100,0,218,79),"+
                "(124,'Mental Illness 40 or more',100,50,218,219),"+
                "(125,'Mental Illness 80 or more',100,100,218,158),"+
                "(126,'Multiple Sclerosis 40 or more',100,0,218,79),"+
                "(127,'Multiple Sclerosis 80 or more',100,100,218,158),"+
                "(128,'Specific Learning Disability 40 or more',100,0,218,79),"+
                "(129,'Speech and Language Disability 40 or more',100,0,218,79),"+
                "(130,'Autrim Spectrum Disorder 40 or more',100,50,218,219),"+
                "(131,'Autrim Spectrum Disorder 80 or more',100,100,218,158),"+
                "(132,'Multiple Disabilities 40 or more',100,100,218,158)";

        db.execSQL(sql);


        return true;
    }

    public Cursor mGetLastSearch()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM LAST_SEARCH";
        return db.rawQuery(Query, null);
    }
    public Cursor mGetLastAllSearch(String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM LAST_SEARCH where _datefordb >='" + date + "' ORDER BY _id DESC";
        Log.e("-------------","SELECT * FROM LAST_SEARCH where _datefordb >='" + date + "' ORDER BY _id DESC");
        return db.rawQuery(Query, null);
    }
    public void deleteLastSearchMoreThenFive(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM LAST_SEARCH where rowid='" + id + "'";
        db.execSQL(deleteQuery);
    }
    public void deleteLastSearchAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("LAST_SEARCH", null, null);
    }

    public void sClear_Temp_Bundle(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from TEMP_BUNDLE");
        Log.e("Bundle Data Deleted", "successfully");
    }


    public void mDeleteTable(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(tableName, null, null);
        Log.e("","deleted table: "+tableName);
    }

    /*GET DIVYANG INFO */
    public Cursor getDivyangIds(String disability) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT _DCD_ID,_DCD_DISABILITY,_DCD_DA_CONCESS,_DCD_D_ID,_DCD_DA_ID FROM DIVYANG_DISABILITY WHERE _DCD_DISABILITY = '" + disability + "'", null);
        return res;
    }

    /**
     * DELETE DivyangIds
     **/
    public void deleteDivyangInfo() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from DIVYANG_DISABILITY");
        db.close();

//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete("DIVYANG_DISABILITY", null, null);
//        db.close();
    }

}



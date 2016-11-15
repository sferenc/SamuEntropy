/*
 * NeuronAnimActivity.java
 *
 * Norbiron Game
 * This is a case study for creating sprites for SamuEntropy/Brainboard.
 *
 * Copyright (C) 2016, Dr. Bátfai Norbert
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Ez a program szabad szoftver; terjeszthető illetve módosítható a
 * Free Software Foundation által kiadott GNU General Public License
 * dokumentumában leírtak; akár a licenc 3-as, akár (tetszőleges) későbbi
 * változata szerint.
 *
 * Ez a program abban a reményben kerül közreadásra, hogy hasznos lesz,
 * de minden egyéb GARANCIA NÉLKÜL, az ELADHATÓSÁGRA vagy VALAMELY CÉLRA
 * VALÓ ALKALMAZHATÓSÁGRA való származtatott garanciát is beleértve.
 * További részleteket a GNU General Public License tartalmaz.
 *
 * A felhasználónak a programmal együtt meg kell kapnia a GNU General
 * Public License egy példányát; ha mégsem kapta meg, akkor
 * tekintse meg a <http://www.gnu.org/licenses/> oldalon.
 *
 * Version history:
 *
 * 0.0.1, 2013.szept.29.
 */
package batfai.samuentropy.brainboard8;

/**
 *
 * @author nbatfai
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by sanyipictures on 2016.11.10..
 */

class Databs extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BatfaisLittleNodes.db";
    private static final String TABLE_NAME    = "BatfaisLittleNodes_table";
    private static final int DATABASE_VERSION = 1;
    public static final String COL_0          = "id";
    public static final String COL_1          = "x";
    public static final String COL_2          = "y";
    public static final String COL_3          = "nodeID";

    public Databs(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE BatfaisLittleNodes_table " +
                " (id integer primary key, " +
                "  x integer, y integer, nodeID integer);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public int getNodeID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT nodeID FROM BatfaisLittleNodes_table WHERE id = " +id+"", null);
        result.moveToFirst();
        return Integer.parseInt(result.getString(result.getColumnIndex(COL_3)));
    }
    public float getX(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT x FROM BatfaisLittleNodes_table WHERE id = " +id+"",null );
        result.moveToFirst();
        return Float.parseFloat(result.getString(result.getColumnIndex(COL_1)));
    }
    public float getY(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT y FROM BatfaisLittleNodes_table WHERE id = " +id+"",null );
        result.moveToFirst();
        return Float.parseFloat(result.getString(result.getColumnIndex(COL_2)));
    }

    public boolean insertALL(int id, int nodeX, int nodeY, int nodeType){
        SQLiteDatabase mydb = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put("id", id);
        ctv.put("x", nodeX);
        ctv.put("y", nodeY);
        ctv.put("nodeID", nodeType);

        mydb.replace("BatfaisLittleNodes_table", null, ctv);

        return true;
    }
    public void deleteDB(){
        SQLiteDatabase mydb = this.getWritableDatabase();
        mydb.execSQL("DELETE * FROM" + TABLE_NAME);
    }



    public int numRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        int numR = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numR;
    }
    public Integer deleteNode(Integer nodeID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("BatfaisLittleNodes_table", "id = ?", new String[] {Integer.toString(nodeID)});
    }
    public boolean updateNode(Integer nodeID, Integer nodeX, Integer nodeY){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put("x", nodeX);
        ctv.put("y", nodeY);
        db.update("BatfaisLittleNodes_table",ctv, "id = ?", new String[] {Integer.toString(nodeID)});
        return true;
    }

    public ArrayList<String> getALL(){

        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor result      = db.rawQuery("SELECT * FROM BatfaisLittleNodes_table", null);

        ArrayList<String> adatB = new ArrayList<String>();
        result.moveToFirst();

        while(result.isAfterLast() == false){
            adatB.add(result.getString(result.getColumnIndex(COL_0)) + " " +
                    result.getString(result.getColumnIndex(COL_1)) + " " +
                    result.getString(result.getColumnIndex(COL_2)));
            result.moveToNext();
        }
        return adatB;
    }
}

public class NeuronGameActivity extends android.app.Activity {

    public static Databs myDB;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        this.myDB = new Databs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(NeuronGameActivity.this, "on pause", Toast.LENGTH_SHORT).show();

        for(int i = 1; i < NorbironSurfaceView.nodeBoxes.size(); ++i){

            int x = NorbironSurfaceView.nodeBoxes.get(i).getX();
            int y = NorbironSurfaceView.nodeBoxes.get(i).getY();
            int nodeID = NorbironSurfaceView.nodeBoxes.get(i).BUZIID;

            myDB.insertALL(i,x,y,nodeID);
        }
    }
}

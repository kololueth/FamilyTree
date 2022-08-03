package lite.sqlite;

import static lite.sqlite.FamoContract.Entry.*;
import static lite.sqlite.FamoContract.SQL_CREATE_MEMBERS;
import static lite.sqlite.FamoContract.SQL_CREATE_UNITS;
import static lite.sqlite.FamoContract.SQL_CREATE_USERS;
import static lite.sqlite.FamoContract.SQL_DELETE_MEMBERS;
import static lite.sqlite.FamoContract.SQL_DELETE_UNITS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import userObjects.ChildSet;
import userObjects.FamilyUnit;
import userObjects.Member;
import userObjects.ParentSet;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Famo2";

    public DbHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

    } // End of DbHelper


    @Override
    public void onCreate(SQLiteDatabase db) {  Log.d(TAG, "Creating Database Tables");

        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_MEMBERS);
        db.execSQL(SQL_CREATE_UNITS);

    } // End of onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  Log.d(TAG, "Upgrading");

        db.execSQL(SQL_DELETE_MEMBERS);
        db.execSQL(SQL_DELETE_UNITS);

        onCreate(db);

    } // End of onUpgrade

    public long addUser(Member member){  Log.d(TAG, "Adding new user");

        SQLiteDatabase db = this.getWritableDatabase();

        long userid;

        ContentValues memberValues = new ContentValues();

        memberValues.put(FIRST_NAME_COLUMN, member.firstName);
        memberValues.put(MIDDLE_NAME_COLUMN, member.middleName);
        memberValues.put(LAST_NAME_COLUMN, member.lastName);
        memberValues.put(BIRTHDATE_COLUMN, member.birthdate);
        memberValues.put(SEX_COLUMN, member.sex);

        userid = db.insert(USER_TABLE, null, memberValues);

        db.insert(MEMBER_TABLE, null, memberValues);


        ContentValues unitValues = new ContentValues();

        unitValues.put(UNIT_ID, 1);
        unitValues.put(MEMBER_ID, 1);
        unitValues.put(MEMBER_TYPE, 1);

        db.insert(UNIT_TABLE, null, unitValues);

        db.close();

        return userid;

    } // End of addUser

    public void addMember(Member member, int memberType) {  Log.d(TAG, "Adding Member " + member.firstName);

        SQLiteDatabase db = this.getWritableDatabase();

        int row;

        ContentValues memberValues = new ContentValues();

        memberValues.put(FIRST_NAME_COLUMN, member.firstName);
        memberValues.put(MIDDLE_NAME_COLUMN, member.middleName);
        memberValues.put(LAST_NAME_COLUMN, member.lastName);
        memberValues.put(BIRTHDATE_COLUMN, member.birthdate);
        memberValues.put(SEX_COLUMN, member.sex);

        row = (int) db.insert(MEMBER_TABLE, null, memberValues);

        ContentValues unitValues = new ContentValues();

        unitValues.put(UNIT_ID, member.fu_id);
        unitValues.put(MEMBER_ID, row);
        unitValues.put(MEMBER_TYPE, memberType);

        db.insert(UNIT_TABLE, null, unitValues);


        db.close();

    } // End of addMember


    public FamilyUnit getMembers(int fuid) {  Log.d(TAG, "Getting Members from FamilyUnit " + fuid);

        SQLiteDatabase db = this.getReadableDatabase();

        ParentSet parents = new ParentSet();
        ChildSet children = new ChildSet();

        Cursor cursorCourses = db.rawQuery("SELECT " + UNIT_TABLE + "." + MEMBER_TYPE + ", " + MEMBER_TABLE + ".* FROM " + UNIT_TABLE + " JOIN " + MEMBER_TABLE + " ON " + UNIT_TABLE + "." + MEMBER_ID + " = " + MEMBER_TABLE + "._id" + " WHERE " + UNIT_TABLE + "." + UNIT_ID +" = " + fuid, null);

        if(cursorCourses != null) {

            cursorCourses.moveToFirst();

            for(int i = 0; i < cursorCourses.getCount(); i++){


                String firstName = cursorCourses.getString(2);
                String middleName = cursorCourses.getString(3);
                String lastName = cursorCourses.getString(4);
                String birthdate = cursorCourses.getString(5);
                String sex = cursorCourses.getString(6);

                Member member = new Member(firstName, middleName, lastName, birthdate, sex);

                member.local_id = cursorCourses.getInt(1);
                member.fu_id = fuid;

                if(cursorCourses.getInt(0) == 1) {

                    member.isChild = true;

                    children.add(member);

                } else if (cursorCourses.getInt(0) == 0){

                    member.isParent = true;

                    parents.add(member);

                }

                Log.d(TAG, "ID = " + member.local_id);
                Log.d(TAG, "First Name " + member.firstName);
                Log.d(TAG, "----------------------------------");

                cursorCourses.moveToNext();

            } // End of for

        } // End of if


        cursorCourses.close();
        db.close();

        FamilyUnit familyUnit = new FamilyUnit(parents, children);

        familyUnit.unitNumber = fuid;

        return familyUnit;


    } // End of getMembers


    public int removeMember(Member member){  Log.d(TAG, "Removing Member " + member.firstName + " from FamilyUnit " + member.fu_id);


        SQLiteDatabase db = this.getWritableDatabase();

        int unitsReturn;
        int membersReturn;

        if(member.isParent){

            unitsReturn = db.delete(UNIT_TABLE, UNIT_ID + " = " + member.fu_id + " AND " + MEMBER_ID + " = " + member.local_id + " AND " + MEMBER_TYPE + " = " + 0, null);

            membersReturn =  db.delete(MEMBER_TABLE, "_id = " + member.local_id, null);

            return membersReturn;

        } else if (member.isChild) {

            unitsReturn = db.delete(UNIT_TABLE, UNIT_ID + " = " + member.fu_id + " AND " + MEMBER_ID + " = " + member.local_id + " AND " + MEMBER_TYPE + " = " + 1, null);

            membersReturn = db.delete(MEMBER_TABLE, "_id = " + member.local_id, null);

            return membersReturn;

        } else {

            return 0;

        }

    } // End of removeMember



} // End of class

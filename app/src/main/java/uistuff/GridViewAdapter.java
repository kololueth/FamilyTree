package uistuff;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.familytree.R;

import java.util.ArrayList;

import userObjects.Member;

public class GridViewAdapter extends BaseAdapter {

    private static final String TAG = "GridViewAdapter";

    public ArrayList<Member> memberList;
    public Context context;
    public Resources resources;


    public GridViewAdapter(Context context, ArrayList<Member> memberList, Resources resources) { Log.d(TAG, "GridViewAdapter Constructor");

        this.memberList = memberList;
        this.context = context;
        this.resources = resources;

    } // End of constructor



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** BASE ADAPTER MUST IMPLEMENT METHODS ****/


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  Log.d(TAG, "GridView item position " + position + " Name " + memberList.get(position).firstName);

        /** Try and order the display of the children by birthdate here */


        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_add_member, null);

        TextView textView = (TextView) convertView.findViewById(R.id.adapter_circle);

            if(memberList.get(position).firstName != null) {

                textView.setText(memberList.get(position).firstName);
                textView.setTextColor(Color.parseColor("#FFFFFF"));

            } else {

                textView.setText("ADD");
                textView.setTextColor(Color.parseColor("#00FF00"));
                textView.setBackground(resources.getDrawable(R.drawable.add_circle));

            } // End of if


        return (View) convertView;

    } // End of getView()

    @Override
    public int getCount() {

        return memberList.size();

    } // End of getCount()

    @Override
    public Member getItem(int position) {

        return memberList.get(position);

    } // End of getItem()

    @Override
    public long getItemId(int position) {

        return 0;

    } // End of getItemId()


    /**** BASE ADAPTER MUST IMPLEMENT METHODS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** METHODS ****/


    /**** METHODS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////





} // End of Class

package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Models.ResultMenuItem;
import smartfoxlabs.biggersmaller.R;

/**
 * Created by dwite_000 on 24.10.2014.
 */
public class ResultMenuAdapter extends ArrayAdapter<ResultMenuItem> {
    private final Context context;
    private final ArrayList<ResultMenuItem> values;
    private int type;

    /**
     *
     * @param context
     * @param values
     * @param type - type of Menu 0 - Result Activity Menu 1 - Main Activity Menu
     */
    public ResultMenuAdapter(Context context, ArrayList<ResultMenuItem> values,int type) {
        super(context, R.layout.row_result_menu, values);
        this.context = context;
        this.values = values;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if (type == 0)
             rowView = inflater.inflate(R.layout.row_result_menu, parent, false);
        else {
            rowView = inflater.inflate(R.layout.row_main_menu, parent, false);
        }
        TextView textView = (TextView) rowView.findViewById(R.id.tvResultMenu);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icResultMenu);
        textView.setText(values.get(position).name);
        imageView.setImageResource(values.get(position).icon);
        return rowView;
    }
}

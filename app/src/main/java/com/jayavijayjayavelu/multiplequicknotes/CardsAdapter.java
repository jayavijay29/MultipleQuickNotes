package com.jayavijayjayavelu.multiplequicknotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayavijayjayavelu on 2/20/17.
 */

public class CardsAdapter extends RecyclerView.Adapter<Cards>  {

    private List<CardValues> cardValues;
    Context context;
    public static int count;

    public CardsAdapter(List<CardValues> cardValues,View recyclerView,Context context) {
        this.cardValues = new ArrayList<CardValues>();
        this.cardValues.addAll(cardValues);
        this.context = context;
        count = cardValues.size();
    }


    @Override
    public Cards onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cards, viewGroup, false);

        return new Cards(itemView);
    }

    @Override
    public void onBindViewHolder(final Cards cardsValueHolder, final int i) {
        final CardValues cardValuesTemp = cardValues.get(count-1);

        final String titleTemp1 = cardValuesTemp.getNotesName();
        String notesTemp1 = cardValuesTemp.getSampleNotes();
        final String notesTemp2=notesTemp1;
        char notesChar[] = notesTemp1.toCharArray();
        if(notesChar.length>=80){
            notesTemp1 =notesTemp1.substring(0,79);
            notesTemp1=notesTemp1+"...";
        }
        cardsValueHolder.notesName.setText(titleTemp1);
        cardsValueHolder.sampleContent.setText(notesTemp1);
        cardsValueHolder.time.setText(cardValuesTemp.getTime1());
        cardsValueHolder.c.setCardBackgroundColor(cardValuesTemp.getIntValue());
        cardsValueHolder.c.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditActivity.class);
                i.putExtra("title",cardsValueHolder.notesName.getText().toString());
                i.putExtra("notes",notesTemp2);
                context.startActivity(i);
            }
        });
        cardsValueHolder.c.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertbox = new AlertDialog.Builder(v.getContext())
                        .setMessage("Do you want to Delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                JSONObject main = MainActivity.temp1;
                                JSONObject valuesObject;
                                try {
                                    JSONArray user1 = main.getJSONArray("NotesList");
                                    for(int i =0; i<user1.length();i++) {
                                        valuesObject = new JSONObject(user1.get(i).toString());
                                        if(titleTemp1.equals(valuesObject.getString("title"))&&
                                                notesTemp2.equals(valuesObject.getString("notes"))){
                                            user1.remove(i);
                                        }
                                    }
                                    JSONObject newObj = new JSONObject();
                                    newObj.accumulate("NotesList",user1);
                                    MainActivity ma = new MainActivity();
                                    ma.updateData(newObj);
                                    Intent i = new Intent(context, MainActivity.class);
                                    context.startActivity(i);
                                }catch (Exception e){

                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
                return true;
            }
        });
        count--;
    }

    @Override
    public int getItemCount() {
        return cardValues.size();
    }
}

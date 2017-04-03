package com.jayavijayjayavelu.multiplequicknotes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jayavijayjayavelu on 2/20/17.
 */

public class Cards extends RecyclerView.ViewHolder{
    protected TextView notesName;
    protected TextView sampleContent;
    protected TextView time;
    protected CardView c;

    public Cards(View itemView) {
        super(itemView);
        notesName = (TextView) itemView.findViewById(R.id.notesName);
        sampleContent = (TextView) itemView.findViewById(R.id.sampleContent);
        time = (TextView) itemView.findViewById(R.id.date);
        c = (CardView) itemView.findViewById(R.id.card);
    }


}

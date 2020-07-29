package com.mmlab.m1.game.fragement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mmlab.m1.R;
import com.mmlab.m1.game.module.AnswerRecordShowing;

import java.util.List;

public class AnswerRecordAdapter extends RecyclerView.Adapter<AnswerRecordAdapter.ViewHolder> {

    List<AnswerRecordShowing> dataset;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView question;
        private final TextView answer;
        private final TextView point;
        private final TextView consequence;
        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            answer = (TextView) v.findViewById(R.id.u_ans_txt);
            question = (TextView) v.findViewById(R.id.num);
            point = (TextView) v.findViewById(R.id.point);
            consequence = (TextView) v.findViewById(R.id.consquence);

        }
        public TextView getQuestionTextView() {
            return question;
        }
        public TextView getAnswerTextView() {
            return answer;
        }
        public TextView getPointTextView() {
            return point;
        }
        public TextView getConsequenceTextView() {
            return consequence;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnswerRecordAdapter(List<AnswerRecordShowing> dataset) {
        this.dataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AnswerRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_record_container, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(dataset.get(position).getQuestion() != null){
            holder.getQuestionTextView().setText("Question : " + dataset.get(position).getQuestion());
        }
        else{
            holder.getQuestionTextView().setText("");
        }


        switch (dataset.get(position).getAnswear()){
            case "T":
                holder.getAnswerTextView().setText("answer : T");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            case "F":
                holder.getAnswerTextView().setText("answer : F");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            case "A":
                holder.getAnswerTextView().setText("answer : A");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            case "B":
                holder.getAnswerTextView().setText("answer : B");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            case "C":
                holder.getAnswerTextView().setText("answer : C");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            case "D":
                holder.getAnswerTextView().setText("answer : D");
                if(dataset.get(position).getCorrectness() == 0){
                    holder.getConsequenceTextView().setText("wrong");
                    holder.getPointTextView().setText("0 point");
                }
                else{
                    holder.getConsequenceTextView().setText("correct");
                    holder.getPointTextView().setText(dataset.get(position).getPoint() + " point");
                }
                break;

            default:
                holder.getAnswerTextView().setText(dataset.get(position).getAnswear());
                holder.getConsequenceTextView().setText("8888");
                holder.getPointTextView().setText(dataset.get(position).getPoint() + "8888 point");

                break;
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

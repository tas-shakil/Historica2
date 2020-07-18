package com.appcommandos.historica.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appcommandos.historica.controller.AppController;
import com.appcommandos.historica.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    ArrayList<Question> questionArrayList = new ArrayList<>();

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListAsyncRespons callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                 for(int i=0; i< response.length(); i++){
                     try {
                         Question question = new Question();

                         question.setAnswer(response.getJSONArray(i).get(0).toString());
                         question.setSnswerTrue(response.getJSONArray(i).getBoolean(1));

                         // add question to the array list
                         questionArrayList.add(question);



                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }

                 if(null != callBack) callBack.processFinished(questionArrayList);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }

}

package android.example.triviaapp;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;

public class Repository {

    private final ArrayList<Question> questionBank = new ArrayList<>();

    public ArrayList<Question> getQuestionBank(final AnswerListAsyncResponse callback) {
        String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(i);
                            String statement = jsonArray.getString(0);
                            Boolean verdict = jsonArray.getBoolean(1);
                            Question question = new Question(statement, verdict);
                            questionBank.add(question);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(callback != null) {
                        callback.processFinished(questionBank);
                    }
                },
                error -> {
                    // TODO: Handle error
                    Log.d("JSON", "Failure");
                });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionBank;
    }
}

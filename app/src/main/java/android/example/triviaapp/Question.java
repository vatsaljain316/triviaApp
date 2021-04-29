package android.example.triviaapp;

public class Question {

    private String statement;
    private Boolean verdict;

    public Question(String statement, Boolean verdict) {
        this.statement = statement;
        this.verdict = verdict;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Boolean getVerdict() {
        return verdict;
    }

    public void setAnswer(Boolean verdict) {
        this.verdict = verdict;
    }
}

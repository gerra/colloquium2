package ru.ifmo.md.colloquium2;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.ifmo.md.colloquium2.db.CandidatesTable;
import ru.ifmo.md.colloquium2.db.MySQLManager;


public class MainActivity extends Activity {
    ListView candidatesListView;
    SimpleCursorAdapter adapter;
    Cursor cursor;

    boolean voting = false;

    MySQLManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new MySQLManager(this);
        cursor = manager.getAllCandidates();
        startManagingCursor(cursor);

        String[] from = new String[] {
                CandidatesTable.CAND_NAME,
                CandidatesTable.CAND_SCORE,
                CandidatesTable.CAND_PERCENT
        };
        int[] to = new int[]{R.id.cand_name, R.id.cand_score, R.id.cand_percent};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
        candidatesListView = (ListView)findViewById(R.id.listView);
        candidatesListView.setAdapter(adapter);

        candidatesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!voting) {
                } else {
                    Cursor curCursor = adapter.getCursor();
                    curCursor.moveToPosition(i);
                    String id = curCursor.getString(curCursor.getColumnIndexOrThrow(CandidatesTable.CAND_ID));
                    //int score = curCursor.getInt(curCursor.getColumnIndexOrThrow(CandidatesTable.CAND_SCORE));
                    manager.increaseScore(id);
                    cursor.requery();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            manager.resetScore();
            stopVoting();
            cursor.requery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startVoting() {
        ((Button)findViewById(R.id.votingButton)).setText("Stop voting");
        ((Button)findViewById(R.id.addCandidateButton)).setClickable(false);
        voting = true;
    }

    private void stopVoting() {
        ((Button)findViewById(R.id.votingButton)).setText("Start voting");
        ((Button)findViewById(R.id.addCandidateButton)).setClickable(true);
        voting = false;
    }

    public void onAddClick(View view) {
        String candidateName = ((EditText)findViewById(R.id.editCandidate)).getText().toString();
        manager.addCandidateByName(candidateName);
        cursor.requery();
    }

    public void onVotingClick(View view) {
        if (voting) {
            stopVoting();
        } else {
            startVoting();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.close();
    }
}

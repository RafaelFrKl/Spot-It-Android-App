/**
 * Win Screen Popup
 */
package cmpt276.assignment.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class WinMessageFragment extends AppCompatDialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View V = LayoutInflater.from(getActivity()).inflate(R.layout.finish_layout, null);

        //TextView highscoretext = (TextView) V.findViewById(R.id.scoretext);
        //highscoretext.setText(" Seconds");
        Button highscores = (Button) V.findViewById(R.id.highscorefromgame);
        Button mainmenu = (Button) V.findViewById(R.id.backtomain);

        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), HighScores.class);
                getContext().startActivity(i);
            }
        });

        //listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        };

        //alert
        return  new AlertDialog.Builder(getActivity())
                .setMessage("Congratulations You have spotted all the items")
                .setView(V)
                .create();
    }

}

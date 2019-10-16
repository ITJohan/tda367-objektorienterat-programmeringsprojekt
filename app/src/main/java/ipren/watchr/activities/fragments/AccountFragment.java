package ipren.watchr.activities.fragments;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import ipren.watchr.Helpers.Util;
import ipren.watchr.R;
import ipren.watchr.viewModels.AccountViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class AccountFragment extends Fragment {

    AccountViewModel accountViewModel;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Signs the user out and vibrates the phone
        getView().findViewById(R.id.logout_btn).setOnClickListener(e -> {
            ((Vibrator) getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            Navigation.findNavController(getView()).popBackStack();
            Toast.makeText(getContext(), "You've been logged out", Toast.LENGTH_SHORT).show();
            accountViewModel.signOut();
        });

        getView().findViewById(R.id.configure_acc_btn).setOnClickListener(e ->
                Navigation.findNavController(getView()).navigate(R.id.action_global_account_settings));

        accountViewModel.getUser().observe(this, e -> {
            ((TextView) getView().findViewById(R.id.username_text_field_acc)).setText(e.getUserName());
            ((TextView) getView().findViewById(R.id.email_text_field_acc)).setText(e.getEmail());
            Util.loadImage(
                    (getView().findViewById(R.id.profile_img_acc)),
                    e.getUserProfilePictureUri().toString(),
                    Util.getProgressDrawable(getContext()));

        });


    }
}

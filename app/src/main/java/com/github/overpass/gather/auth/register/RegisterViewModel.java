package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.auth.register.add.AddPersonalDataFragment;
import com.github.overpass.gather.auth.register.confirm.ConfirmCodeFragment;
import com.github.overpass.gather.auth.register.signup.SignUpFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<Integer> registrationData = new MutableLiveData<>();

    {
        registrationData.setValue(0);
    }

    public void next() {
        int current = registrationData.getValue();
        registrationData.setValue(current + 1);
    }

    public RecyclerView.Adapter getAdapter(FragmentManager supportFragmentManager) {
        return new EnrollmentPagerAdapter(supportFragmentManager);
    }

    public LiveData<Integer> getRegistrationProgressData() {
        return registrationData;
    }

    private class EnrollmentPagerAdapter extends FragmentStateAdapter {

        private EnrollmentPagerAdapter(@NonNull FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = SignUpFragment.newInstance();
                    break;
                case 1:
                    fragment = ConfirmCodeFragment.newInstance();
                    break;
                default:
                    fragment = AddPersonalDataFragment.newInstance();

            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}

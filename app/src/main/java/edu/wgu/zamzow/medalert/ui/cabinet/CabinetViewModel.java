package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CabinetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CabinetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
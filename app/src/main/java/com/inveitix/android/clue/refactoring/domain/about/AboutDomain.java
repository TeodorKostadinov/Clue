package com.inveitix.android.clue.refactoring.domain.about;

import com.inveitix.android.clue.refactoring.domain.repositories.HelperTextRepository;
import com.inveitix.android.clue.refactoring.domain.repositories.models.HelperTextRecord;

public class AboutDomain implements AboutDomainInterface {

    private ViewListener viewListener;
    private HelperTextRepository repository;
    private AppDataRepository appDataRepository;

    public AboutDomain(HelperTextRepository repository, AppDataRepository appDataRepository) {
        this.repository = repository;
        this.appDataRepository = appDataRepository;
    }

    @Override
    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onUiReady() {
        viewListener.showProgressBar();
        HelperTextRecord record = repository.findByType(
                HelperTextRepository.HelperTextType.ABOUT);
        String version = appDataRepository.getVersion();
        viewListener.hideProgressBar();
        viewListener.showAboutText(record.getText(), version);
    }

    public interface ViewListener {
        void showAboutText(String aboutText, String appVersion);
        void showProgressBar();
        void hideProgressBar();
    }
}

package com.inveitix.android.clue.refactoring.domain.about;

public interface AboutDomainInterface {
    void setViewListener(AboutDomain.ViewListener viewListener);
    void onUiReady();
}

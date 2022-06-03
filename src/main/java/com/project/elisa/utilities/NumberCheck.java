package com.project.elisa.utilities;

import org.springframework.stereotype.Component;

@Component
public class NumberCheck {
    public boolean checkNegativeInt(int qoh,int price) {
        if (qoh < 0 || price< 0) {
            return false;
        } else {
            return true;
        }
    }
}

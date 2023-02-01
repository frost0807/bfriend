package com.frost.bfriend.common.util.certification;

import org.springframework.stereotype.Component;

import java.util.Random;

import static com.frost.bfriend.common.constants.EmailConstants.SPECIAL_SYMBOL_LIST;

@Component
public class CertificationGenerator {

    protected String createRandomCode() {
        Random random = new Random();
        StringBuffer code = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    code.append(generateRandomUpperCaseAlphabet(random));
                    break;
                case 1:
                    code.append(generateRandomLowerCaseAlphabet(random));
                    break;
                case 2:
                    code.append(generateRandomNumber(random));
                    break;
            }
        }
        return code.toString();
    }

    protected String createRandomPassword() {
        Random random = new Random();
        StringBuffer newPassword = new StringBuffer();
        boolean[] checkCaseUsed = {false, false, false, false};

        for (int i = 0; i < 20; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    newPassword.append(generateRandomUpperCaseAlphabet(random));
                    checkCaseUsed[index] = true;
                    break;
                case 1:
                    newPassword.append(generateRandomLowerCaseAlphabet(random));
                    checkCaseUsed[index] = true;
                    break;
                case 2:
                    newPassword.append(generateRandomNumber(random));
                    checkCaseUsed[index] = true;
                    break;
                case 3:
                    newPassword.append(generateRandomSpecialSymbol(random));
                    checkCaseUsed[index] = true;
            }

            if(i==19&&!(checkCaseUsed[0]&&checkCaseUsed[1]&&checkCaseUsed[2]&&checkCaseUsed[3])) {
                newPassword = new StringBuffer();
                i=-1;
            }
        }

        return newPassword.toString();
    }

    private char generateRandomUpperCaseAlphabet(Random random) {
        return (char) ((int) random.nextInt(26) + 97);
    }

    private char generateRandomLowerCaseAlphabet(Random random) {
        return (char) ((int) random.nextInt(26) + 65);
    }

    private int generateRandomNumber(Random random) {
        return random.nextInt(9);
    }

    private char generateRandomSpecialSymbol(Random random) {
        return SPECIAL_SYMBOL_LIST[(int) random.nextInt(8)];
    }
}
